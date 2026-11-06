package org.aamorelos.mobile.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.aamorelos.mobile.domain.ApiResult
import org.aamorelos.mobile.domain.model.GrupoModel
import org.aamorelos.mobile.domain.ports.GruposRepositoryPort
import javax.inject.Inject

class GetGruposUseCase @Inject constructor(
    private val repository: GruposRepositoryPort
) {
    operator fun invoke(
        estadoId: String = "17"
    ): Flow<ApiResult<List<GrupoModel>>> =
        repository.getGrupos(estadoId)
}
