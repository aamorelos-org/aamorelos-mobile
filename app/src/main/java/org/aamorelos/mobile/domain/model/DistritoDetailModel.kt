package org.aamorelos.mobile.domain.model

/**
 * Modelo de dominio (hexagonal) para el detalle de un distrito.
 * Representa un distrito individual con su información extendida:
 * dirección, contacto, horarios de servicio y atributos descriptivos.
 *
 * Se usa camelCase en Kotlin; el mapeo desde snake_case se resuelve
 * en el DTO de la capa de datos.
 */
data class DistritoDetailModel(
    val id: String,
    val areaId: String?,
    val municipioId: String?,
    val nombre: String,
    val distritoNumero: Int?,
    val fechaCreacion: String?,
    val direccion: String?,
    val vecinos: String?,
    val codigoPostal: String?,
    val referencia: String?,
    val telefono: String?,
    val servicioHoras: String?,
    val esActivo: Int?,
    val municipio: String?,
    val area: String?
) {
    val isActivo: Boolean get() = esActivo == 1
}
