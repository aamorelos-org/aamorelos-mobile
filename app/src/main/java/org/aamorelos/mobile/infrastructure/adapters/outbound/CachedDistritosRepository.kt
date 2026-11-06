package org.aamorelos.mobile.infrastructure.adapters.outbound

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import android.util.Log
import org.aamorelos.mobile.data.local.AAMorelosDatabase
import org.aamorelos.mobile.data.local.DistritoEntity
import org.aamorelos.mobile.data.remote.AaMorelosApi
import org.aamorelos.mobile.data.remote.toModel
import org.aamorelos.mobile.domain.ApiResult
import org.aamorelos.mobile.domain.model.DistritoModel
import org.aamorelos.mobile.domain.ports.DistritosRepositoryPort
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val TAG = "CachedDistritosRepository"

/**
 * Adaptador de salida con caché local (offline-first) — hexagonal.
 * Implementa el puerto de dominio y añade el respaldo en SQLite local:
 *  - Si hay caché fresca -> se sirve desde local (sin consultar la red).
 *  - Si no -> consulta la API, guarda en local y entrega los datos.
 *  - Si la API falla -> respalda con los datos guardados en local.
 */
class CachedDistritosRepository @Inject constructor(
    private val api: AaMorelosApi,
    private val database: AAMorelosDatabase
) : DistritosRepositoryPort {

    override fun getDistritos(estadoId: String): Flow<ApiResult<List<DistritoModel>>> = flow {
        emit(ApiResult.Loading)

        try {
            val cached = database.getDistritos(estadoId, freshTimestamp())
            if (cached.isNotEmpty()) {
                emit(ApiResult.Success(cached, fromCache = true))
                return@flow
            }
        } catch (e: Exception) {
            // Si falla la lectura de la caché local se continúa con intento de red
        }

        try {
            val response = api.getDistritos(estadoId)
            val distritos = response.data.map { it.toModel() }
            database.saveDistritos(distritos.map { it.toEntity(estadoId) })
            Log.d(TAG, "API distritos OK (${distritos.size} registros) - estadoId=$estadoId")
            emit(ApiResult.Success(distritos, fromCache = false))
        } catch (e: HttpException) {
            Log.e(TAG, "Fallo consumo API distritos (HTTP): ${e.message}", e)
            emitFromCacheOrError(e, estadoId)
        } catch (e: IOException) {
            Log.e(TAG, "Fallo consumo API distritos (sin conexión): ${e.message}", e)
            emitFromCacheOrError(e, estadoId)
        } catch (e: Exception) {
            Log.e(TAG, "Fallo consumo API distritos (error inesperado): ${e.message}", e)
            emitFromCacheOrError(e, estadoId)
        }
    }

    private suspend fun emitFromCacheOrError(
        e: Exception,
        estadoId: String
    ): ApiResult<List<DistritoModel>> {
        val fallback = database.getDistritos(estadoId, freshTimestamp())
        return if (fallback.isNotEmpty()) {
            ApiResult.Success(fallback, fromCache = true)
        } else {
            Log.e(TAG, "Fallo consumo API distritos y no hay cache disponible: ${e.message}", e)
            ApiResult.Error("Error al cargar distritos: ${e.message}", e)
        }    }

    private fun freshTimestamp(): Long = System.currentTimeMillis() - CACHE_TTL_MILLIS

    private fun DistritoModel.toEntity(estadoId: String): DistritoEntity = DistritoEntity(
        estadoId = estadoId,
        id = id,
        nombre = nombre,
        municipio = municipio,
        vecinos = vecinos,
        area = area,
        areaId = areaId,
        ultimaActualizacion = System.currentTimeMillis()
    )

    private fun DistritoEntity.toModel(): DistritoModel = DistritoModel(
        id = id,
        nombre = nombre,
        municipio = municipio,
        vecinos = vecinos?.takeIf { it.isNotBlank() },
        area = area,
        areaId = areaId
    )

    companion object {
        private const val CACHE_TTL_MILLIS = 24 * 60 * 60 * 1000L // 24 horas
    }
}
