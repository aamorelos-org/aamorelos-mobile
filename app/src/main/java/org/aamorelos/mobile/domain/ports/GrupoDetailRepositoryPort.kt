package org.aamorelos.mobile.domain.ports

import kotlinx.coroutines.flow.Flow
import org.aamorelos.mobile.domain.ApiResult
import org.aamorelos.mobile.domain.model.GrupoDetailModel

/**
 * Puerto de salida para el detalle de un grupo (driven port) — hexagonal.
 */
interface GrupoDetailRepositoryPort {
    fun getGrupoDetail(
        estadoId: String = "17",
        grupoId: String
    ): Flow<ApiResult<GrupoDetailModel>>
}
