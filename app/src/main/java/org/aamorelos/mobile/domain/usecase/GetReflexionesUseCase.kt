package org.aamorelos.mobile.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.aamorelos.mobile.domain.model.ReflexionModel
import org.aamorelos.mobile.domain.ports.ReflexionesRepositoryPort
import javax.inject.Inject

class GetReflexionesUseCase @Inject constructor(
    private val repository: ReflexionesRepositoryPort
) {
    operator fun invoke(): Flow<PagingData<ReflexionModel>> =
        repository.getReflexionesPager()
}
