package org.aamorelos.mobile.infrastructure.adapters.outbound

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.aamorelos.mobile.data.local.AAMorelosDatabase
import org.aamorelos.mobile.data.local.ReflexionDetailEntity
import org.aamorelos.mobile.data.remote.AaMorelosApi
import org.aamorelos.mobile.data.remote.toModel
import org.aamorelos.mobile.domain.ApiResult
import org.aamorelos.mobile.domain.model.ReflexionDetailModel
import org.aamorelos.mobile.domain.ports.ReflexionDetailRepositoryPort
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val TAG_REFLEXION_DETAIL = "ReflexionDetailRepo"

/**
 * Adaptador de salida con caché local (offline-first) — hexagonal.
 *  - Si hay caché fresca -> se sirve desde local (sin consultar la red).
 *  - Si no -> consulta la API, guarda en local y entrega los datos.
 *  - Si la API falla -> respalda con los datos guardados en local.
 * La imagen se cachea aparte en disco (ReflexionesFileFetcher en filesDir/reflexiones/<slogan>.webp).
 */
class ReflexionDetailRepositoryAdapter @Inject constructor(
    private val api: AaMorelosApi,
    private val database: AAMorelosDatabase
) : ReflexionDetailRepositoryPort {

    override fun getReflexionDetail(
        slogan: String
    ): Flow<ApiResult<ReflexionDetailModel>> = flow {
        emit(ApiResult.Loading)

        try {
            val cached = database.getReflexionDetail(slogan, freshTimestamp())
            if (cached != null) {
                emit(ApiResult.Success(cached, fromCache = true))
                return@flow
            }
        } catch (e: Exception) {
            // Si falla la lectura de la caché local se continúa con intento de red
        }

        try {
            val response = api.getReflexionDetail(slogan = slogan)
            val model = response.data.toModel()
            try {
                database.saveReflexionDetail(model.toEntity())
            } catch (e: Exception) {
                Log.w(TAG_REFLEXION_DETAIL, "No se pudo guardar detalle en caché slogan=$slogan: ${e.message}")
            }
            Log.d(TAG_REFLEXION_DETAIL, "API reflexion detail OK slogan=$slogan titulo=${model.titulo}")
            emit(ApiResult.Success(model, fromCache = false))
        } catch (e: HttpException) {
            Log.e(TAG_REFLEXION_DETAIL, "Fallo consumo API detalle reflexion (HTTP): ${e.message} slogan=$slogan", e)
            emitFromCacheOrError(e, slogan)
        } catch (e: IOException) {
            Log.e(TAG_REFLEXION_DETAIL, "Fallo consumo API detalle reflexion (sin conexión): ${e.message} slogan=$slogan", e)
            emitFromCacheOrError(e, slogan)
        } catch (e: Exception) {
            Log.e(TAG_REFLEXION_DETAIL, "Fallo consumo API detalle reflexion (inesperado): ${e.message} slogan=$slogan", e)
            emitFromCacheOrError(e, slogan)
        }
    }

    private suspend fun emitFromCacheOrError(
        e: Exception,
        slogan: String
    ): ApiResult<ReflexionDetailModel> {
        // Fallback: cualquier copia local (aunque esté vencida) es mejor que un error
        val fallback = try {
            database.getReflexionDetail(slogan, 0L)
        } catch (ex: Exception) {
            null
        }
        return if (fallback != null) {
            ApiResult.Success(fallback, fromCache = true)
        } else {
            Log.e(TAG_REFLEXION_DETAIL, "Fallo detalle reflexion y no hay cache disponible slogan=$slogan: ${e.message}", e)
            ApiResult.Error("Error al cargar el detalle de la reflexión: ${e.message}", e)
        }
    }

    private fun freshTimestamp(): Long = System.currentTimeMillis() - CACHE_TTL_MILLIS

    private fun ReflexionDetailModel.toEntity(): ReflexionDetailEntity = ReflexionDetailEntity(
        slogan = slogan,
        id = id,
        titulo = titulo,
        subtitulo = subtitulo,
        imagen = imagen,
        alt = alt,
        url = url,
        descripcion = descripcion,
        ultimaActualizacion = System.currentTimeMillis()
    )

    companion object {
        private const val CACHE_TTL_MILLIS = 24 * 60 * 60 * 1000L // 24 horas
    }
}
