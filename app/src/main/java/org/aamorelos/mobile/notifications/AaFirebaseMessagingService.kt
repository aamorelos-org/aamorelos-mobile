package org.aamorelos.mobile.notifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.aamorelos.mobile.R

/**
 * Servicio FCM de la app.
 *
 * Recibe los mensajes de los tópicos `eventos` y `reflexiones`:
 * - Título/cuerpo: los que mande el servidor (bloque notification de FCM;
 *   en mensajes solo-data, las claves titulo/subtitulo del data).
 * - data: mapa {"tipo", "slogan", "evento_id"|"reflexion_id"} para navegar
 *   al detalle correspondiente al tocar la notificación.
 *
 * Con la app en segundo plano y mensaje con bloque notification, FCM muestra
 * la notificación automáticamente y no pasa por aquí (el data llega en los
 * extras del Intent al tocarla). Este servicio muestra la notificación en
 * primer plano y para mensajes solo-data en cualquier estado.
 */
class AaFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val data = remoteMessage.data
        val title = remoteMessage.notification?.title
            ?: data[PushNotificationParser.KEY_TITULO]
            ?: data["title"]
        val body = remoteMessage.notification?.body
            ?: data[PushNotificationParser.KEY_SUBTITULO]
            ?: data["body"]
            ?: data["mensaje"]

        Log.d(
            TAG,
            "Mensaje FCM recibido: tipo=${data[PushNotificationParser.KEY_TIPO]}, " +
                "titulo=${title ?: getString(R.string.notification_default_title)}"
        )

        PushNotificationHelper.showNotification(
            context = this,
            title = title,
            body = body,
            data = data
        )
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Los tópicos `eventos`/`reflexiones` se suscriben en MainActivity con
        // este token; no hace falta enviarlo a ningún servidor propio.
        Log.d(TAG, "Token FCM actualizado")
    }

    private companion object {
        const val TAG = "AaFirebaseMessagingService"
    }
}
