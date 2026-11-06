package org.aamorelos.mobile.domain.ports

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.aamorelos.mobile.domain.model.DirectorioModel

/**
 * Puerto de salida para directorio con paginación (driven port) — hexagonal.
 */
interface DirectorioRepositoryPort {
    fun getDirectorioPager(): Flow<PagingData<DirectorioModel>>
}
