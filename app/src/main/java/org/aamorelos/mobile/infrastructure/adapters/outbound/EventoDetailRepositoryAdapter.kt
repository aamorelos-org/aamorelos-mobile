package org.aamorelos.mobile.infrastructure.adapters.outbound

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.aamorelos.mobile.data.local.AAMorelosDatabase
import org.aamorelos.mobile.data.local.EventoAniversarioDiaEntity
import org.aamorelos.mobile.data.local.EventoAniversarioEntity
import org.aamorelos.mobile.data.local.EventoDetailEntity
import org.aamorelos.mobile.data.remote.AaMorelosApi
import org.aamorelos.mobile.data.remote.toModel
import org.aamorelos.mobile.domain.ApiResult
import org.aamorelos.mobile.domain.model.EventoDetailModel
import org.aamorelos.mobile.domain.ports.EventoDetailRepositoryPort
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val TAG_EVENTO_DETAIL = "EventoDetailRepo"

/**
 * Adaptador de salida con caché local (offline-first) — hexagonal.
 *  - Si hay caché fresca -> se sirve desde local (sin consultar la red).
 *  - Si no -> consulta la API, guarda en local y entrega los datos.
 *  - Si la API falla -> respalda con los datos guardados en local.
 * La imagen se cachea aparte en disco (EventosFileFetcher en filesDir/eventos/<slogan>.webp).
 */
class EventoDetailRepositoryAdapter @Inject constructor(
    private val api: AaMorelosApi,
    private val database: AAMorelosDatabase
) : EventoDetailRepositoryPort {

    override fun getEventoDetail(
        eventoId: Int
    ): Flow<ApiResult<EventoDetailModel>> = flow {
        emit(ApiResult.Loading)

        try {
            val cached = database.getEventoDetail(eventoId, freshTimestamp())
            if (cached != null) {
                emit(ApiResult.Success(cached, fromCache = true))
                return@flow
            }
        } catch (e: Exception) {
            // Si falla la lectura de la caché local se continúa con intento de red
        }

        try {
            val response = api.getEventoDetail(id = eventoId)
            val model = response.data.toModel()
            try {
                database.saveEventoDetail(model.toEntity())
            } catch (e: Exception) {
                Log.w(TAG_EVENTO_DETAIL, "No se pudo guardar detalle en caché id=$eventoId: ${e.message}")
            }
            Log.d(TAG_EVENTO_DETAIL, "API evento detail OK id=$eventoId tipo=${model.tipo} titulo=${model.titulo}")
            emit(ApiResult.Success(model, fromCache = false))
        } catch (e: HttpException) {
            Log.e(TAG_EVENTO_DETAIL, "Fallo consumo API detalle evento (HTTP): ${e.message} id=$eventoId", e)
            emitFromCacheOrError(e, eventoId)
        } catch (e: IOException) {
            Log.e(TAG_EVENTO_DETAIL, "Fallo consumo API detalle evento (sin conexión): ${e.message} id=$eventoId", e)
            emitFromCacheOrError(e, eventoId)
        } catch (e: Exception) {
            Log.e(TAG_EVENTO_DETAIL, "Fallo consumo API detalle evento (inesperado): ${e.message} id=$eventoId", e)
            emitFromCacheOrError(e, eventoId)
        }
    }

    private suspend fun emitFromCacheOrError(
        e: Exception,
        eventoId: Int
    ): ApiResult<EventoDetailModel> {
        val fallback = try {
            database.getEventoDetail(eventoId, 0L)
        } catch (ex: Exception) {
            null
        }
        return if (fallback != null) {
            ApiResult.Success(fallback, fromCache = true)
        } else {
            Log.e(TAG_EVENTO_DETAIL, "Fallo detalle evento y no hay cache disponible id=$eventoId: ${e.message}", e)
            ApiResult.Error("Error al cargar el detalle del evento: ${e.message}", e)
        }
    }

    private fun freshTimestamp(): Long = System.currentTimeMillis() - CACHE_TTL_MILLIS

    private fun EventoDetailModel.toEntity(): EventoDetailEntity = EventoDetailEntity(
        id = id,
        tipo = tipo,
        slogan = slogan,
        url = url,
        titulo = titulo,
        subtitulo = subtitulo,
        imagen = imagen,
        alt = alt,
        pie = pie,
        frases = frases ?: emptyList(),
        aniversario = aniversario?.let { an ->
            EventoAniversarioEntity(
                epigrafe = an.epigrafe,
                kicker = an.kicker,
                fraseReflexion = an.fraseReflexion,
                sede = an.sede,
                diasUnidad = an.diasUnidad,
                juntaPublica = an.juntaPublica,
                contactoNombre = an.contactoNombre,
                contactoTelefono = an.contactoTelefono,
                dias = an.dias.map {
                    EventoAniversarioDiaEntity(
                        dia = it.dia,
                        tema = it.tema,
                        fecha = it.fecha,
                        grupos = it.grupos,
                        numero = it.numero,
                        esJuntaPublica = it.esJuntaPublica
                    )
                }
            )
        },
        ultimaActualizacion = System.currentTimeMillis()
    )

    companion object {
        private const val CACHE_TTL_MILLIS = 24 * 60 * 60 * 1000L // 24 horas
    }
}
