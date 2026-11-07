package org.aamorelos.mobile.notifications

import android.content.Intent
import org.aamorelos.mobile.notifications.PushNotificationDestination.EventoDetail
import org.aamorelos.mobile.notifications.PushNotificationDestination.ReflexionDetail

/**
 * Interpreta el `data` (mapa de texto) de un mensaje FCM o los extras de un
 * [Intent] creado al tocar una notificación, y lo convierte en destino.
 *
 * Claves que manda el servidor (según especificación):
 * - eventos:     tipo = "informativo"|"invitacion", slogan, evento_id
 * - reflexiones: tipo = "reflexion", slogan, reflexion_id
 *
 * Título/cuerpo no forman parte del mapa: llegan en el bloque `notification`
 * de FCM (titulo/subtitulo del servidor) o, en mensajes solo-data, en las
 * claves titulo/subtitulo del propio data. Ver [AaFirebaseMessagingService].
 */
object PushNotificationParser {

    const val KEY_TIPO = "tipo"
    const val KEY_SLOGAN = "slogan"
    const val KEY_EVENTO_ID = "evento_id"
    const val KEY_REFLEXION_ID = "reflexion_id"
    const val KEY_TITULO = "titulo"
    const val KEY_SUBTITULO = "subtitulo"

    /** Todas las claves de data que portamos en los extras del Intent de la notificación. */
    val DATA_KEYS = setOf(
        KEY_TIPO,
        KEY_SLOGAN,
        KEY_EVENTO_ID,
        KEY_REFLEXION_ID,
        KEY_TITULO,
        KEY_SUBTITULO
    )

    /**
     * @return destino o null si el data no trae información suficiente para navegar.
     */
    fun parse(data: Map<String, String>): PushNotificationDestination? {
        val tipo = data[KEY_TIPO]?.trim()?.lowercase().orEmpty()
        val eventoId = data[KEY_EVENTO_ID]?.trim()?.toIntOrNull()
        val reflexionId = data[KEY_REFLEXION_ID]?.trim()?.takeIf { it.isNotEmpty() }
        val slogan = data[KEY_SLOGAN]?.trim()?.takeIf { it.isNotEmpty() }

        // "informativo" e "invitacion" (y cualquier variante de evento) van al
        // detalle de evento con evento_id. La pantalla/pantalla de detalle ya
        // distingue informativo vs. aniversario según la respuesta de la API.
        if (tipo.contains("reflex")) {
            // La ruta/API de detalle de reflexión es por slogan; si el servidor
            // no manda slogan pero sí reflexion_id, ese id actúa como slogan.
            val identificador = slogan ?: reflexionId ?: return null
            return ReflexionDetail(slogan = identificador, reflexionId = reflexionId)
        }
        if (tipo.contains("informativo") || tipo.contains("invitacion") ||
            tipo.contains("invitación") || tipo.contains("evento")
        ) {
            return eventoId?.let(::EventoDetail)
        }

        // Sin tipo reconocible, inferir por las claves presentes (tolerancia a
        // payloads que omitan tipo): evento_id manda sobre el resto.
        if (eventoId != null) return EventoDetail(eventoId)
        if (reflexionId != null || slogan != null) {
            val identificador = slogan ?: reflexionId ?: return null
            return ReflexionDetail(slogan = identificador, reflexionId = reflexionId)
        }
        return null
    }

    /** Extrae el data de los extras de un Intent (notificación tocada o cold start). */
    fun parseIntent(intent: Intent?): PushNotificationDestination? {
        if (intent == null) return null
        val bundle = intent.extras ?: return null
        val data = bundle.keySet()
            .asSequence()
            .mapNotNull { key ->
                bundle.getString(key)?.let { key to it }
            }
            .toMap()
        if (data.isEmpty()) return null
        return parse(data)
    }

    /** Escribe el data del mensaje FCM en los extras de [intent]. */
    fun putData(intent: Intent, data: Map<String, String>): Intent {
        data.forEach { (key, value) -> intent.putExtra(key, value) }
        return intent
    }
}
