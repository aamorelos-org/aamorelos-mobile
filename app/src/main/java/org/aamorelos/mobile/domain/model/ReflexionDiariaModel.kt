package org.aamorelos.mobile.domain.model

/**
 * Modelo de dominio de la reflexión diaria (hexagonal).
 * Identificado por [fecha] ("MM-DD", ej: "11-06") — clave de
 * `GET reflexiones-diarias/?fecha={MM-DD}`.
 * [titulo] es título principal, [texto] cuerpo, [contenido] referencia
 * bibliográfica, [cita] cita destacada.
 */
data class ReflexionDiariaModel(
    val id: Int,
    val fecha: String,
    val titulo: String,
    val texto: String?,
    val cita: String?,
    val contenido: String?
)
