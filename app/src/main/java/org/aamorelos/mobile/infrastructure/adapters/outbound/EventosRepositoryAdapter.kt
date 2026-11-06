package org.aamorelos.mobile.infrastructure.adapters.outbound

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.aamorelos.mobile.data.local.AAMorelosDatabase
import org.aamorelos.mobile.data.paging.EventosPagingSource
import org.aamorelos.mobile.data.remote.AaMorelosApi
import org.aamorelos.mobile.domain.model.EventoItemModel
import org.aamorelos.mobile.domain.ports.EventosRepositoryPort
import javax.inject.Inject

/**
 * Adaptador de salida paginado — hexagonal.
 * Expone un [Pager] que crea [EventosPagingSource] (API + SQLite).
 */
class EventosRepositoryAdapter @Inject constructor(
    private val api: AaMorelosApi,
    private val database: AAMorelosDatabase
) : EventosRepositoryPort {

    override fun getEventosPager(): Flow<PagingData<EventoItemModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                prefetchDistance = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                EventosPagingSource(api = api, database = database)
            }
        ).flow
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}
