package org.aamorelos.mobile.domain.ports

import kotlinx.coroutines.flow.Flow
import org.aamorelos.mobile.domain.ApiResult
import org.aamorelos.mobile.domain.model.GrupoModel

/**
 * Puerto de salida (driven port) — hexagonal.
 * El dominio define el contrato; la infraestructura provee el adaptador.
 */
interface GruposRepositoryPort {
    fun getGrupos(estadoId: String): Flow<ApiResult<List<GrupoModel>>>
}
