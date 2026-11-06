package org.aamorelos.mobile.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.aamorelos.mobile.domain.ApiResult
import org.aamorelos.mobile.domain.model.DistritoModel
import org.aamorelos.mobile.domain.ports.DistritosRepositoryPort
import javax.inject.Inject

class GetDistritosUseCase @Inject constructor(
    private val repository: DistritosRepositoryPort
) {
    operator fun invoke(
        estadoId: String = "17"
    ): Flow<ApiResult<List<DistritoModel>>> =
        repository.getDistritos(estadoId)
}
