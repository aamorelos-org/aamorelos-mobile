package org.aamorelos.mobile.infrastructure.adapters.outbound

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.aamorelos.mobile.data.local.AAMorelosDatabase
import org.aamorelos.mobile.data.local.ReflexionDiariaEntity
import org.aamorelos.mobile.data.remote.AaMorelosApi
import org.aamorelos.mobile.data.remote.toModel
import org.aamorelos.mobile.domain.ApiResult
import org.aamorelos.mobile.domain.model.ReflexionDiariaModel
import org.aamorelos.mobile.domain.ports.ReflexionDiariaRepositoryPort
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val TAG_REFLEXION_DIARIA = "ReflexionDiariaRepo"

/**
 * Adaptador de salida con caché local (offline-first) — hexagonal.
 *  - Si hay caché fresca -> se sirve desde local (sin consultar la red).
 *  - Si no -> consulta la API, guarda en local y entrega los datos.
 *  - Si la API falla -> respalda con los datos guardados en local.
 * La respuesta trae `data: [...]` con 1 elemento; se extrae `firstOrNull()`.
 */
class ReflexionDiariaRepositoryAdapter @Inject constructor(
    private val api: AaMorelosApi,
    private val database: AAMorelosDatabase
) : ReflexionDiariaRepositoryPort {

    override fun getReflexionDiaria(
        fecha: String
    ): Flow<ApiResult<ReflexionDiariaModel>> = flow {
        emit(ApiResult.Loading)

        try {
            val cached = database.getReflexionDiaria(fecha, freshTimestamp())
            if (cached != null) {
                emit(ApiResult.Success(cached, fromCache = true))
                return@flow
            }
        } catch (e: Exception) {
            // Si falla la lectura de la caché local se continúa con intento de red
        }

        try {
            val response = api.getReflexionDiaria(fecha = fecha)
            val first = response.data.firstOrNull()
            if (first == null) {
                emit(ApiResult.Error("No se encontró reflexión para la fecha $fecha"))
                return@flow
            }
            val model = first.toModel()
            try {
                database.saveReflexionDiaria(model.toEntity())
            } catch (e: Exception) {
                Log.w(TAG_REFLEXION_DIARIA, "No se pudo guardar en caché fecha=$fecha: ${e.message}")
            }
            Log.d(TAG_REFLEXION_DIARIA, "API reflexion diaria OK fecha=$fecha titulo=${model.titulo}")
            emit(ApiResult.Success(model, fromCache = false))
        } catch (e: HttpException) {
            Log.e(TAG_REFLEXION_DIARIA, "Fallo consumo API reflexion diaria (HTTP): ${e.message} fecha=$fecha", e)
            emitFromCacheOrError(e, fecha)
        } catch (e: IOException) {
            Log.e(TAG_REFLEXION_DIARIA, "Fallo consumo API reflexion diaria (sin conexión): ${e.message} fecha=$fecha", e)
            emitFromCacheOrError(e, fecha)
        } catch (e: Exception) {
            Log.e(TAG_REFLEXION_DIARIA, "Fallo consumo API reflexion diaria (inesperado): ${e.message} fecha=$fecha", e)
            emitFromCacheOrError(e, fecha)
        }
    }

    private suspend fun emitFromCacheOrError(
        e: Exception,
        fecha: String
    ): ApiResult<ReflexionDiariaModel> {
        // Fallback: cualquier copia local (aunque esté vencida) es mejor que un error
        val fallback = try {
            database.getReflexionDiaria(fecha, 0L)
        } catch (ex: Exception) {
            null
        }
        return if (fallback != null) {
            ApiResult.Success(fallback, fromCache = true)
        } else {
            Log.e(TAG_REFLEXION_DIARIA, "Fallo reflexion diaria y no hay cache disponible fecha=$fecha: ${e.message}", e)
            ApiResult.Error("Error al cargar la reflexión diaria: ${e.message}", e)
        }
    }

    private fun freshTimestamp(): Long = System.currentTimeMillis() - CACHE_TTL_MILLIS

    private fun ReflexionDiariaModel.toEntity(): ReflexionDiariaEntity = ReflexionDiariaEntity(
        fecha = fecha,
        id = id,
        titulo = titulo,
        texto = texto,
        cita = cita,
        contenido = contenido,
        ultimaActualizacion = System.currentTimeMillis()
    )

    companion object {
        private const val CACHE_TTL_MILLIS = 24 * 60 * 60 * 1000L // 24 horas
    }
}
