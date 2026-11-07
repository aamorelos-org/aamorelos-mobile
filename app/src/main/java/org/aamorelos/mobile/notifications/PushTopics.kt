package org.aamorelos.mobile.notifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

/**
 * Tópicos FCM a los que la app se suscribe para recibir push.
 *
 * El servidor publica en:
 * - [TOPIC_EVENTOS] (tipo informativo/invitacion -> detalle de evento)
 * - [TOPIC_REFLEXIONES] (tipo reflexion -> detalle de reflexión)
 *
 * La suscripción persiste en el dispositivo; volver a llamar a [subscribe]
 * en cada arranque es seguro (FCM deduplica) y recupera la suscripción
 * si el token se regeneró o se reinstaló la app.
 */
object PushTopics {

    const val TOPIC_EVENTOS = "eventos"
    const val TOPIC_REFLEXIONES = "reflexiones"

    private const val TAG = "PushTopics"

    fun subscribe() {
        val messaging = try {
            FirebaseMessaging.getInstance()
        } catch (e: Exception) {
            Log.w(TAG, "FirebaseMessaging no disponible, no se suscriben tópicos", e)
            return
        }
        listOf(TOPIC_EVENTOS, TOPIC_REFLEXIONES).forEach { topic ->
            messaging.subscribeToTopic(topic)
                .addOnSuccessListener {
                    Log.d(TAG, "Suscripción al tópico '$topic' OK")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "No se pudo suscribir al tópico '$topic'", e)
                }
        }
    }
}
