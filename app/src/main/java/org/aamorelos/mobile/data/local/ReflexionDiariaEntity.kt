package org.aamorelos.mobile.data.local

import org.aamorelos.mobile.domain.model.ReflexionDiariaModel

/**
 * Entidad local para la reflexión diaria (copia offline-first).
 * Refleja la tabla `reflexion_diaria` y mapea a [ReflexionDiariaModel].
 * Clave natural: [fecha] ("MM-DD") — equivalente al slogan en reflexion_detalle.
 */
data class ReflexionDiariaEntity(
    val fecha: String,
    val id: Int,
    val titulo: String,
    val texto: String?,
    val cita: String?,
    val contenido: String?,
    val ultimaActualizacion: Long
) {
    fun toModel(): ReflexionDiariaModel = ReflexionDiariaModel(
        id = id,
        fecha = fecha,
        titulo = titulo,
        texto = texto?.takeIf { it.isNotBlank() },
        cita = cita?.takeIf { it.isNotBlank() },
        contenido = contenido?.takeIf { it.isNotBlank() }
    )
}
