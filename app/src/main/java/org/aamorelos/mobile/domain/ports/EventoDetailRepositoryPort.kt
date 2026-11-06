package org.aamorelos.mobile.domain.ports

import kotlinx.coroutines.flow.Flow
import org.aamorelos.mobile.domain.ApiResult
import org.aamorelos.mobile.domain.model.EventoDetailModel

/**
 * Puerto de salida para el detalle de un evento (driven port) — hexagonal.
 */
interface EventoDetailRepositoryPort {
    fun getEventoDetail(
        eventoId: Int
    ): Flow<ApiResult<EventoDetailModel>>
}
