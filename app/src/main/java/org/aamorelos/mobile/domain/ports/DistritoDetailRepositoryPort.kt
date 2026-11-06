package org.aamorelos.mobile.domain.ports

import kotlinx.coroutines.flow.Flow
import org.aamorelos.mobile.domain.ApiResult
import org.aamorelos.mobile.domain.model.DistritoDetailModel

/**
 * Puerto de salida para el detalle de un distrito (driven port) — hexagonal.
 */
interface DistritoDetailRepositoryPort {
    fun getDistritoDetail(
        distritoId: String
    ): Flow<ApiResult<DistritoDetailModel>>
}
