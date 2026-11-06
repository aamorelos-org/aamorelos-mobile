package org.aamorelos.mobile.infrastructure.adapters.inbound

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.aamorelos.mobile.domain.ApiResult
import org.aamorelos.mobile.domain.model.DistritoModel
import org.aamorelos.mobile.domain.usecase.GetDistritosUseCase
import javax.inject.Inject

/**
 * Adaptador de entrada — hexagonal (MVVM).
 * La UI (ej. Compose) es un cliente del puerto de entrada representado
 * por este ViewModel. Orquesta el caso de uso.
 */
@HiltViewModel
class DistritosViewModel @Inject constructor(
    private val getDistritosUseCase: GetDistritosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ApiResult<List<DistritoModel>>>(
        ApiResult.Loading
    )
    val uiState: StateFlow<ApiResult<List<DistritoModel>>> = _uiState.asStateFlow()

    init {
        loadDistritos()
    }

    fun loadDistritos(estadoId: String = "17") {
        viewModelScope.launch {
            getDistritosUseCase(estadoId).collect { result ->
                _uiState.value = result
            }
        }
    }
}
