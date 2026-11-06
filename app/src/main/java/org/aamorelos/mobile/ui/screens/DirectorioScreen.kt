package org.aamorelos.mobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import org.aamorelos.mobile.domain.model.DirectorioModel
import org.aamorelos.mobile.infrastructure.adapters.inbound.DirectorioViewModel
import org.aamorelos.mobile.ui.components.DirectorioCard

@Composable
fun DirectorioScreen(
    modifier: Modifier = Modifier,
    viewModel: DirectorioViewModel = hiltViewModel()
) {
    val directorio: LazyPagingItems<DirectorioModel> =
        viewModel.directorio.collectAsLazyPagingItems()

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        when {
            directorio.loadState.refresh is LoadState.Loading && directorio.itemCount == 0 -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            directorio.loadState.refresh is LoadState.Error && directorio.itemCount == 0 -> {
                val error = (directorio.loadState.refresh as LoadState.Error).error
                DirectorioErrorState(
                    message = error.message ?: "Error al cargar directorio",
                    onRetry = { directorio.retry() },
                    modifier = Modifier.fillMaxSize()
                )
            }
            directorio.itemCount == 0 && directorio.loadState.refresh is LoadState.NotLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No se encontró el directorio",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Intenta recargar la galería.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        count = directorio.itemCount,
                        key = directorio.itemKey { it.id }
                    ) { index ->
                        directorio[index]?.let { item ->
                            DirectorioCard(directorio = item)
                        }
                    }

                    if (directorio.loadState.append is LoadState.Loading) {
                        item(span = { GridItemSpan(2) }) {
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
                    if (directorio.loadState.append is LoadState.Error) {
                        val error = (directorio.loadState.append as LoadState.Error).error
                        item(span = { GridItemSpan(2) }) {
                            DirectorioErrorState(
                                message = error.message ?: "Error al cargar más páginas",
                                onRetry = { directorio.retry() },
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
private fun DirectorioErrorState(
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
            text = "Error al cargar directorio",
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
