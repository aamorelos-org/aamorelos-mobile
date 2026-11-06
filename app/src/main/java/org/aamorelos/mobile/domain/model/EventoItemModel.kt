package org.aamorelos.mobile.domain.model

/**
 * Modelo de dominio para un ítem de evento (lista paginada).
 * La imagen vive en [imagen] (nombre exacto del campo JSON de la API).
 */
data class EventoItemModel(
    val id: Int,
    val tipo: String, // "informativo" o "aniversario"
    val slogan: String,
    val url: String?,
    val titulo: String,
    val subtitulo: String?,
    val imagen: String?,
    val alt: String?,
    val pie: String?
) {
    val isAniversario: Boolean get() = tipo == "aniversario"
    val isInformativo: Boolean get() = tipo == "informativo"
}
