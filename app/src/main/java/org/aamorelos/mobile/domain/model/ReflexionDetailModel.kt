package org.aamorelos.mobile.domain.model

/**
 * Modelo de dominio del detalle de una reflexión (hexagonal).
 * Identificado por [slogan] (clave de `GET reflexiones/{slogan}`).
 */
data class ReflexionDetailModel(
    val id: Int,
    val slogan: String,
    val titulo: String,
    val subtitulo: String?,
    val imagen: String?,
    val alt: String?,
    val url: String?,
    val descripcion: String?
)
