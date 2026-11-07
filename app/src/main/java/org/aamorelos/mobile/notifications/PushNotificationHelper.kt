package org.aamorelos.mobile.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.aamorelos.mobile.MainActivity
import org.aamorelos.mobile.R

/**
 * Canal y visualización de notificaciones push.
 *
 * Se usa tanto para mensajes solo-data (la app los recibe en
 * [AaFirebaseMessagingService.onMessageReceived] y debe mostrarlos a mano)
 * como para mensajes en primer plano con bloque notification (FCM no los
 * muestra automáticamente en primer plano).
 *
 * Cuando la app está en segundo plano y el mensaje trae bloque notification,
 * FCM ya muestra la notificación del sistema; al tocarla, los extras del
 * data llegan en el Intent de [MainActivity] y se navega de la misma forma
 * (ver [PushNotificationParser.parseIntent]).
 */
object PushNotificationHelper {

    const val CHANNEL_ID = "aamorelos_default"

    private const val TAG = "PushNotificationHelper"

    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE)
            as? NotificationManager ?: return
        if (manager.getNotificationChannel(CHANNEL_ID) != null) return
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = context.getString(R.string.notification_channel_description)
        }
        manager.createNotificationChannel(channel)
    }

    fun showNotification(
        context: Context,
        title: String?,
        body: String?,
        data: Map<String, String>
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "Sin permiso POST_NOTIFICATIONS, no se muestra la notificación")
            return
        }

        val safeTitle = title?.takeIf { it.isNotBlank() }
            ?: context.getString(R.string.notification_default_title)
        val safeBody = body?.takeIf { it.isNotBlank() }

        // Al tocar la notificación se abre MainActivity (singleTop) con el
        // mismo data del mensaje FCM en los extras; MainActivity lo convierte
        // en destino y MainNavigation navega al detalle.
        val contentIntent = buildContentIntent(context, data)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(safeTitle)
            .apply { safeBody?.let { setContentText(it) } }
            .setStyle(NotificationCompat.BigTextStyle().bigText(safeBody ?: safeTitle))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(contentIntent)
            .build()

        // ID estable por contenido para no apilar duplicados idénticos.
        val notificationId = data.hashCode()
        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }

    private fun buildContentIntent(context: Context, data: Map<String, String>): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_SINGLE_TOP
            PushNotificationParser.putData(this, data)
        }
        // RequestCode por destino para que dos notificaciones distintas no se pisen.
        val requestCode = data.hashCode()
        return PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
