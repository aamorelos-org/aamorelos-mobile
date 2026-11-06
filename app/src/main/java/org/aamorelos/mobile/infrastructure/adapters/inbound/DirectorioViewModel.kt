package org.aamorelos.mobile.infrastructure.adapters.inbound

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import org.aamorelos.mobile.domain.model.DirectorioModel
import org.aamorelos.mobile.domain.usecase.GetDirectorioUseCase
import javax.inject.Inject

/**
 * Adaptador de entrada — hexagonal (MVVM).
 * Expone el flujo paginado cacheado en el scope del ViewModel.
 */
@HiltViewModel
class DirectorioViewModel @Inject constructor(
    getDirectorioUseCase: GetDirectorioUseCase
) : ViewModel() {

    val directorio: Flow<PagingData<DirectorioModel>> =
        getDirectorioUseCase().cachedIn(viewModelScope)
}
