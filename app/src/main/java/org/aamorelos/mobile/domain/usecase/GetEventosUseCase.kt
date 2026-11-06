package org.aamorelos.mobile.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.aamorelos.mobile.domain.model.EventoItemModel
import org.aamorelos.mobile.domain.ports.EventosRepositoryPort
import javax.inject.Inject

class GetEventosUseCase @Inject constructor(
    private val repository: EventosRepositoryPort
) {
    operator fun invoke(): Flow<PagingData<EventoItemModel>> =
        repository.getEventosPager()
}
