package org.aamorelos.mobile.infrastructure.adapters.inbound

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import org.aamorelos.mobile.domain.model.ReflexionModel
import org.aamorelos.mobile.domain.usecase.GetReflexionesUseCase
import javax.inject.Inject

/**
 * Adaptador de entrada — hexagonal (MVVM).
 * Expone el flujo paginado cacheado en el scope del ViewModel.
 */
@HiltViewModel
class ReflexionesViewModel @Inject constructor(
    getReflexionesUseCase: GetReflexionesUseCase
) : ViewModel() {

    val reflexiones: Flow<PagingData<ReflexionModel>> =
        getReflexionesUseCase().cachedIn(viewModelScope)
}
