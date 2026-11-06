package org.aamorelos.mobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.aamorelos.mobile.domain.ApiResult
import org.aamorelos.mobile.domain.model.DistritoModel
import org.aamorelos.mobile.infrastructure.adapters.inbound.DistritosViewModel

private const val TODOS = "Todos"

@Composable
fun DistritosScreen(
    modifier: Modifier = Modifier,
    onDistritoClick: (String) -> Unit = {},
    viewModel: DistritosViewModel = hiltViewModel()
) {
    var query by rememberSaveable { mutableStateOf("") }
    var areaSeleccionada by rememberSaveable { mutableStateOf(TODOS) }

    val apiResult by viewModel.uiState.collectAsState()

    when (val result = apiResult) {
        is ApiResult.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is ApiResult.Error -> {
            ErrorState(
                message = result.message,
                onRetry = { viewModel.loadDistritos() },
                modifier = modifier
            )
        }
        is ApiResult.Success -> {
            DistritosContent(
                distritos = result.data,
                query = query,
                onQueryChange = { query = it },
                areaSeleccionada = areaSeleccionada,
                onAreaSelected = { areaSeleccionada = it },
                onDistritoClick = onDistritoClick,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun DistritosContent(
    distritos: List<DistritoModel>,
    query: String,
    onQueryChange: (String) -> Unit,
    areaSeleccionada: String,
    onAreaSelected: (String) -> Unit,
    onDistritoClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val areasDisponibles = remember(distritos) {
        listOf(TODOS) + distritos.map { it.area }.distinct().sorted()
    }

    val distritosFiltrados = remember(query, areaSeleccionada, distritos) {
        val texto = query.trim().lowercase()
        distritos.filter { distrito ->
            val coincideArea = areaSeleccionada == TODOS || distrito.area == areaSeleccionada
            val coincideTexto = texto.isEmpty() ||
                distrito.nombre.contains(texto, ignoreCase = true) ||
                distrito.municipio.contains(texto, ignoreCase = true) ||
                distrito.vecinos?.contains(texto, ignoreCase = true) == true
            coincideArea && coincideTexto
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SearchBar(
            query = query,
            onQueryChange = onQueryChange
        )

        FilterChipsRow(
            areas = areasDisponibles,
            areaSeleccionada = areaSeleccionada,
            onAreaSelected = onAreaSelected
        )

        Text(
            text = "Mostrando ${distritosFiltrados.size} de ${distritos.size} distritos",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (distritosFiltrados.isEmpty()) {
            EmptyState(modifier = Modifier.fillMaxWidth())
        } else {
            DistritosLazyColumn(distritos = distritosFiltrados, onDistritoClick = onDistritoClick)
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text("Buscar distrito, municipio o colonia") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar"
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Limpiar búsqueda"
                    )
                }
            }
        },
        singleLine = true,
        shape = MaterialTheme.shapes.medium
    )
}

@Composable
private fun FilterChipsRow(
    areas: List<String>,
    areaSeleccionada: String,
    onAreaSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(areas) { area ->
            FilterChip(
                selected = area == areaSeleccionada,
                onClick = { onAreaSelected(area) },
                label = { Text(text = area) }
            )
        }
    }
}

@Composable
private fun DistritosLazyColumn(
    distritos: List<DistritoModel>,
    onDistritoClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = distritos,
            key = { it.id }
        ) { distrito ->
            DistritoCard(distrito = distrito, onClick = { onDistritoClick(distrito.id) })
        }
    }
}

@Composable
private fun DistritoCard(
    distrito: DistritoModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = distrito.nombre,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                AssistChip(
                    onClick = {},
                    label = { Text(text = distrito.area) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (distrito.vecinos.isNullOrBlank()) {
                        distrito.municipio
                    } else {
                        "${distrito.municipio} • ${distrito.vecinos}"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No se encontraron distritos",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Prueba con otra búsqueda o área.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error al cargar distritos",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}
