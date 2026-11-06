package org.aamorelos.mobile.infrastructure.adapters.inbound

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.aamorelos.mobile.domain.ApiResult
import org.aamorelos.mobile.domain.model.ReflexionDiariaModel
import org.aamorelos.mobile.domain.usecase.GetReflexionDiariaUseCase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * Adaptador de entrada — hexagonal (MVVM).
 * La fecha ("MM-DD") se gestiona aquí: por defecto es hoy, y la UI
 * puede cambiarla con el selector de fecha (DatePicker) vía [onFechaChange].
 */
@HiltViewModel
class ReflexionDiariaViewModel @Inject constructor(
    private val getReflexionDiariaUseCase: GetReflexionDiariaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ApiResult<ReflexionDiariaModel>>(ApiResult.Loading)
    val uiState: StateFlow<ApiResult<ReflexionDiariaModel>> = _uiState.asStateFlow()

    private val _fecha = MutableStateFlow(todayFecha())
    val fecha: StateFlow<String> = _fecha.asStateFlow()

    init {
        load()
    }

    fun load() {
        val fecha = _fecha.value
        _uiState.value = ApiResult.Loading
        viewModelScope.launch {
            getReflexionDiariaUseCase(fecha = fecha).collect { result ->
                _uiState.value = result
            }
        }
    }

    fun onFechaChange(fecha: String) {
        if (fecha.isBlank()) return
        _fecha.value = fecha
        load()
    }

    companion object {
        fun todayFecha(): String =
            SimpleDateFormat("MM-dd", Locale.US).format(Date())
    }
}
