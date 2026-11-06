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
import org.aamorelos.mobile.domain.model.ReflexionDetailModel
import org.aamorelos.mobile.domain.usecase.GetReflexionDetailUseCase
import org.aamorelos.mobile.ui.navigation.Screen
import javax.inject.Inject

/**
 * Adaptador de entrada — hexagonal (MVVM).
 * Lee el slogan del navArguments vía SavedStateHandle y orquesta
 * el caso de uso de detalle.
 */
@HiltViewModel
class ReflexionDetailViewModel @Inject constructor(
    private val getReflexionDetailUseCase: GetReflexionDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<ApiResult<ReflexionDetailModel>>(ApiResult.Loading)
    val uiState: StateFlow<ApiResult<ReflexionDetailModel>> = _uiState.asStateFlow()

    private val slogan: String = savedStateHandle.get<String>(Screen.ARG_REFLEXION_SLOGAN).orEmpty()

    init {
        loadReflexionDetail()
    }

    fun loadReflexionDetail() {
        if (slogan.isBlank()) {
            _uiState.value = ApiResult.Error("No se recibió el slogan de la reflexión")
            return
        }
        viewModelScope.launch {
            getReflexionDetailUseCase(slogan = slogan).collect { result ->
                _uiState.value = result
            }
        }
    }
}
