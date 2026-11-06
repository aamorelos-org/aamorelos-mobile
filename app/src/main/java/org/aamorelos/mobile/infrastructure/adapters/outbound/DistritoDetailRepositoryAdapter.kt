package org.aamorelos.mobile.infrastructure.adapters.outbound

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.aamorelos.mobile.data.local.AAMorelosDatabase
import org.aamorelos.mobile.data.local.DistritoDetailEntity
import org.aamorelos.mobile.data.remote.AaMorelosApi
import org.aamorelos.mobile.data.remote.toModel
import org.aamorelos.mobile.domain.ApiResult
import org.aamorelos.mobile.domain.model.DistritoDetailModel
import org.aamorelos.mobile.domain.ports.DistritoDetailRepositoryPort
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val TAG_DISTRITO_DETAIL = "DistritoDetailRepository"

/**
 * Adaptador de salida con caché local (offline-first) — hexagonal.
 *  - Si hay caché fresca -> se sirve desde local (sin consultar la red).
 *  - Si no -> consulta la API, guarda en local y entrega los datos.
 *  - Si la API falla -> respalda con los datos guardados en local.
 */
class DistritoDetailRepositoryAdapter @Inject constructor(
    private val api: AaMorelosApi,
    private val database: AAMorelosDatabase
) : DistritoDetailRepositoryPort {

    override fun getDistritoDetail(
        distritoId: String
    ): Flow<ApiResult<DistritoDetailModel>> = flow {
        emit(ApiResult.Loading)

        try {
            val cached = database.getDistritoDetail(distritoId, freshTimestamp())
            if (cached != null) {
                emit(ApiResult.Success(cached, fromCache = true))
                return@flow
            }
        } catch (e: Exception) {
            // Si falla la lectura de la caché local se continúa con intento de red
        }

        try {
            val dto = api.getDistritoDetail(distritoId = distritoId)
            val model = dto.toModel()
            try {
                database.saveDistritoDetail(model.toEntity())
            } catch (e: Exception) {
                Log.w(TAG_DISTRITO_DETAIL, "No se pudo guardar detalle en caché id=$distritoId: ${e.message}")
            }
            Log.d(TAG_DISTRITO_DETAIL, "API distrito detail OK id=$distritoId nombre=${model.nombre}")
            emit(ApiResult.Success(model, fromCache = false))
        } catch (e: HttpException) {
            Log.e(TAG_DISTRITO_DETAIL, "Fallo consumo API detalle distrito (HTTP): ${e.message} id=$distritoId", e)
            emitFromCacheOrError(e, distritoId)
        } catch (e: IOException) {
            Log.e(TAG_DISTRITO_DETAIL, "Fallo consumo API detalle distrito (sin conexión): ${e.message} id=$distritoId", e)
            emitFromCacheOrError(e, distritoId)
        } catch (e: Exception) {
            Log.e(TAG_DISTRITO_DETAIL, "Fallo consumo API detalle distrito (inesperado): ${e.message} id=$distritoId", e)
            emitFromCacheOrError(e, distritoId)
        }
    }

    private suspend fun emitFromCacheOrError(
        e: Exception,
        distritoId: String
    ): ApiResult<DistritoDetailModel> {
        // Fallback: cualquier copia local (aunque esté vencida) es mejor que un error
        val fallback = try {
            database.getDistritoDetail(distritoId, 0L)
        } catch (ex: Exception) {
            null
        }
        return if (fallback != null) {
            ApiResult.Success(fallback, fromCache = true)
        } else {
            Log.e(TAG_DISTRITO_DETAIL, "Fallo detalle distrito y no hay cache disponible id=$distritoId: ${e.message}", e)
            ApiResult.Error("Error al cargar el detalle del distrito: ${e.message}", e)
        }
    }

    private fun freshTimestamp(): Long = System.currentTimeMillis() - CACHE_TTL_MILLIS

    private fun DistritoDetailModel.toEntity(): DistritoDetailEntity = DistritoDetailEntity(
        id = id,
        areaId = areaId,
        municipioId = municipioId,
        nombre = nombre,
        distritoNumero = distritoNumero,
        fechaCreacion = fechaCreacion,
        direccion = direccion,
        vecinos = vecinos,
        codigoPostal = codigoPostal,
        referencia = referencia,
        telefono = telefono,
        servicioHoras = servicioHoras,
        esActivo = esActivo,
        municipio = municipio,
        area = area,
        ultimaActualizacion = System.currentTimeMillis()
    )

    companion object {
        private const val CACHE_TTL_MILLIS = 24 * 60 * 60 * 1000L // 24 horas
    }
}
