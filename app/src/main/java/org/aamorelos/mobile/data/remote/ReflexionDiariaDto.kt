package org.aamorelos.mobile.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.aamorelos.mobile.domain.model.ReflexionDiariaModel

/**
 * DTO de la reflexión diaria — mapea 1:1 el JSON plano de
 * GET reflexiones-diarias/?fecha={MM-DD}.
 * Respuesta: `{ ok, total_registros, fecha, data: [{ id, fecha, titulo, texto, cita, contenido }] }`.
 */
@JsonClass(generateAdapter = false)
data class ReflexionDiariaDto(
    val id: Int,
    val fecha: String,
    val titulo: String,
    val texto: String? = null,
    val cita: String? = null,
    val contenido: String? = null
)

@JsonClass(generateAdapter = false)
data class ReflexionDiariaResponse(
    val ok: Boolean,
    @Json(name = "total_registros")
    val totalRegistros: Int?,
    val fecha: String?,
    val data: List<ReflexionDiariaDto>
)

fun ReflexionDiariaDto.toModel(): ReflexionDiariaModel = ReflexionDiariaModel(
    id = id,
    fecha = fecha,
    titulo = titulo,
    texto = texto?.takeIf { it.isNotBlank() },
    cita = cita?.takeIf { it.isNotBlank() },
    contenido = contenido?.takeIf { it.isNotBlank() }
)
