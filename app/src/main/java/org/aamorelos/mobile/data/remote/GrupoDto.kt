package org.aamorelos.mobile.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.aamorelos.mobile.domain.model.GrupoModel

@JsonClass(generateAdapter = false)
data class GrupoDto(
    val id: String,
    val nombre: String,
    @Json(name = "distrito_id")
    val distritoId: String?,
    val municipio: String,
    val vecinos: String?,
    val tipo: String,
    val genero: String,
    val lengua: String
)

@JsonClass(generateAdapter = false)
data class GruposResponse(
    val ok: Boolean,
    @Json(name = "estado_id")
    val estadoId: String?,
    @Json(name = "total_registros")
    val totalRegistros: Int?,
    val data: List<GrupoDto>
)

fun GrupoDto.toModel(): GrupoModel = GrupoModel(
    id = id,
    nombre = nombre,
    distritoId = distritoId ?: "",
    municipio = municipio,
    vecinos = vecinos?.takeIf { it.isNotBlank() },
    tipo = tipo,
    genero = genero,
    lengua = lengua
)
