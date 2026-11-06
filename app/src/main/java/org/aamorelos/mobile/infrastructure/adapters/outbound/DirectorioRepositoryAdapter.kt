package org.aamorelos.mobile.infrastructure.adapters.outbound

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.aamorelos.mobile.data.local.AAMorelosDatabase
import org.aamorelos.mobile.data.paging.DirectorioPagingSource
import org.aamorelos.mobile.data.remote.AaMorelosApi
import org.aamorelos.mobile.domain.model.DirectorioModel
import org.aamorelos.mobile.domain.ports.DirectorioRepositoryPort
import javax.inject.Inject

/**
 * Adaptador de salida paginado — hexagonal.
 * Expone un [Pager] que crea [DirectorioPagingSource] (API + SQLite).
 */
class DirectorioRepositoryAdapter @Inject constructor(
    private val api: AaMorelosApi,
    private val database: AAMorelosDatabase
) : DirectorioRepositoryPort {

    override fun getDirectorioPager(): Flow<PagingData<DirectorioModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                prefetchDistance = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                DirectorioPagingSource(api = api, database = database)
            }
        ).flow
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}
