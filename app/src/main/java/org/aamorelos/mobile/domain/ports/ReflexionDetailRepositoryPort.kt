package org.aamorelos.mobile.domain.ports

import kotlinx.coroutines.flow.Flow
import org.aamorelos.mobile.domain.ApiResult
import org.aamorelos.mobile.domain.model.ReflexionDetailModel

/**
 * Puerto de salida para el detalle de una reflexión (driven port) — hexagonal.
 */
interface ReflexionDetailRepositoryPort {
    fun getReflexionDetail(
        slogan: String
    ): Flow<ApiResult<ReflexionDetailModel>>
}
