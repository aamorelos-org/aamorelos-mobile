package org.aamorelos.mobile.data.local

import org.aamorelos.mobile.domain.model.GrupoModel

data class GrupoEntity(
    val estadoId: String,
    val id: String,
    val nombre: String,
    val distritoId: String?,
    val municipio: String,
    val vecinos: String?,
    val tipo: String,
    val genero: String,
    val lengua: String,
    val ultimaActualizacion: Long
) {
    fun toModel(): GrupoModel = GrupoModel(
        id = id,
        nombre = nombre,
        distritoId = distritoId ?: "",
        municipio = municipio,
        vecinos = vecinos?.takeIf { it.isNotBlank() },
        tipo = tipo,
        genero = genero,
        lengua = lengua
    )
}
