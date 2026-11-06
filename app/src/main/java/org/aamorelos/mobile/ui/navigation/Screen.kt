package org.aamorelos.mobile.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.People
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    HOME("home", "Inicio", Icons.Default.Home),
    GRUPOS("grupos", "Grupos", Icons.Default.People),
    EVENTOS("eventos", "Eventos", Icons.Default.Event),
    REFLEXION_DIARIA("reflexion-diaria", "Diaria", Icons.Default.Description),
    INFORMACION("informacion", "Información", Icons.Default.Info);

    companion object {
        const val ARG_GRUPO_ID = "grupoId"
        const val ARG_DISTRITO_ID = "distritoId"
        const val ARG_REFLEXION_SLOGAN = "reflexionSlogan"
        const val ARG_EVENTO_ID = "eventoId"
        const val ARG_ESTADO_ID = "estadoId"
        const val INFORMACION_ROUTE = "informacion"
        const val DISTRITOS_ROUTE = "distritos"
        const val DIRECTORIO_ROUTE = "directorio"
        const val REFLEXIONES_ROUTE = "reflexiones"
        const val REFLEXION_DIARIA_ROUTE = "reflexion-diaria"
        const val GRUPO_DETAIL_ROUTE = "grupo_detalle/{$ARG_GRUPO_ID}"
        const val DISTRITO_DETAIL_ROUTE = "distrito_detalle/{$ARG_DISTRITO_ID}"
        const val REFLEXION_DETAIL_ROUTE = "reflexion_detalle/{$ARG_REFLEXION_SLOGAN}"
        const val EVENTO_DETAIL_ROUTE = "evento_detalle/{$ARG_EVENTO_ID}"

        fun grupoDetailRoute(grupoId: String, estadoId: String = "17"): String =
            "grupo_detalle/$grupoId?estadoId=$estadoId"

        fun distritoDetailRoute(distritoId: String): String =
            "distrito_detalle/$distritoId"

        fun reflexionDetailRoute(slogan: String): String =
            "reflexion_detalle/$slogan"

        fun eventoDetailRoute(eventoId: Int): String =
            "evento_detalle/$eventoId"

        fun isGrupoDetailRoute(route: String?): Boolean =
            route?.startsWith("grupo_detalle") == true

        fun isDistritoDetailRoute(route: String?): Boolean =
            route?.startsWith("distrito_detalle") == true

        fun isReflexionDetailRoute(route: String?): Boolean =
            route?.startsWith("reflexion_detalle") == true

        fun isEventoDetailRoute(route: String?): Boolean =
            route?.startsWith("evento_detalle") == true
    }
}
