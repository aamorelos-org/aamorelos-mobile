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
import org.aamorelos.mobile.domain.model.DistritoDetailModel
import org.aamorelos.mobile.domain.usecase.GetDistritoDetailUseCase
import org.aamorelos.mobile.ui.navigation.Screen
import javax.inject.Inject

/**
 * Adaptador de entrada — hexagonal (MVVM).
 * Lee el distritoId del navArguments vía SavedStateHandle y orquesta
 * el caso de uso de detalle.
 */
@HiltViewModel
class DistritoDetailViewModel @Inject constructor(
    private val getDistritoDetailUseCase: GetDistritoDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<ApiResult<DistritoDetailModel>>(ApiResult.Loading)
    val uiState: StateFlow<ApiResult<DistritoDetailModel>> = _uiState.asStateFlow()

    private val distritoId: String = savedStateHandle.get<String>(Screen.ARG_DISTRITO_ID).orEmpty()

    init {
        loadDistritoDetail()
    }

    fun loadDistritoDetail() {
        if (distritoId.isBlank()) {
            _uiState.value = ApiResult.Error("No se recibió el id del distrito")
            return
        }
        viewModelScope.launch {
            getDistritoDetailUseCase(distritoId = distritoId).collect { result ->
                _uiState.value = result
            }
        }
    }
}
