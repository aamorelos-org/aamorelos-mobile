package org.aamorelos.mobile.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.aamorelos.mobile.domain.model.DistritoDetailModel

/**
 * DTO de detalle de distrito — mapea 1:1 el JSON plano de
 * GET distritos/{id}.
 *
 * Los campos snake_case de la API se traducen con @Json.
 * Se permiten nullables y defaults para robustez ante campos ausentes.
 */
@JsonClass(generateAdapter = false)
data class DistritoDetailDto(
    val id: String,
    @Json(name = "area_id")
    val areaId: String? = null,
    @Json(name = "municipio_id")
    val municipioId: String? = null,
    val nombre: String,
    @Json(name = "distrito_numero")
    val distritoNumero: Int? = null,
    @Json(name = "fecha_creacion")
    val fechaCreacion: String? = null,
    val direccion: String? = null,
    val vecinos: String? = null,
    @Json(name = "codigo_postal")
    val codigoPostal: String? = null,
    // Ojo: la API usa "referencia" (singular), no "referencias"
    val referencia: String? = null,
    val telefono: String? = null,
    @Json(name = "servicio_horas")
    val servicioHoras: String? = null,
    @Json(name = "es_activo")
    val esActivo: Int? = null,
    val municipio: String? = null,
    val area: String? = null
)

fun DistritoDetailDto.toModel(): DistritoDetailModel = DistritoDetailModel(
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
