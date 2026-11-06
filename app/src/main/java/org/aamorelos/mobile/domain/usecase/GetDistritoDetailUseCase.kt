package org.aamorelos.mobile.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.aamorelos.mobile.domain.ApiResult
import org.aamorelos.mobile.domain.model.DistritoDetailModel
import org.aamorelos.mobile.domain.ports.DistritoDetailRepositoryPort
import javax.inject.Inject

class GetDistritoDetailUseCase @Inject constructor(
    private val repository: DistritoDetailRepositoryPort
) {
    operator fun invoke(
        distritoId: String
    ): Flow<ApiResult<DistritoDetailModel>> =
        repository.getDistritoDetail(distritoId = distritoId)
}
