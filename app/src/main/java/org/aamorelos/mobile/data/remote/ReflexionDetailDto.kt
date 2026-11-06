package org.aamorelos.mobile.data.remote

import com.squareup.moshi.JsonClass
import org.aamorelos.mobile.domain.model.ReflexionDetailModel

/**
 * DTOs de detalle de reflexión — mapean
 * GET reflexiones/{slogan} -> `{ "ok": true, "data": { ... } }`.
 *
 * La imagen se llama exactamente "imagen" y el cuerpo es "descripcion".
 */
@JsonClass(generateAdapter = false)
data class ReflexionDetailDto(
    val id: Int,
    val slogan: String,
    val titulo: String,
    val subtitulo: String? = null,
    val imagen: String? = null,
    val alt: String? = null,
    val url: String? = null,
    val descripcion: String? = null
)

@JsonClass(generateAdapter = false)
data class ReflexionDetailResponse(
    val ok: Boolean,
    val data: ReflexionDetailDto
)

fun ReflexionDetailDto.toModel(): ReflexionDetailModel = ReflexionDetailModel(
    id = id,
    slogan = slogan,
    titulo = titulo,
    subtitulo = subtitulo?.takeIf { it.isNotBlank() },
    imagen = imagen?.takeIf { it.isNotBlank() },
    alt = alt?.takeIf { it.isNotBlank() },
    url = url?.takeIf { it.isNotBlank() },
    descripcion = descripcion?.takeIf { it.isNotBlank() }
)
