package org.aamorelos.mobile.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import org.aamorelos.mobile.ui.navigation.Screen
import org.aamorelos.mobile.ui.screens.EventosScreen
import org.aamorelos.mobile.ui.screens.AboutScreen
import org.aamorelos.mobile.ui.screens.EventoDetailScreen
import org.aamorelos.mobile.ui.screens.GrupoDetailScreen
import org.aamorelos.mobile.ui.screens.GruposScreen
import org.aamorelos.mobile.ui.screens.DistritoDetailScreen
import org.aamorelos.mobile.ui.screens.DistritosScreen
import org.aamorelos.mobile.ui.screens.HomeScreen
import org.aamorelos.mobile.ui.screens.ReflexionesScreen
import org.aamorelos.mobile.ui.screens.ReflexionDetailScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val isGrupoDetail = Screen.isGrupoDetailRoute(currentRoute)
    val isDistritoDetail = Screen.isDistritoDetailRoute(currentRoute)
    val isReflexionDetail = Screen.isReflexionDetailRoute(currentRoute)
    val isEventoDetail = Screen.isEventoDetailRoute(currentRoute)
    val isDetail = isGrupoDetail || isDistritoDetail || isReflexionDetail || isEventoDetail
    val currentScreen: Screen = currentRoute?.let { route ->
        Screen.entries.firstOrNull { it.route == route }
    } ?: Screen.HOME
    val topBarTitle = when {
        isGrupoDetail -> "Detalle del grupo"
        isDistritoDetail -> "Detalle del distrito"
        isReflexionDetail -> "Detalle de la reflexión"
        isEventoDetail -> "Detalle del evento"
        else -> "Alcohólicos Anónimos"
    }
    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                onItemClick = {
                    scope.launch { drawerState.close() }
                },
                onInformacionClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Screen.INFORMACION_ROUTE) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onReflexionesClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Screen.REFLEXIONES.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            topBar = {
                TopAppBar(
                    title = {
                        if (!isDetail) {
                            androidx.compose.foundation.layout.Column {
                                Text(
                                    text = "Alcohólicos Anónimos",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "6to Distrito - Área Morelos - 2026",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.7f)
                                )
                            }
                        } else {
                            Text(
                                text = topBarTitle,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = androidx.compose.ui.graphics.Color(0xFF1D4E89),
                        titleContentColor = androidx.compose.ui.graphics.Color.White,
                        navigationIconContentColor = androidx.compose.ui.graphics.Color.White,
                        actionIconContentColor = androidx.compose.ui.graphics.Color.White
                    ),
                    navigationIcon = {
                        if (isDetail) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Volver"
                                )
                            }
                        }
                    },
                    actions = {
                        if (!isDetail) {
                            IconButton(onClick = { openDial(context) }) {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = "Llamar por teléfono"
                                )
                            }
                            IconButton(onClick = { openEmail(context) }) {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = "Enviar correo"
                                )
                            }
                            IconButton(
                                onClick = { scope.launch { drawerState.open() } }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Abrir menú"
                                )
                            }
                        }
                    }
                )
            },
            bottomBar = {
                if (!isDetail) {
                    NavigationBar(
                        containerColor = androidx.compose.ui.graphics.Color(0xFF1D4E89),
                        contentColor = androidx.compose.ui.graphics.Color.White
                    ) {
                        Screen.entries.forEach { screen ->
                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        imageVector = screen.icon,
                                        contentDescription = screen.label
                                    )
                                },
                                label = {
                                    Text(text = screen.label)
                                },
                                colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                                    selectedIconColor = androidx.compose.ui.graphics.Color.White,
                                    selectedTextColor = androidx.compose.ui.graphics.Color.White,
                                    unselectedIconColor = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.6f),
                                    unselectedTextColor = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.6f),
                                    indicatorColor = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.15f)
                                ),
                                selected = currentScreen == screen,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = Screen.HOME.route
            ) {
                composable(Screen.HOME.route) {
                    HomeScreen(
                        modifier = Modifier.padding(padding),
                        onVerMasClick = {
                            navController.navigate(Screen.EVENTOS.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onEventoClick = { eventoId ->
                            navController.navigate(Screen.eventoDetailRoute(eventoId))
                        },
                        onVerMasReflexionesClick = {
                            navController.navigate(Screen.REFLEXIONES.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onReflexionClick = { slogan ->
                            navController.navigate(Screen.reflexionDetailRoute(slogan))
                        }
                    )
                }
                composable(Screen.GRUPOS.route) {
                    GruposScreen(
                        modifier = Modifier.padding(padding),
                        onGrupoClick = { grupoId ->
                            navController.navigate(Screen.grupoDetailRoute(grupoId))
                        }
                    )
                }
                composable(Screen.EVENTOS.route) {
                    EventosScreen(
                        modifier = Modifier.padding(padding),
                        onEventoClick = { eventoId ->
                            navController.navigate(Screen.eventoDetailRoute(eventoId))
                        }
                    )
                }
                composable(Screen.INFORMACION.route) {
                    AboutScreen(modifier = Modifier.padding(padding))
                }
                composable(Screen.DISTRITOS_ROUTE) {
                    DistritosScreen(
                        modifier = Modifier.padding(padding),
                        onDistritoClick = { distritoId ->
                            navController.navigate(Screen.distritoDetailRoute(distritoId))
                        }
                    )
                }
                composable(
                    route = "${Screen.GRUPO_DETAIL_ROUTE}?estadoId={${Screen.ARG_ESTADO_ID}}",
                    arguments = listOf(
                        navArgument(Screen.ARG_GRUPO_ID) { type = NavType.StringType },
                        navArgument(Screen.ARG_ESTADO_ID) {
                            type = NavType.StringType
                            defaultValue = "17"
                        }
                    )
                ) {
                    GrupoDetailScreen(modifier = Modifier.padding(padding))
                }
                composable(
                    route = Screen.DISTRITO_DETAIL_ROUTE,
                    arguments = listOf(
                        navArgument(Screen.ARG_DISTRITO_ID) { type = NavType.StringType }
                    )
                ) {
                    DistritoDetailScreen(modifier = Modifier.padding(padding))
                }
                composable(Screen.REFLEXIONES.route) {
                    ReflexionesScreen(
                        modifier = Modifier.padding(padding),
                        onReflexionClick = { slogan ->
                            navController.navigate(Screen.reflexionDetailRoute(slogan))
                        }
                    )
                }
                composable(
                    route = Screen.REFLEXION_DETAIL_ROUTE,
                    arguments = listOf(
                        navArgument(Screen.ARG_REFLEXION_SLOGAN) { type = NavType.StringType }
                    )
                ) {
                    ReflexionDetailScreen(
                        modifier = Modifier.padding(padding),
                        onBack = { navController.popBackStack() }
                    )
                }
                composable(
                    route = Screen.EVENTO_DETAIL_ROUTE,
                    arguments = listOf(
                        navArgument(Screen.ARG_EVENTO_ID) { type = NavType.IntType }
                    )
                ) {
                    EventoDetailScreen(
                        modifier = Modifier.padding(padding),
                        onBack = { navController.popBackStack() }
                    )
                }
                composable(Screen.INFORMACION_ROUTE) {
                    AboutScreen(modifier = Modifier.padding(padding))
                }
            }
        }
    }
}
