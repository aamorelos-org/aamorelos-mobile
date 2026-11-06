package org.aamorelos.mobile.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import org.aamorelos.mobile.domain.model.GrupoModel
import org.aamorelos.mobile.infrastructure.adapters.inbound.DistritosViewModel
import org.aamorelos.mobile.infrastructure.adapters.inbound.GruposViewModel

private const val TODOS = "Todos"
private const val TODOS_ID = "__TODOS__"

@Composable
fun GruposScreen(
    modifier: Modifier = Modifier,
    onGrupoClick: (String) -> Unit = {},
    viewModel: GruposViewModel = hiltViewModel(),
    distritosViewModel: DistritosViewModel = hiltViewModel()
) {
    var query by rememberSaveable { mutableStateOf("") }
    var tipoSeleccionado by rememberSaveable { mutableStateOf(TODOS) }
    var distritoSeleccionadoId by rememberSaveable { mutableStateOf(TODOS_ID) }

    val apiResult by viewModel.uiState.collectAsState()
    val distritosResult by distritosViewModel.uiState.collectAsState()
    val distritos = (distritosResult as? ApiResult.Success)?.data ?: emptyList()

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
                onRetry = { viewModel.loadGrupos() },
                modifier = modifier
            )
        }
        is ApiResult.Success -> {
            GruposContent(
                grupos = result.data,
                distritos = distritos,
                distritosCargando = distritosResult is ApiResult.Loading,
                query = query,
                onQueryChange = { query = it },
                tipoSeleccionado = tipoSeleccionado,
                onTipoSelected = { tipoSeleccionado = it },
                distritoSeleccionadoId = distritoSeleccionadoId,
                onDistritoSelected = { distritoSeleccionadoId = it },
                onGrupoClick = onGrupoClick,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun GruposContent(
    grupos: List<GrupoModel>,
    distritos: List<DistritoModel>,
    distritosCargando: Boolean,
    query: String,
    onQueryChange: (String) -> Unit,
    tipoSeleccionado: String,
    onTipoSelected: (String) -> Unit,
    distritoSeleccionadoId: String,
    onDistritoSelected: (String) -> Unit,
    onGrupoClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val tiposDisponibles = remember(grupos) {
        listOf(TODOS) + grupos.map { it.tipo }.distinct().sorted()
    }

    // Distrito -> etiqueta legible ("(01) Primer Distrito • Morelos")
    val distritoLabelById = remember(distritos) {
        distritos.associate { it.id to distritoLabel(it) }
    }

    val distritosPresentes = remember(grupos) {
        grupos.map { it.distritoId }.toSet()
    }

    val distritosDisponibles: List<Pair<String, String>> = remember(
        distritos,
        distritosPresentes,
        grupos,
        distritosCargando
    ) {
        val base = if (distritos.isNotEmpty()) {
            // Solo distritos que efectivamente tienen grupos, ordenados por nombre
            distritos
                .filter { it.id in distritosPresentes }
                .sortedBy { it.nombre.lowercase() }
                .map { it.id to distritoLabel(it) }
        } else {
            // Fallback sin distritos: agrupar por el id crudo
            grupos.map { it.distritoId }.distinct().sorted().map { it to "Distrito $it" }
        }
        listOf(TODOS_ID to TODOS) + base
    }

    val gruposFiltrados = remember(query, tipoSeleccionado, distritoSeleccionadoId, grupos) {
        val texto = query.trim().lowercase()
        grupos.filter { grupo ->
            val coincideTipo = tipoSeleccionado == TODOS || grupo.tipo == tipoSeleccionado
            val coincideDistrito =
                distritoSeleccionadoId == TODOS_ID || grupo.distritoId == distritoSeleccionadoId
            val coincideTexto = texto.isEmpty() ||
                grupo.nombre.contains(texto, ignoreCase = true) ||
                grupo.municipio.contains(texto, ignoreCase = true) ||
                grupo.vecinos?.contains(texto, ignoreCase = true) == true
            coincideTipo && coincideDistrito && coincideTexto
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

        Text(
            text = "Tipo",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        FilterChipsRow(
            tipos = tiposDisponibles,
            tipoSeleccionado = tipoSeleccionado,
            onTipoSelected = onTipoSelected
        )

        DistritoFilterDropdown(
            opciones = distritosDisponibles,
            seleccionadoId = distritoSeleccionadoId,
            onSeleccionado = onDistritoSelected,
            cargando = distritosCargando,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Mostrando ${gruposFiltrados.size} de ${grupos.size} grupos",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (gruposFiltrados.isEmpty()) {
            EmptyState(modifier = Modifier.fillMaxWidth())
        } else {
            GruposLazyColumn(
                grupos = gruposFiltrados,
                distritoLabelById = distritoLabelById,
                onGrupoClick = onGrupoClick
            )
        }
    }
}

private fun distritoLabel(distrito: DistritoModel): String {
    val area = distrito.area.trim()
    return if (area.isNotBlank()) "${distrito.nombre} • $area" else distrito.nombre
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
        placeholder = { Text("Buscar grupo, municipio o colonia") },
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
    tipos: List<String>,
    tipoSeleccionado: String,
    onTipoSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tipos) { tipo ->
            FilterChip(
                selected = tipo == tipoSeleccionado,
                onClick = { onTipoSelected(tipo) },
                label = { Text(text = tipo) }
            )
        }
    }
}

@Composable
private fun DistritoFilterDropdown(
    opciones: List<Pair<String, String>>,
    seleccionadoId: String,
    onSeleccionado: (String) -> Unit,
    cargando: Boolean,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val textoSeleccionado = opciones.find { it.first == seleccionadoId }?.second
        ?: if (cargando) "Cargando distritos..." else TODOS

    Box(modifier = modifier) {
        OutlinedTextField(
            value = textoSeleccionado,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            label = { Text("Distrito") },
            placeholder = { Text(TODOS) },
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Seleccionar distrito"
                    )
                }
            },
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            opciones.forEach { (id, label) ->
                DropdownMenuItem(
                    text = { Text(text = label) },
                    onClick = {
                        onSeleccionado(id)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun GruposLazyColumn(
    grupos: List<GrupoModel>,
    distritoLabelById: Map<String, String>,
    onGrupoClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = grupos,
            key = { it.id }
        ) { grupo ->
            GrupoCard(
                grupo = grupo,
                distritoLabel = distritoLabelById[grupo.distritoId],
                onClick = { onGrupoClick(grupo.id) }
            )
        }
    }
}

@Composable
private fun GrupoCard(
    grupo: GrupoModel,
    onClick: () -> Unit,
    distritoLabel: String? = null,
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
                    text = grupo.nombre,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                AssistChip(
                    onClick = {},
                    label = { Text(text = grupo.tipo) },
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
                    text = if (grupo.vecinos.isNullOrBlank()) {
                        grupo.municipio
                    } else {
                        "${grupo.municipio} • ${grupo.vecinos}"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${grupo.genero} • ${grupo.lengua}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (!distritoLabel.isNullOrBlank()) {
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
                        text = "Distrito: $distritoLabel",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
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
            text = "No se encontraron grupos",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Prueba con otra búsqueda, tipo o distrito de grupo.",
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
            text = "Error al cargar grupos",
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
