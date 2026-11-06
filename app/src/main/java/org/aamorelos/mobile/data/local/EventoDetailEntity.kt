package org.aamorelos.mobile.data.local

import org.aamorelos.mobile.domain.model.EventoDetailModel

/**
 * Entidad local del detalle de un evento (tablas evento_detalle *,
 * ver AAMorelosDatabase). Reúne cabecera + frases + aniversario.
 */
data class EventoDetailEntity(
    val id: Int,
    val tipo: String,
    val slogan: String,
    val url: String?,
    val titulo: String,
    val subtitulo: String?,
    val imagen: String?,
    val alt: String?,
    val pie: String?,
    val frases: List<String>,
    val aniversario: EventoAniversarioEntity?,
    val ultimaActualizacion: Long
) {
    fun toModel(): EventoDetailModel = EventoDetailModel(
        id = id,
        tipo = tipo,
        slogan = slogan,
        url = url?.takeIf { it.isNotBlank() },
        titulo = titulo,
        subtitulo = subtitulo?.takeIf { it.isNotBlank() },
        imagen = imagen?.takeIf { it.isNotBlank() },
        alt = alt?.takeIf { it.isNotBlank() },
        pie = pie?.takeIf { it.isNotBlank() },
        frases = frases.takeIf { it.isNotEmpty() },
        aniversario = aniversario?.toModel()
    )
}

data class EventoAniversarioEntity(
    val epigrafe: String?,
    val kicker: String?,
    val fraseReflexion: String?,
    val sede: String?,
    val diasUnidad: String?,
    val juntaPublica: String?,
    val contactoNombre: String?,
    val contactoTelefono: String?,
    val dias: List<EventoAniversarioDiaEntity>
) {
    fun toModel(): org.aamorelos.mobile.domain.model.AniversarioModel =
        org.aamorelos.mobile.domain.model.AniversarioModel(
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
}

data class EventoAniversarioDiaEntity(
    val dia: String,
    val tema: String,
    val fecha: String,
    val grupos: String,
    val numero: String,
    val esJuntaPublica: Boolean
) {
    fun toModel(): org.aamorelos.mobile.domain.model.AniversarioDiaModel =
        org.aamorelos.mobile.domain.model.AniversarioDiaModel(
            dia = dia,
            tema = tema,
            fecha = fecha,
            grupos = grupos,
            numero = numero,
            esJuntaPublica = esJuntaPublica
        )
}
