package org.aamorelos.mobile.data.local

import org.aamorelos.mobile.domain.model.ReflexionModel

/**
 * Entidad local de reflexión (tabla `reflexiones`).
 * [position] = offset global en la API (skip + índice) para ordenar
 * de forma estable en las consultas paginadas con LIMIT/OFFSET.
 */
data class ReflexionEntity(
    val id: Int,
    val slogan: String,
    val titulo: String,
    val subtitulo: String,
    val imagen: String?,
    val alt: String?,
    val url: String?,
    val position: Int,
    val ultimaActualizacion: Long
) {
    fun toModel(): ReflexionModel = ReflexionModel(
        id = id,
        slogan = slogan,
        titulo = titulo,
        subtitulo = subtitulo,
        imagen = imagen?.takeIf { it.isNotBlank() },
        alt = alt?.takeIf { it.isNotBlank() },
        url = url?.takeIf { it.isNotBlank() }
    )
}
