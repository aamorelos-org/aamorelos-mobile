package org.aamorelos.mobile.data.cache

import android.content.Context
import java.io.File

/**
 * Helper central para la caché de imágenes del directorio en disco.
 *
 * Carpeta persistente: `filesDir/directorio/`
 * Nombre de archivo: `<key>.webp` (la clave es `DirectorioModel.imageKey`,
 * ej: `pagina_1_2026_septiembre`).
 * Ej: `pagina_1_2026_septiembre.webp`
 * aunque la URL real sea `.../directorio/1.webp`.
 */
object DirectorioImagesCache {

    fun directory(context: Context): File =
        File(context.filesDir, "directorio")

    fun ensureDirectory(context: Context): File =
        directory(context).apply { if (!exists()) mkdirs() }

    fun fileNameFor(slogan: String, url: String): String {
        val s = slogan.trim()
        return if (s.isNotBlank()) "${s}.webp" else fileNameForUrl(url)
    }

    fun fileNameForUrl(url: String): String {
        val basename = url.substringAfterLast("/").substringBefore("?").trim()
        return basename.takeIf { it.isNotBlank() } ?: "directorio.webp"
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
