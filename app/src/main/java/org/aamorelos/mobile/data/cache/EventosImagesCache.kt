package org.aamorelos.mobile.data.cache

import android.content.Context
import java.io.File

/**
 * Helper central para la caché de imágenes de eventos en disco.
 *
 * Carpeta persistente: `filesDir/eventos/`
 * Nombre de archivo: `<slogan>.webp` (el slogan es la clave de la imagen).
 * Ej: slogan="32_semana_nacional" -> `32_semana_nacional.webp`
 * aunque la URL real sea `.../32_semana_nacional_informacion.webp`.
 */
object EventosImagesCache {

    fun directory(context: Context): File =
        File(context.filesDir, "eventos")

    fun ensureDirectory(context: Context): File =
        directory(context).apply { if (!exists()) mkdirs() }

    fun fileNameFor(slogan: String, url: String): String {
        val s = slogan.trim()
        return if (s.isNotBlank()) "${s}.webp" else fileNameForUrl(url)
    }

    fun fileNameForUrl(url: String): String {
        val basename = url.substringAfterLast("/").substringBefore("?").trim()
        return basename.takeIf { it.isNotBlank() } ?: "evento.webp"
    }

    fun fileFor(context: Context, slogan: String, url: String): File =
        File(ensureDirectory(context), fileNameFor(slogan, url))

    fun fileForUrl(context: Context, url: String): File =
        File(ensureDirectory(context), fileNameForUrl(url))

    fun findLocalFile(context: Context, slogan: String, url: String): File? {
        val file = File(directory(context), fileNameFor(slogan, url))
        return file.takeIf { it.exists() && it.length() > 0L }
    }
}
