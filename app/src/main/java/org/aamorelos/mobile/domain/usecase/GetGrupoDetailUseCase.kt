package org.aamorelos.mobile.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.aamorelos.mobile.domain.ApiResult
import org.aamorelos.mobile.domain.model.GrupoDetailModel
import org.aamorelos.mobile.domain.ports.GrupoDetailRepositoryPort
import javax.inject.Inject

class GetGrupoDetailUseCase @Inject constructor(
    private val repository: GrupoDetailRepositoryPort
) {
    operator fun invoke(
        grupoId: String,
        estadoId: String = "17"
    ): Flow<ApiResult<GrupoDetailModel>> =
        repository.getGrupoDetail(estadoId = estadoId, grupoId = grupoId)
}
