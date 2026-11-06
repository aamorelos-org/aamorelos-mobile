package org.aamorelos.mobile.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.aamorelos.mobile.domain.ApiResult
import org.aamorelos.mobile.domain.model.ReflexionDiariaModel
import org.aamorelos.mobile.domain.ports.ReflexionDiariaRepositoryPort
import javax.inject.Inject

class GetReflexionDiariaUseCase @Inject constructor(
    private val repository: ReflexionDiariaRepositoryPort
) {
    operator fun invoke(fecha: String): Flow<ApiResult<ReflexionDiariaModel>> =
        repository.getReflexionDiaria(fecha = fecha)
}
