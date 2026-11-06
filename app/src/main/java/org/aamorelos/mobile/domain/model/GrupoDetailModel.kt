package org.aamorelos.mobile.domain.model

/**
 * Modelo de dominio (hexagonal) para el detalle de un grupo.
 * Representa un grupo individual con toda su información extendida:
 * horarios, dirección, ubicación y atributos descriptivos.
 *
 * Se usa camelCase en Kotlin; el mapeo desde snake_case se resuelve
 * en el DTO de la capa de datos.
 */
data class GrupoDetailModel(
    val id: String,
    val distritoId: String,
    val municipioId: String,
    val tipoId: Int,
    val lenguaId: Int,
    val generoId: Int,
    val nombre: String,
    val fechaCreacion: String?,
    val direccion: String?,
    val vecinos: String?,
    val codigoPostal: String?,
    val referencias: String?,
    val localidad: String?,
    val cpr: String?,
    val horarioLunes: String?,
    val horarioMartes: String?,
    val horarioMiercoles: String?,
    val horarioJueves: String?,
    val horarioViernes: String?,
    val horarioSabado: String?,
    val horarioDomingo: String?,
    val horarioObservacion: String?,
    val lenguajeDetalle: String?,
    val permiteFumar: Int,
    val esActivo: Int,
    val latitud: String?,
    val longitud: String?,
    val fumarTexto: String?,
    val municipio: String,
    val tipo: String,
    val genero: String,
    val lengua: String
) {
    val isActivo: Boolean get() = esActivo == 1

    val hasCoordinates: Boolean
        get() = !latitud.isNullOrBlank() && !longitud.isNullOrBlank() && latitud != "0" && longitud != "0"

    data class DiaHorario(val dia: String, val horario: String)

    val horarios: List<DiaHorario>
        get() = listOf(
            DiaHorario("Lunes", horarioLunes.orEmpty()),
            DiaHorario("Martes", horarioMartes.orEmpty()),
            DiaHorario("Miércoles", horarioMiercoles.orEmpty()),
            DiaHorario("Jueves", horarioJueves.orEmpty()),
            DiaHorario("Viernes", horarioViernes.orEmpty()),
            DiaHorario("Sábado", horarioSabado.orEmpty()),
            DiaHorario("Domingo", horarioDomingo.orEmpty())
        ).filter { it.horario.isNotBlank() }
}
