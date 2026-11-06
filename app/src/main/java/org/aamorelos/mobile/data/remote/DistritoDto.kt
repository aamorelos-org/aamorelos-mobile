package org.aamorelos.mobile.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.aamorelos.mobile.domain.model.DistritoModel

@JsonClass(generateAdapter = false)
data class DistritoDto(
    val id: String,
    val nombre: String,
    val municipio: String,
    val vecinos: String?,
    val area: String?,
    @Json(name = "area_id")
    val areaId: String?
)

@JsonClass(generateAdapter = false)
data class DistritosResponse(
    val ok: Boolean,
    @Json(name = "estado_id")
    val estadoId: String?,
    @Json(name = "area_id")
    val areaId: String?,
    @Json(name = "total_registros")
    val totalRegistros: Int?,
    val data: List<DistritoDto>
)

fun DistritoDto.toModel(): DistritoModel = DistritoModel(
    id = id,
    nombre = nombre,
    municipio = municipio,
    vecinos = vecinos?.takeIf { it.isNotBlank() },
    area = area ?: "",
    areaId = areaId ?: ""
)
