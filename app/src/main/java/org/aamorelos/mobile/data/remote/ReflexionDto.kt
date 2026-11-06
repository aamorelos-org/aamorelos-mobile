package org.aamorelos.mobile.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.aamorelos.mobile.domain.model.ReflexionModel

/**
 * DTO de reflexión — mapea 1:1 el JSON plano de
 * GET reflexiones/?skip={skip}&limit={limit}.
 * La propiedad de imagen se llama exactamente "imagen".
 */
@JsonClass(generateAdapter = false)
data class ReflexionDto(
    val id: Int,
    val slogan: String,
    val titulo: String,
    val subtitulo: String,
    val imagen: String? = null,
    val alt: String? = null,
    val url: String? = null
)

@JsonClass(generateAdapter = false)
data class ReflexionesResponse(
    val ok: Boolean,
    @Json(name = "total_registros")
    val totalRegistros: Int?,
    val skip: Int? = null,
    val limit: Int? = null,
    val data: List<ReflexionDto>
)

fun ReflexionDto.toModel(): ReflexionModel = ReflexionModel(
    id = id,
    slogan = slogan,
    titulo = titulo,
    subtitulo = subtitulo,
    imagen = imagen?.takeIf { it.isNotBlank() },
    alt = alt?.takeIf { it.isNotBlank() },
    url = url?.takeIf { it.isNotBlank() }
)
