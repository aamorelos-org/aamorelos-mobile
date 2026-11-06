package org.aamorelos.mobile.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.aamorelos.mobile.domain.model.DirectorioModel

/**
 * DTO de ítem de directorio — mapea 1:1 el JSON plano de
 * GET directorio/?skip={skip}&limit={limit}.
 * Campos exactos de la API: id, pagina, imagen, anio, mes.
 * La propiedad de imagen se llama exactamente "imagen".
 */
@JsonClass(generateAdapter = false)
data class DirectorioItemDto(
    val id: Int,
    val pagina: Int,
    val imagen: String? = null,
    val anio: String = "",
    val mes: String = ""
)

@JsonClass(generateAdapter = false)
data class DirectorioResponse(
    val ok: Boolean,
    @Json(name = "total_registros")
    val totalRegistros: Int?,
    val skip: Int? = null,
    val limit: Int? = null,
    val data: List<DirectorioItemDto>
)

fun DirectorioItemDto.toModel(): DirectorioModel = DirectorioModel(
    id = id,
    pagina = pagina,
    imagen = imagen?.takeIf { it.isNotBlank() },
    anio = anio.takeIf { it.isNotBlank() } ?: "",
    mes = mes.takeIf { it.isNotBlank() } ?: ""
)
