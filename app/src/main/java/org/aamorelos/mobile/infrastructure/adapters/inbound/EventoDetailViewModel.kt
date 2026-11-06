package org.aamorelos.mobile.infrastructure.adapters.inbound

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.aamorelos.mobile.domain.ApiResult
import org.aamorelos.mobile.domain.model.EventoDetailModel
import org.aamorelos.mobile.domain.usecase.GetEventoDetailUseCase
import org.aamorelos.mobile.ui.navigation.Screen
import javax.inject.Inject

/**
 * Adaptador de entrada — hexagonal (MVVM).
 * Lee el eventoId del navArguments vía SavedStateHandle y orquesta
 * el caso de uso de detalle.
 */
@HiltViewModel
class EventoDetailViewModel @Inject constructor(
    private val getEventoDetailUseCase: GetEventoDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<ApiResult<EventoDetailModel>>(ApiResult.Loading)
    val uiState: StateFlow<ApiResult<EventoDetailModel>> = _uiState.asStateFlow()

    private val eventoId: Int = savedStateHandle.get<Int>(Screen.ARG_EVENTO_ID) ?: -1

    init {
        loadEventoDetail()
    }

    fun loadEventoDetail() {
        if (eventoId <= 0) {
            _uiState.value = ApiResult.Error("No se recibió el id del evento")
            return
        }
        viewModelScope.launch {
            getEventoDetailUseCase(eventoId = eventoId).collect { result ->
                _uiState.value = result
            }
        }
    }
}
