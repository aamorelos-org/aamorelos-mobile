package org.aamorelos.mobile.notifications

import org.aamorelos.mobile.ui.navigation.Screen

/**
 * Destino de navegación derivado de una notificación push de FCM.
 *
 * Se recibe vía FCM en dos tópicos:
 * - `eventos`: data {"tipo": "informativo"|"invitacion", "slogan": "...", "evento_id": "1"}
 *   -> detalle de evento con ese evento_id.
 * - `reflexiones`: data {"tipo": "reflexion", "slogan": "...", "reflexion_id": "..."}
 *   -> detalle de reflexión equivalente a ese reflexion_id.
 */
sealed interface PushNotificationDestination {

    /** Ruta de navegación equivalente (evento_detalle/{id} o reflexion_detalle/{slogan}). */
    val route: String

    /** Detalle de evento: la API es GET eventos/{id}. */
    data class EventoDetail(val eventoId: Int) : PushNotificationDestination {
        override val route: String get() = Screen.eventoDetailRoute(eventoId)
    }

    /**
     * Detalle de reflexión.
     *
     * La API y la ruta actual son por slogan (GET reflexiones/{slogan}), por lo que
     * [slogan] es el argumento de navegación. [reflexionId] se conserva tal como llegó
     * en el data; si el servidor manda reflexion_id como identificador (slogan) y no
     * manda slogan, el parser lo usa como [slogan].
     */
    data class ReflexionDetail(
        val slogan: String,
        val reflexionId: String? = null
    ) : PushNotificationDestination {
        override val route: String get() = Screen.reflexionDetailRoute(slogan)
    }
}
