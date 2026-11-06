package org.aamorelos.mobile.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.aamorelos.mobile.domain.ApiResult
import org.aamorelos.mobile.domain.model.ReflexionDetailModel
import org.aamorelos.mobile.domain.ports.ReflexionDetailRepositoryPort
import javax.inject.Inject

class GetReflexionDetailUseCase @Inject constructor(
    private val repository: ReflexionDetailRepositoryPort
) {
    operator fun invoke(
        slogan: String
    ): Flow<ApiResult<ReflexionDetailModel>> =
        repository.getReflexionDetail(slogan = slogan)
}
