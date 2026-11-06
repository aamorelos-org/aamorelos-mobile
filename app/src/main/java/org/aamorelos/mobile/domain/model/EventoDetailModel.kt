package org.aamorelos.mobile.domain.model

/**
 * Modelo de dominio del detalle de un evento (polimórfico).
 *
 * - Informativo: [frases] trae los párrafos de lectura.
 * - Aniversario: [aniversario] trae invitación, sede, contactos y agenda por días.
 */
data class EventoDetailModel(
    val id: Int,
    val tipo: String, // "informativo" o "aniversario"
    val slogan: String,
    val url: String?,
    val titulo: String,
    val subtitulo: String?,
    val imagen: String?,
    val alt: String?,
    val pie: String?,
    val frases: List<String>?,
    val aniversario: AniversarioModel?
) {
    val isAniversario: Boolean get() = tipo == "aniversario" && aniversario != null
    val isInformativo: Boolean get() = tipo == "informativo"
}

data class AniversarioModel(
    val epigrafe: String?,
    val kicker: String?,
    val fraseReflexion: String?,
    val sede: String?,
    val diasUnidad: String?,
    val juntaPublica: String?,
    val contactoNombre: String?,
    val contactoTelefono: String?,
    val dias: List<AniversarioDiaModel>
)

data class AniversarioDiaModel(
    val dia: String,
    val tema: String,
    val fecha: String,
    val grupos: String,
    val numero: String,
    val esJuntaPublica: Boolean
)
