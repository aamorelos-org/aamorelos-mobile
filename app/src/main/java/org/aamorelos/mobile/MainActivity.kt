package org.aamorelos.mobile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import org.aamorelos.mobile.notifications.PushNotificationDestination
import org.aamorelos.mobile.notifications.PushNotificationParser
import org.aamorelos.mobile.notifications.PushTopics
import org.aamorelos.mobile.ui.components.MainNavigation
import org.aamorelos.mobile.ui.theme.AAMorelosTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /** Destino pendiente derivado de una notificación tocada (cold o warm start). */
    private val _pendingDestination =
        MutableStateFlow<PushNotificationDestination?>(null)

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            // Si el usuario deniega, FCM seguirá recibiendo mensajes pero el
            // sistema no mostrará las notificaciones; no bloqueamos la app.
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Suscripción a los tópicos FCM "eventos" y "reflexiones".
        PushTopics.subscribe()
        requestNotificationPermissionIfNeeded()
        // Intent de lanzamiento: puede traer el data de una notificación tocada.
        handlePushIntent(intent)

        setContent {
            AAMorelosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val pendingDestination by _pendingDestination.collectAsState()
                    MainNavigation(
                        pendingDestination = pendingDestination,
                        onPendingDestinationConsumed = { _pendingDestination.value = null }
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // MainActivity es singleTop: tocar una notificación con la app abierta
        // reutiliza la actividad y llega por aquí.
        setIntent(intent)
        handlePushIntent(intent)
    }

    private fun handlePushIntent(intent: Intent?) {
        val destination = PushNotificationParser.parseIntent(intent) ?: return
        // Consumir los extras para no re-navegar al recrear la actividad.
        PushNotificationParser.DATA_KEYS.forEach { intent?.removeExtra(it) }
        _pendingDestination.value = destination
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}
