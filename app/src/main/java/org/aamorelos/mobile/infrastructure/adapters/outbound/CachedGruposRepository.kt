package org.aamorelos.mobile.infrastructure.adapters.outbound

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import android.util.Log
import org.aamorelos.mobile.data.local.AAMorelosDatabase
import org.aamorelos.mobile.data.local.GrupoEntity
import org.aamorelos.mobile.data.remote.AaMorelosApi
import org.aamorelos.mobile.data.remote.toModel
import org.aamorelos.mobile.domain.ApiResult
import org.aamorelos.mobile.domain.model.GrupoModel
import org.aamorelos.mobile.domain.ports.GruposRepositoryPort
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val TAG = "CachedGruposRepository"

/**
 * Adaptador de salida con caché local (offline-first) — hexagonal.
 * Implementa el puerto de dominio y añade el respaldo en SQLite local:
 *  - Si hay caché fresca -> se sirve desde local (sin consultar la red).
 *  - Si no -> consulta la API, guarda en local y entrega los datos.
 *  - Si la API falla -> respalda con los datos guardados en local.
 */
class CachedGruposRepository @Inject constructor(
    private val api: AaMorelosApi,
    private val database: AAMorelosDatabase
) : GruposRepositoryPort {

    override fun getGrupos(estadoId: String): Flow<ApiResult<List<GrupoModel>>> = flow {
        emit(ApiResult.Loading)

        try {
            val cached = database.getGrupos(estadoId, freshTimestamp())
            if (cached.isNotEmpty()) {
                emit(ApiResult.Success(cached, fromCache = true))
                return@flow
            }
        } catch (e: Exception) {
            // Si falla la lectura de la caché local se continúa con intento de red
        }

        try {
            val response = api.getGrupos(estadoId)
            val grupos = response.data.map { it.toModel() }
            database.saveGrupos(grupos.map { it.toEntity(estadoId) })
            Log.d(TAG, "API grupos OK (${grupos.size} registros) - estadoId=$estadoId")
            emit(ApiResult.Success(grupos, fromCache = false))
        } catch (e: HttpException) {
            Log.e(TAG, "Fallo consumo API grupos (HTTP): ${e.message}", e)
            emitFromCacheOrError(e, estadoId)
        } catch (e: IOException) {
            Log.e(TAG, "Fallo consumo API grupos (sin conexión): ${e.message}", e)
            emitFromCacheOrError(e, estadoId)
        } catch (e: Exception) {
            Log.e(TAG, "Fallo consumo API grupos (error inesperado): ${e.message}", e)
            emitFromCacheOrError(e, estadoId)
        }
    }

    private suspend fun emitFromCacheOrError(
        e: Exception,
        estadoId: String
    ): ApiResult<List<GrupoModel>> {
        val fallback = database.getGrupos(estadoId, freshTimestamp())
        return if (fallback.isNotEmpty()) {
            ApiResult.Success(fallback, fromCache = true)
        } else {
            Log.e(TAG, "Fallo consumo API grupos y no hay cache disponible: ${e.message}", e)
            ApiResult.Error("Error al cargar grupos: ${e.message}", e)
        }
    }

    private fun freshTimestamp(): Long = System.currentTimeMillis() - CACHE_TTL_MILLIS

    private fun GrupoModel.toEntity(estadoId: String): GrupoEntity = GrupoEntity(
        estadoId = estadoId,
        id = id,
        nombre = nombre,
        distritoId = distritoId,
        municipio = municipio,
        vecinos = vecinos,
        tipo = tipo,
        genero = genero,
        lengua = lengua,
        ultimaActualizacion = System.currentTimeMillis()
    )

    companion object {
        private const val CACHE_TTL_MILLIS = 24 * 60 * 60 * 1000L // 24 horas
    }
}
