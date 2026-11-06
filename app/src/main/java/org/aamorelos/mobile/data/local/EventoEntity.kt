package org.aamorelos.mobile.data.local

import org.aamorelos.mobile.domain.model.EventoItemModel

/**
 * Entidad local de evento (tabla `eventos`).
 * [position] = offset global en la API (skip + índice) para ordenar
 * de forma estable en las consultas paginadas con LIMIT/OFFSET.
 */
data class EventoEntity(
    val id: Int,
    val tipo: String,
    val slogan: String,
    val url: String?,
    val titulo: String,
    val subtitulo: String?,
    val imagen: String?,
    val alt: String?,
    val pie: String?,
    val position: Int,
    val ultimaActualizacion: Long
) {
    fun toModel(): EventoItemModel = EventoItemModel(
        id = id,
        tipo = tipo,
        slogan = slogan,
        url = url?.takeIf { it.isNotBlank() },
        titulo = titulo,
        subtitulo = subtitulo?.takeIf { it.isNotBlank() },
        imagen = imagen?.takeIf { it.isNotBlank() },
        alt = alt?.takeIf { it.isNotBlank() },
        pie = pie?.takeIf { it.isNotBlank() }
    )
}
