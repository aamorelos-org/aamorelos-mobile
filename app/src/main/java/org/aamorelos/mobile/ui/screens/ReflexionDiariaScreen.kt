package org.aamorelos.mobile.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.aamorelos.mobile.domain.ApiResult
import org.aamorelos.mobile.domain.model.ReflexionDiariaModel
import org.aamorelos.mobile.infrastructure.adapters.inbound.ReflexionDiariaViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ReflexionDiariaScreen(
    modifier: Modifier = Modifier,
    viewModel: ReflexionDiariaViewModel = hiltViewModel()
) {
    val apiResult by viewModel.uiState.collectAsState()
    val fecha by viewModel.fecha.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }

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
            ReflexionDiariaErrorState(
                message = result.message,
                fecha = fecha,
                onRetry = { viewModel.load() },
                onFechaClick = { showDatePicker = true },
                modifier = modifier
            )
        }
        is ApiResult.Success -> {
            ReflexionDiariaContent(
                reflexion = result.data,
                fecha = fecha,
                onFechaClick = { showDatePicker = true },
                modifier = modifier
            )
        }
    }

    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        val state = rememberDatePickerState(
            initialSelectedDateMillis = calendar.timeInMillis
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        state.selectedDateMillis?.let { millis ->
                            val nuevaFecha = SimpleDateFormat("MM-dd", Locale.US).format(millis)
                            viewModel.onFechaChange(nuevaFecha)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = state)
        }
    }
}

@Composable
private fun ReflexionDiariaContent(
    reflexion: ReflexionDiariaModel,
    fecha: String,
    onFechaClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Selector / indicador superior de fecha
        AssistChip(
            onClick = onFechaClick,
            label = {
                Text(
                    text = "REFLEXIÓN DIARIA · $fecha",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = MaterialTheme.typography.labelLarge.letterSpacing
                )
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
            },
            colors = AssistChipDefaults.assistChipColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                labelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                leadingIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )

        // Título principal
        Text(
            text = reflexion.titulo,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        // Párrafo de texto descriptivo
        if (!reflexion.texto.isNullOrBlank()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
                )
            ) {
                Text(
                    text = reflexion.texto,
                    modifier = Modifier.padding(20.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Referencia bibliográfica centrada
        if (!reflexion.contenido.isNullOrBlank()) {
            Text(
                text = reflexion.contenido,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }

        // Tarjeta inferior destacada para la cita con borde lateral distintivo
        if (!reflexion.cita.isNullOrBlank()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .width(6.dp)
                            .height(88.dp)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = reflexion.cita,
                        modifier = Modifier.padding(end = 16.dp, top = 16.dp, bottom = 16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun ReflexionDiariaErrorState(
    message: String,
    fecha: String,
    onRetry: () -> Unit,
    onFechaClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AssistChip(
            onClick = onFechaClick,
            label = {
                Text(
                    text = "REFLEXIÓN DIARIA · $fecha",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
            }
        )
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        Text(
            text = "Error al cargar la reflexión diaria",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}
