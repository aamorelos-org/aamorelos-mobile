package org.aamorelos.mobile.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.aamorelos.mobile.domain.model.AniversarioDiaModel
import org.aamorelos.mobile.domain.model.AniversarioModel
import org.aamorelos.mobile.domain.model.EventoDetailModel

/**
 * DTOs de detalle de evento (polimórfico) — mapean
 * GET eventos/{id} -> `{ "ok": true, "data": { ... } }`.
 *
 * - Informativo: `frases` trae los párrafos, `aniversario` es null.
 * - Aniversario: `frases` es null, `aniversario` trae invitación + agenda.
 */
@JsonClass(generateAdapter = false)
data class EventoDetailDto(
    val id: Int,
    val tipo: String,
    val slogan: String,
    val url: String? = null,
    val titulo: String,
    val subtitulo: String? = null,
    val imagen: String? = null,
    val alt: String? = null,
    val pie: String? = null,
    val frases: List<String>? = null,
    val aniversario: AniversarioDto? = null
)

@JsonClass(generateAdapter = false)
data class AniversarioDto(
    val epigrafe: String? = null,
    val kicker: String? = null,
    @Json(name = "frase_reflexion")
    val fraseReflexion: String? = null,
    val sede: String? = null,
    @Json(name = "dias_unidad")
    val diasUnidad: String? = null,
    @Json(name = "junta_publica")
    val juntaPublica: String? = null,
    @Json(name = "contacto_nombre")
    val contactoNombre: String? = null,
    @Json(name = "contacto_telefono")
    val contactoTelefono: String? = null,
    val dias: List<AniversarioDiaDto> = emptyList()
)

@JsonClass(generateAdapter = false)
data class AniversarioDiaDto(
    val dia: String,
    val tema: String,
    val fecha: String,
    val grupos: String,
    val numero: String,
    @Json(name = "es_junta_publica")
    val esJuntaPublica: Boolean = false
)

@JsonClass(generateAdapter = false)
data class EventoDetailResponse(
    val ok: Boolean,
    val data: EventoDetailDto
)

fun EventoDetailDto.toModel(): EventoDetailModel = EventoDetailModel(
    id = id,
    tipo = tipo,
    slogan = slogan,
    url = url?.takeIf { it.isNotBlank() },
    titulo = titulo,
    subtitulo = subtitulo?.takeIf { it.isNotBlank() },
    imagen = imagen?.takeIf { it.isNotBlank() },
    alt = alt?.takeIf { it.isNotBlank() },
    pie = pie?.takeIf { it.isNotBlank() },
    frases = frases?.map { it.trim() }?.filter { it.isNotBlank() }?.takeIf { it.isNotEmpty() },
    aniversario = aniversario?.toModel()
)

fun AniversarioDto.toModel(): AniversarioModel = AniversarioModel(
    epigrafe = epigrafe?.takeIf { it.isNotBlank() },
    kicker = kicker?.takeIf { it.isNotBlank() },
    fraseReflexion = fraseReflexion?.takeIf { it.isNotBlank() },
    sede = sede?.takeIf { it.isNotBlank() },
    diasUnidad = diasUnidad?.takeIf { it.isNotBlank() },
    juntaPublica = juntaPublica?.takeIf { it.isNotBlank() },
    contactoNombre = contactoNombre?.takeIf { it.isNotBlank() },
    contactoTelefono = contactoTelefono?.takeIf { it.isNotBlank() },
    dias = dias.map { it.toModel() }
)

fun AniversarioDiaDto.toModel(): AniversarioDiaModel = AniversarioDiaModel(
    dia = dia,
    tema = tema,
    fecha = fecha,
    grupos = grupos,
    numero = numero,
    esJuntaPublica = esJuntaPublica
)
