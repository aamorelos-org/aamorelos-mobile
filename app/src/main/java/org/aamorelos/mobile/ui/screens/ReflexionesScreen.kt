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
import org.aamorelos.mobile.domain.model.ReflexionModel
import org.aamorelos.mobile.infrastructure.adapters.inbound.ReflexionesViewModel
import org.aamorelos.mobile.ui.components.ReflexionCard

@Composable
fun ReflexionesScreen(
    modifier: Modifier = Modifier,
    onReflexionClick: (String) -> Unit = {},
    viewModel: ReflexionesViewModel = hiltViewModel()
) {
    val reflexiones: LazyPagingItems<ReflexionModel> =
        viewModel.reflexiones.collectAsLazyPagingItems()

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        when {
            reflexiones.loadState.refresh is LoadState.Loading && reflexiones.itemCount == 0 -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            reflexiones.loadState.refresh is LoadState.Error && reflexiones.itemCount == 0 -> {
                val error = (reflexiones.loadState.refresh as LoadState.Error).error
                ReflexionesErrorState(
                    message = error.message ?: "Error al cargar reflexiones",
                    onRetry = { reflexiones.retry() },
                    modifier = Modifier.fillMaxSize()
                )
            }
            reflexiones.itemCount == 0 && reflexiones.loadState.refresh is LoadState.NotLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No se encontraron reflexiones",
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
                        count = reflexiones.itemCount,
                        key = reflexiones.itemKey { it.id }
                    ) { index ->
                        reflexiones[index]?.let { reflexion ->
                            ReflexionCard(
                                reflexion = reflexion,
                                onClick = { onReflexionClick(reflexion.slogan) }
                            )
                        }
                    }

                    // Indicator de carga al final (append)
                    if (reflexiones.loadState.append is LoadState.Loading) {
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
                    if (reflexiones.loadState.append is LoadState.Error) {
                        val error = (reflexiones.loadState.append as LoadState.Error).error
                        item {
                            ReflexionesErrorState(
                                message = error.message ?: "Error al cargar más reflexiones",
                                onRetry = { reflexiones.retry() },
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
private fun ReflexionesErrorState(
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
            text = "Error al cargar reflexiones",
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
