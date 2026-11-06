package org.aamorelos.mobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import org.aamorelos.mobile.domain.model.EventoItemModel
import org.aamorelos.mobile.infrastructure.adapters.inbound.EventosViewModel
import org.aamorelos.mobile.ui.components.EventoCard

@Composable
fun EventosScreen(
    modifier: Modifier = Modifier,
    onEventoClick: (Int) -> Unit = {},
    viewModel: EventosViewModel = hiltViewModel()
) {
    val eventos: LazyPagingItems<EventoItemModel> =
        viewModel.eventos.collectAsLazyPagingItems()

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        when {
            eventos.loadState.refresh is LoadState.Loading && eventos.itemCount == 0 -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            eventos.loadState.refresh is LoadState.Error && eventos.itemCount == 0 -> {
                val error = (eventos.loadState.refresh as LoadState.Error).error
                EventosErrorState(
                    message = error.message ?: "Error al cargar eventos",
                    onRetry = { eventos.retry() },
                    modifier = Modifier.fillMaxSize()
                )
            }
            eventos.itemCount == 0 && eventos.loadState.refresh is LoadState.NotLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No se encontraron eventos",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Intenta recargar la lista.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        count = eventos.itemCount,
                        key = eventos.itemKey { it.id }
                    ) { index ->
                        eventos[index]?.let { evento ->
                            EventoCard(
                                evento = evento,
                                onClick = { onEventoClick(evento.id) }
                            )
                        }
                    }

                    if (eventos.loadState.append is LoadState.Loading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    if (eventos.loadState.append is LoadState.Error) {
                        val error = (eventos.loadState.append as LoadState.Error).error
                        item {
                            EventosErrorState(
                                message = error.message ?: "Error al cargar más eventos",
                                onRetry = { eventos.retry() },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EventosErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error al cargar eventos",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}
