package org.aamorelos.mobile.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.aamorelos.mobile.domain.model.GrupoDetailModel

/**
 * DTO de detalle de grupo — mapea 1:1 el JSON plano de
 * GET estados/{estadoId}/grupos/{id}.
 *
 * Los campos snake_case de la API se traducen con @Json.
 * Se permiten nullables y defaults para robustez ante campos ausentes.
 */
@JsonClass(generateAdapter = false)
data class GrupoDetailDto(
    val id: String,
    @Json(name = "distrito_id")
    val distritoId: String,
    @Json(name = "municipio_id")
    val municipioId: String,
    @Json(name = "tipo_id")
    val tipoId: Int,
    @Json(name = "lengua_id")
    val lenguaId: Int,
    @Json(name = "genero_id")
    val generoId: Int,
    val nombre: String,
    @Json(name = "fecha_creacion")
    val fechaCreacion: String? = null,
    val direccion: String? = null,
    val vecinos: String? = null,
    @Json(name = "codigo_postal")
    val codigoPostal: String? = null,
    val referencias: String? = null,
    val localidad: String? = null,
    val cpr: String? = null,
    @Json(name = "horario_lunes")
    val horarioLunes: String? = null,
    @Json(name = "horario_martes")
    val horarioMartes: String? = null,
    @Json(name = "horario_miercoles")
    val horarioMiercoles: String? = null,
    @Json(name = "horario_jueves")
    val horarioJueves: String? = null,
    @Json(name = "horario_viernes")
    val horarioViernes: String? = null,
    @Json(name = "horario_sabado")
    val horarioSabado: String? = null,
    @Json(name = "horario_domingo")
    val horarioDomingo: String? = null,
    @Json(name = "horario_observacion")
    val horarioObservacion: String? = null,
    @Json(name = "lenguaje_detalle")
    val lenguajeDetalle: String? = null,
    @Json(name = "permite_fumar")
    val permiteFumar: Int = 0,
    @Json(name = "es_activo")
    val esActivo: Int = 0,
    val latitud: String? = null,
    val longitud: String? = null,
    @Json(name = "fumar_texto")
    val fumarTexto: String? = null,
    val municipio: String,
    val tipo: String,
    val genero: String,
    val lengua: String
)

fun GrupoDetailDto.toModel(): GrupoDetailModel = GrupoDetailModel(
    id = id,
    distritoId = distritoId,
    municipioId = municipioId,
    tipoId = tipoId,
    lenguaId = lenguaId,
    generoId = generoId,
    nombre = nombre,
    fechaCreacion = fechaCreacion?.takeIf { it.isNotBlank() },
    direccion = direccion?.takeIf { it.isNotBlank() },
    vecinos = vecinos?.takeIf { it.isNotBlank() },
    codigoPostal = codigoPostal?.takeIf { it.isNotBlank() },
    referencias = referencias?.takeIf { it.isNotBlank() },
    localidad = localidad?.takeIf { it.isNotBlank() },
    cpr = cpr?.takeIf { it.isNotBlank() },
    horarioLunes = horarioLunes?.takeIf { it.isNotBlank() },
    horarioMartes = horarioMartes?.takeIf { it.isNotBlank() },
    horarioMiercoles = horarioMiercoles?.takeIf { it.isNotBlank() },
    horarioJueves = horarioJueves?.takeIf { it.isNotBlank() },
    horarioViernes = horarioViernes?.takeIf { it.isNotBlank() },
    horarioSabado = horarioSabado?.takeIf { it.isNotBlank() },
    horarioDomingo = horarioDomingo?.takeIf { it.isNotBlank() },
    horarioObservacion = horarioObservacion?.takeIf { it.isNotBlank() },
    lenguajeDetalle = lenguajeDetalle?.takeIf { it.isNotBlank() },
    permiteFumar = permiteFumar,
    esActivo = esActivo,
    latitud = latitud?.takeIf { it.isNotBlank() },
    longitud = longitud?.takeIf { it.isNotBlank() },
    fumarTexto = fumarTexto?.takeIf { it.isNotBlank() },
    municipio = municipio,
    tipo = tipo,
    genero = genero,
    lengua = lengua
)
