package org.aamorelos.mobile.domain.ports

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.aamorelos.mobile.domain.model.ReflexionModel

/**
 * Puerto de salida para reflexiones con paginación (driven port) — hexagonal.
 */
interface ReflexionesRepositoryPort {
    fun getReflexionesPager(): Flow<PagingData<ReflexionModel>>
}
