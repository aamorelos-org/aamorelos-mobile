package org.aamorelos.mobile.domain.model

data class DistritoModel(
    val id: String,
    val nombre: String,
    val municipio: String,
    val vecinos: String?,
    val area: String,
    val areaId: String
)
