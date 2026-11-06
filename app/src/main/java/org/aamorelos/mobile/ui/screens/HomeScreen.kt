package org.aamorelos.mobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import org.aamorelos.mobile.domain.model.EventoItemModel
import org.aamorelos.mobile.domain.model.ReflexionModel
import org.aamorelos.mobile.infrastructure.adapters.inbound.EventosViewModel
import org.aamorelos.mobile.infrastructure.adapters.inbound.ReflexionesViewModel
import org.aamorelos.mobile.ui.components.EventoCard
import org.aamorelos.mobile.ui.components.ReflexionCard

private const val HOME_EVENTOS_LIMIT = 5
private const val HOME_REFLEXIONES_LIMIT = 5

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onVerMasClick: () -> Unit = {},
    onEventoClick: (Int) -> Unit = {},
    onVerMasReflexionesClick: () -> Unit = {},
    onReflexionClick: (String) -> Unit = {},
    viewModel: EventosViewModel = hiltViewModel(),
    reflexionesViewModel: ReflexionesViewModel = hiltViewModel()
) {
    val eventos: LazyPagingItems<EventoItemModel> =
        viewModel.eventos.collectAsLazyPagingItems()
    val reflexiones: LazyPagingItems<ReflexionModel> =
        reflexionesViewModel.reflexiones.collectAsLazyPagingItems()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // Sección modular: carrusel de eventos (primeros 5)
        item(key = "eventos") {
            EventosCarouselSection(
                eventos = eventos,
                onVerMasClick = onVerMasClick,
                onEventoClick = onEventoClick
            )
        }

        // Sección modular: carrusel de reflexiones (primeros 5)
        item(key = "reflexiones") {
            ReflexionesCarouselSection(
                reflexiones = reflexiones,
                onVerMasClick = onVerMasReflexionesClick,
                onReflexionClick = onReflexionClick
            )
        }
    }
}

@Composable
private fun EventosCarouselSection(
    eventos: LazyPagingItems<EventoItemModel>,
    onVerMasClick: () -> Unit,
    onEventoClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Eventos",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            TextButton(onClick = onVerMasClick) {
                Text(
                    text = "Ver Más",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        when {
            eventos.loadState.refresh is LoadState.Loading && eventos.itemCount == 0 -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            eventos.loadState.refresh is LoadState.Error && eventos.itemCount == 0 -> {
                val error = (eventos.loadState.refresh as LoadState.Error).error
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = error.message ?: "Error al cargar eventos",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    Button(onClick = { eventos.retry() }) {
                        Text("Reintentar")
                    }
                }
            }
            eventos.itemCount == 0 && eventos.loadState.refresh is LoadState.NotLoading -> {
                Text(
                    text = "No se encontraron eventos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
            else -> {
                val count = if (eventos.itemCount < HOME_EVENTOS_LIMIT) eventos.itemCount else HOME_EVENTOS_LIMIT
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        count = count,
                        key = { index -> eventos[index]?.id ?: index }
                    ) { index ->
                        eventos[index]?.let { evento ->
                            EventoCard(
                                evento = evento,
                                onClick = { onEventoClick(evento.id) },
                                modifier = Modifier.width(280.dp)
                            )
                        }
                    }
                    if (eventos.loadState.append is LoadState.Loading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .width(80.dp)
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReflexionesCarouselSection(
    reflexiones: LazyPagingItems<ReflexionModel>,
    onVerMasClick: () -> Unit,
    onReflexionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Reflexiones",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            TextButton(onClick = onVerMasClick) {
                Text(
                    text = "Ver Más",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        when {
            reflexiones.loadState.refresh is LoadState.Loading && reflexiones.itemCount == 0 -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            reflexiones.loadState.refresh is LoadState.Error && reflexiones.itemCount == 0 -> {
                val error = (reflexiones.loadState.refresh as LoadState.Error).error
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = error.message ?: "Error al cargar reflexiones",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    Button(onClick = { reflexiones.retry() }) {
                        Text("Reintentar")
                    }
                }
            }
            reflexiones.itemCount == 0 && reflexiones.loadState.refresh is LoadState.NotLoading -> {
                Text(
                    text = "No se encontraron reflexiones",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
            else -> {
                val count = if (reflexiones.itemCount < HOME_REFLEXIONES_LIMIT) reflexiones.itemCount else HOME_REFLEXIONES_LIMIT
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        count = count,
                        key = { index -> reflexiones[index]?.id ?: index }
                    ) { index ->
                        reflexiones[index]?.let { reflexion ->
                            ReflexionCard(
                                reflexion = reflexion,
                                onClick = { onReflexionClick(reflexion.slogan) },
                                modifier = Modifier.width(280.dp)
                            )
                        }
                    }
                    if (reflexiones.loadState.append is LoadState.Loading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .width(80.dp)
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}
