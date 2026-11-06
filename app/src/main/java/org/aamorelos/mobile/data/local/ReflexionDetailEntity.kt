package org.aamorelos.mobile.data.local

import org.aamorelos.mobile.domain.model.ReflexionDetailModel

/**
 * Entidad local para el detalle de una reflexión (copia offline-first).
 * Refleja la tabla `reflexion_detalle` y mapea a [ReflexionDetailModel].
 */
data class ReflexionDetailEntity(
    val slogan: String,
    val id: Int,
    val titulo: String,
    val subtitulo: String?,
    val imagen: String?,
    val alt: String?,
    val url: String?,
    val descripcion: String?,
    val ultimaActualizacion: Long
) {
    fun toModel(): ReflexionDetailModel = ReflexionDetailModel(
        id = id,
        slogan = slogan,
        titulo = titulo,
        subtitulo = subtitulo?.takeIf { it.isNotBlank() },
        imagen = imagen?.takeIf { it.isNotBlank() },
        alt = alt?.takeIf { it.isNotBlank() },
        url = url?.takeIf { it.isNotBlank() },
        descripcion = descripcion?.takeIf { it.isNotBlank() }
    )
}
