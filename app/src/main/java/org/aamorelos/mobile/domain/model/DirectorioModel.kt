package org.aamorelos.mobile.domain.model

/**
 * Modelo de dominio para un ítem del directorio (lista paginada tipo galería).
 * La imagen vive en [imagen] (nombre exacto del campo JSON de la API).
 * [anio] y [mes] vienen tal cual de la API ("2026", "Septiembre").
 */
data class DirectorioModel(
    val id: Int,
    val pagina: Int,
    val imagen: String?,
    val anio: String,
    val mes: String
) {
    /**
     * Clave estable para la caché de imágenes en disco.
     * Incluye página + periodo para que un nuevo mes no colisione
     * con archivos de meses anteriores con los mismos números de página.
     */
    val imageKey: String
        get() = "pagina_${pagina}_${anio}_${mes}"
            .replace(" ", "_")
            .lowercase()
}
