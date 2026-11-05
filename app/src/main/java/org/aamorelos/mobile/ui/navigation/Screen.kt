package org.aamorelos.mobile.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    HOME("home", "Inicio", Icons.Default.Home),
    GRUPOS("grupos", "Grupos", Icons.Default.People),
    EVENTOS("eventos", "Eventos", Icons.Default.Event);
}
