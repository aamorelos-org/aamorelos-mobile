package org.aamorelos.mobile.infrastructure.adapters.outbound

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.aamorelos.mobile.data.local.AAMorelosDatabase
import org.aamorelos.mobile.data.local.GrupoDetailEntity
import org.aamorelos.mobile.data.remote.AaMorelosApi
import org.aamorelos.mobile.data.remote.toModel
import org.aamorelos.mobile.domain.ApiResult
import org.aamorelos.mobile.domain.model.GrupoDetailModel
import org.aamorelos.mobile.domain.ports.GrupoDetailRepositoryPort
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val TAG_DETAIL = "GrupoDetailRepository"

/**
 * Adaptador de salida con caché local (offline-first) — hexagonal.
 *  - Si hay caché fresca -> se sirve desde local (sin consultar la red).
 *  - Si no -> consulta la API, guarda en local y entrega los datos.
 *  - Si la API falla -> respalda con los datos guardados en local.
 */
class GrupoDetailRepositoryAdapter @Inject constructor(
    private val api: AaMorelosApi,
    private val database: AAMorelosDatabase
) : GrupoDetailRepositoryPort {

    override fun getGrupoDetail(
        estadoId: String,
        grupoId: String
    ): Flow<ApiResult<GrupoDetailModel>> = flow {
        emit(ApiResult.Loading)

        try {
            val cached = database.getGrupoDetail(estadoId, grupoId, freshTimestamp())
            if (cached != null) {
                emit(ApiResult.Success(cached, fromCache = true))
                return@flow
            }
        } catch (e: Exception) {
            // Si falla la lectura de la caché local se continúa con intento de red
        }

        try {
            val dto = api.getGrupoDetail(estadoId = estadoId, grupoId = grupoId)
            val model = dto.toModel()
            try {
                database.saveGrupoDetail(model.toEntity(estadoId))
            } catch (e: Exception) {
                Log.w(TAG_DETAIL, "No se pudo guardar detalle en caché id=$grupoId: ${e.message}")
            }
            Log.d(TAG_DETAIL, "API grupo detail OK id=$grupoId estadoId=$estadoId nombre=${model.nombre}")
            emit(ApiResult.Success(model, fromCache = false))
        } catch (e: HttpException) {
            Log.e(TAG_DETAIL, "Fallo consumo API detalle grupo (HTTP): ${e.message} id=$grupoId", e)
            emitFromCacheOrError(e, estadoId, grupoId)
        } catch (e: IOException) {
            Log.e(TAG_DETAIL, "Fallo consumo API detalle grupo (sin conexión): ${e.message} id=$grupoId", e)
            emitFromCacheOrError(e, estadoId, grupoId)
        } catch (e: Exception) {
            Log.e(TAG_DETAIL, "Fallo consumo API detalle grupo (inesperado): ${e.message} id=$grupoId", e)
            emitFromCacheOrError(e, estadoId, grupoId)
        }
    }

    private suspend fun emitFromCacheOrError(
        e: Exception,
        estadoId: String,
        grupoId: String
    ): ApiResult<GrupoDetailModel> {
        // Fallback: cualquier copia local (aunque esté vencida) es mejor que un error
        val fallback = try {
            database.getGrupoDetail(estadoId, grupoId, 0L)
        } catch (ex: Exception) {
            null
        }
        return if (fallback != null) {
            ApiResult.Success(fallback, fromCache = true)
        } else {
            Log.e(TAG_DETAIL, "Fallo detalle grupo y no hay cache disponible id=$grupoId: ${e.message}", e)
            ApiResult.Error("Error al cargar el detalle del grupo: ${e.message}", e)
        }
    }

    private fun freshTimestamp(): Long = System.currentTimeMillis() - CACHE_TTL_MILLIS

    private fun GrupoDetailModel.toEntity(estadoId: String): GrupoDetailEntity = GrupoDetailEntity(
        estadoId = estadoId,
        id = id,
        distritoId = distritoId,
        municipioId = municipioId,
        tipoId = tipoId,
        lenguaId = lenguaId,
        generoId = generoId,
        nombre = nombre,
        fechaCreacion = fechaCreacion,
        direccion = direccion,
        vecinos = vecinos,
        codigoPostal = codigoPostal,
        referencias = referencias,
        localidad = localidad,
        cpr = cpr,
        horarioLunes = horarioLunes,
        horarioMartes = horarioMartes,
        horarioMiercoles = horarioMiercoles,
        horarioJueves = horarioJueves,
        horarioViernes = horarioViernes,
        horarioSabado = horarioSabado,
        horarioDomingo = horarioDomingo,
        horarioObservacion = horarioObservacion,
        lenguajeDetalle = lenguajeDetalle,
        permiteFumar = permiteFumar,
        esActivo = esActivo,
        latitud = latitud,
        longitud = longitud,
        fumarTexto = fumarTexto,
        municipio = municipio,
        tipo = tipo,
        genero = genero,
        lengua = lengua,
        ultimaActualizacion = System.currentTimeMillis()
    )

    companion object {
        private const val CACHE_TTL_MILLIS = 24 * 60 * 60 * 1000L // 24 horas
    }
}
