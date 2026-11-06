package org.aamorelos.mobile.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.aamorelos.mobile.domain.ApiResult
import org.aamorelos.mobile.domain.model.EventoDetailModel
import org.aamorelos.mobile.domain.ports.EventoDetailRepositoryPort
import javax.inject.Inject

class GetEventoDetailUseCase @Inject constructor(
    private val repository: EventoDetailRepositoryPort
) {
    operator fun invoke(
        eventoId: Int
    ): Flow<ApiResult<EventoDetailModel>> =
        repository.getEventoDetail(eventoId = eventoId)
}
