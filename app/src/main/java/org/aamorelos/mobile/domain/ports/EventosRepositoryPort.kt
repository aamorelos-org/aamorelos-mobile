package org.aamorelos.mobile.domain.ports

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.aamorelos.mobile.domain.model.EventoItemModel

/**
 * Puerto de salida para eventos con paginación (driven port) — hexagonal.
 */
interface EventosRepositoryPort {
    fun getEventosPager(): Flow<PagingData<EventoItemModel>>
}
