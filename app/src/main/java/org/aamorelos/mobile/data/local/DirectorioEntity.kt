package org.aamorelos.mobile.data.local

import org.aamorelos.mobile.domain.model.DirectorioModel

/**
 * Entidad local de directorio (tabla `directorio`).
 * [position] = offset global en la API (skip + índice) para ordenar
 * de forma estable en las consultas paginadas con LIMIT/OFFSET.
 */
data class DirectorioEntity(
    val id: Int,
    val pagina: Int,
    val imagen: String?,
    val anio: String,
    val mes: String,
    val position: Int,
    val ultimaActualizacion: Long
) {
    fun toModel(): DirectorioModel = DirectorioModel(
        id = id,
        pagina = pagina,
        imagen = imagen?.takeIf { it.isNotBlank() },
        anio = anio?.takeIf { it.isNotBlank() } ?: "",
        mes = mes?.takeIf { it.isNotBlank() } ?: ""
    )
}
