package org.aamorelos.mobile.data.local

import org.aamorelos.mobile.domain.model.GrupoDetailModel

/**
 * Entidad local para el detalle de un grupo (copia offline-first).
 * Refleja la tabla `grupo_detalle` y mapea a [GrupoDetailModel].
 */
data class GrupoDetailEntity(
    val estadoId: String,
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
    val lengua: String,
    val ultimaActualizacion: Long
) {
    fun toModel(): GrupoDetailModel = GrupoDetailModel(
        id = id,
        distritoId = distritoId,
        municipioId = municipioId,
        tipoId = tipoId,
        lenguaId = lenguaId,
        generoId = generoId,
        nombre = nombre,
        fechaCreacion = fechaCreacion?.takeIf { it.isNotBlank() },
        direccion = direccion?.takeIf { it.isNotBlank() },
        vecinos = vecinos?.takeIf { it.isNotBlank() },
        codigoPostal = codigoPostal?.takeIf { it.isNotBlank() },
        referencias = referencias?.takeIf { it.isNotBlank() },
        localidad = localidad?.takeIf { it.isNotBlank() },
        cpr = cpr?.takeIf { it.isNotBlank() },
        horarioLunes = horarioLunes?.takeIf { it.isNotBlank() },
        horarioMartes = horarioMartes?.takeIf { it.isNotBlank() },
        horarioMiercoles = horarioMiercoles?.takeIf { it.isNotBlank() },
        horarioJueves = horarioJueves?.takeIf { it.isNotBlank() },
        horarioViernes = horarioViernes?.takeIf { it.isNotBlank() },
        horarioSabado = horarioSabado?.takeIf { it.isNotBlank() },
        horarioDomingo = horarioDomingo?.takeIf { it.isNotBlank() },
        horarioObservacion = horarioObservacion?.takeIf { it.isNotBlank() },
        lenguajeDetalle = lenguajeDetalle?.takeIf { it.isNotBlank() },
        permiteFumar = permiteFumar,
        esActivo = esActivo,
        latitud = latitud?.takeIf { it.isNotBlank() },
        longitud = longitud?.takeIf { it.isNotBlank() },
        fumarTexto = fumarTexto?.takeIf { it.isNotBlank() },
        municipio = municipio,
        tipo = tipo,
        genero = genero,
        lengua = lengua
    )
}
