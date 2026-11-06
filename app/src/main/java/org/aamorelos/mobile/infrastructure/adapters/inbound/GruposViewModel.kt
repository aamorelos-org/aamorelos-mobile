package org.aamorelos.mobile.infrastructure.adapters.inbound

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.aamorelos.mobile.domain.ApiResult
import org.aamorelos.mobile.domain.model.GrupoModel
import org.aamorelos.mobile.domain.usecase.GetGruposUseCase
import javax.inject.Inject

/**
 * Adaptador de entrada — hexagonal (MVVM).
 * La UI (ej. Compose) es un cliente del puerto de entrada representado
 * por este ViewModel. Orquesta el caso de uso.
 */
@HiltViewModel
class GruposViewModel @Inject constructor(
    private val getGruposUseCase: GetGruposUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ApiResult<List<GrupoModel>>>(
        ApiResult.Loading
    )
    val uiState: StateFlow<ApiResult<List<GrupoModel>>> = _uiState.asStateFlow()

    init {
        loadGrupos()
    }

    fun loadGrupos(estadoId: String = "17") {
        viewModelScope.launch {
            getGruposUseCase(estadoId).collect { result ->
                _uiState.value = result
            }
        }
    }
}
