package org.aamorelos.mobile.domain.model

/**
 * Modelo de dominio para una reflexión.
 * La imagen vive en [imagen] (nombre exacto del campo JSON de la API).
 */
data class ReflexionModel(
    val id: Int,
    val slogan: String,
    val titulo: String,
    val subtitulo: String,
    val imagen: String?,
    val alt: String?,
    val url: String?
)
