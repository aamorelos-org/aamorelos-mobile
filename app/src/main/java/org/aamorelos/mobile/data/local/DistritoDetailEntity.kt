package org.aamorelos.mobile.data.local

import org.aamorelos.mobile.domain.model.DistritoDetailModel

/**
 * Entidad local para el detalle de un distrito (copia offline-first).
 * Refleja la tabla `distrito_detalle` y mapea a [DistritoDetailModel].
 */
data class DistritoDetailEntity(
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
    val area: String?,
    val ultimaActualizacion: Long
) {
    fun toModel(): DistritoDetailModel = DistritoDetailModel(
        id = id,
        areaId = areaId?.takeIf { it.isNotBlank() },
        municipioId = municipioId?.takeIf { it.isNotBlank() },
        nombre = nombre,
        distritoNumero = distritoNumero,
        fechaCreacion = fechaCreacion?.takeIf { it.isNotBlank() },
        direccion = direccion?.takeIf { it.isNotBlank() },
        vecinos = vecinos?.takeIf { it.isNotBlank() },
        codigoPostal = codigoPostal?.takeIf { it.isNotBlank() },
        referencia = referencia?.takeIf { it.isNotBlank() },
        telefono = telefono?.takeIf { it.isNotBlank() },
        servicioHoras = servicioHoras?.takeIf { it.isNotBlank() },
        esActivo = esActivo,
        municipio = municipio?.takeIf { it.isNotBlank() },
        area = area?.takeIf { it.isNotBlank() }
    )
}
