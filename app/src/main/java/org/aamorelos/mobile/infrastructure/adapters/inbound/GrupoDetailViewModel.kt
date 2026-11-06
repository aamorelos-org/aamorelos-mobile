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
import org.aamorelos.mobile.domain.model.GrupoDetailModel
import org.aamorelos.mobile.domain.usecase.GetGrupoDetailUseCase
import org.aamorelos.mobile.ui.navigation.Screen
import javax.inject.Inject

/**
 * Adaptador de entrada — hexagonal (MVVM).
 * Lee el grupoId del navArguments vía SavedStateHandle y orquesta
 * el caso de uso de detalle.
 */
@HiltViewModel
class GrupoDetailViewModel @Inject constructor(
    private val getGrupoDetailUseCase: GetGrupoDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<ApiResult<GrupoDetailModel>>(ApiResult.Loading)
    val uiState: StateFlow<ApiResult<GrupoDetailModel>> = _uiState.asStateFlow()

    private val grupoId: String = savedStateHandle.get<String>(Screen.ARG_GRUPO_ID).orEmpty()
    private val estadoId: String = savedStateHandle.get<String>(Screen.ARG_ESTADO_ID) ?: "17"

    init {
        loadGrupoDetail()
    }

    fun loadGrupoDetail() {
        if (grupoId.isBlank()) {
            _uiState.value = ApiResult.Error("No se recibió el id del grupo")
            return
        }
        viewModelScope.launch {
            getGrupoDetailUseCase(grupoId = grupoId, estadoId = estadoId).collect { result ->
                _uiState.value = result
            }
        }
    }
}
