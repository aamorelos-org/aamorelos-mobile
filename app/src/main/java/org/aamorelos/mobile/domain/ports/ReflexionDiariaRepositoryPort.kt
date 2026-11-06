package org.aamorelos.mobile.domain.ports

import kotlinx.coroutines.flow.Flow
import org.aamorelos.mobile.domain.ApiResult
import org.aamorelos.mobile.domain.model.ReflexionDiariaModel

/**
 * Puerto de salida para la reflexión diaria (driven port) — hexagonal.
 * [fecha] en formato "MM-DD" (ej: "11-06").
 */
interface ReflexionDiariaRepositoryPort {
    fun getReflexionDiaria(fecha: String): Flow<ApiResult<ReflexionDiariaModel>>
}
