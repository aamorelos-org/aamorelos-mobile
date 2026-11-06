package org.aamorelos.mobile.domain.ports

import kotlinx.coroutines.flow.Flow
import org.aamorelos.mobile.domain.ApiResult
import org.aamorelos.mobile.domain.model.DistritoModel

/**
 * Puerto de salida (driven port) — hexagonal.
 * El dominio define el contrato; la infraestructura provee el adaptador.
 */
interface DistritosRepositoryPort {
    fun getDistritos(estadoId: String): Flow<ApiResult<List<DistritoModel>>>
}
