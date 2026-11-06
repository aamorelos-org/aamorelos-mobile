package org.aamorelos.mobile.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.aamorelos.mobile.domain.model.EventoItemModel

/**
 * DTO de ítem de evento — mapea 1:1 el JSON plano de
 * GET eventos/?skip={skip}&limit={limit}.
 * La propiedad de imagen se llama exactamente "imagen".
 */
@JsonClass(generateAdapter = false)
data class EventoItemDto(
    val id: Int,
    val tipo: String,
    val slogan: String,
    val url: String? = null,
    val titulo: String,
    val subtitulo: String? = null,
    val imagen: String? = null,
    val alt: String? = null,
    val pie: String? = null
)

@JsonClass(generateAdapter = false)
data class EventosResponse(
    val ok: Boolean,
    @Json(name = "total_registros")
    val totalRegistros: Int?,
    val skip: Int? = null,
    val limit: Int? = null,
    val data: List<EventoItemDto>
)

fun EventoItemDto.toModel(): EventoItemModel = EventoItemModel(
    id = id,
    tipo = tipo,
    slogan = slogan,
    url = url?.takeIf { it.isNotBlank() },
    titulo = titulo,
    subtitulo = subtitulo?.takeIf { it.isNotBlank() },
    imagen = imagen?.takeIf { it.isNotBlank() },
    alt = alt?.takeIf { it.isNotBlank() },
    pie = pie?.takeIf { it.isNotBlank() }
)
