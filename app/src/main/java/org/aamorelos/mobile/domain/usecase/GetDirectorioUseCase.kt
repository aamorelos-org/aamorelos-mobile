package org.aamorelos.mobile.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.aamorelos.mobile.domain.model.DirectorioModel
import org.aamorelos.mobile.domain.ports.DirectorioRepositoryPort
import javax.inject.Inject

class GetDirectorioUseCase @Inject constructor(
    private val repository: DirectorioRepositoryPort
) {
    operator fun invoke(): Flow<PagingData<DirectorioModel>> =
        repository.getDirectorioPager()
}
