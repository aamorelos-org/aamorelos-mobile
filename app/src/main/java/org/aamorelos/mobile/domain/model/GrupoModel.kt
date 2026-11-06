package org.aamorelos.mobile.domain.model

data class GrupoModel(
    val id: String,
    val nombre: String,
    val distritoId: String,
    val municipio: String,
    val vecinos: String?,
    val tipo: String,
    val genero: String,
    val lengua: String
)
