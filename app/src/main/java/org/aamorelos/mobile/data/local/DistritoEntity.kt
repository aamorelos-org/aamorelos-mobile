package org.aamorelos.mobile.data.local

import org.aamorelos.mobile.domain.model.DistritoModel

data class DistritoEntity(
    val estadoId: String,
    val id: String,
    val nombre: String,
    val municipio: String,
    val vecinos: String?,
    val area: String,
    val areaId: String,
    val ultimaActualizacion: Long
) {
    fun toModel(): DistritoModel = DistritoModel(
        id = id,
        nombre = nombre,
        municipio = municipio,
        vecinos = vecinos?.takeIf { it.isNotBlank() },
        area = area,
        areaId = areaId
    )
}
