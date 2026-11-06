package org.aamorelos.mobile.data.cache

import android.content.Context
import java.io.File

/**
 * Helper central para la caché de imágenes de reflexiones en disco.
 *
 * Carpeta persistente: `filesDir/reflexiones/`
 * Nombre de archivo: basename de `imagen` (coincide con `slogan.webp`),
 * ej: `https://.../bebiendo_demasiado.webp` -> `bebiendo_demasiado.webp`
 */
object ReflexionesImagesCache {

    fun directory(context: Context): File =
        File(context.filesDir, "reflexiones")

    fun ensureDirectory(context: Context): File =
        directory(context).apply { if (!exists()) mkdirs() }

    fun fileNameForUrl(url: String): String {
        val basename = url.substringAfterLast("/").substringBefore("?").trim()
        return basename.takeIf { it.isNotBlank() } ?: "reflexion.webp"
    }

    fun fileNameFor(slogan: String, url: String): String {
        // slogan siempre genera "<slogan>.webp"; si viene vacío, fallback al basename de la URL
        return "${slogan.trim()}.webp".takeIf { slogan.isNotBlank() }
            ?: fileNameForUrl(url)
    }

    fun fileForUrl(context: Context, url: String): File =
        File(ensureDirectory(context), fileNameForUrl(url))

    fun fileFor(context: Context, slogan: String, url: String): File =
        File(ensureDirectory(context), fileNameFor(slogan, url))

    fun findLocalFile(context: Context, url: String): File? {
        val file = File(directory(context), fileNameForUrl(url))
        return file.takeIf { it.exists() && it.length() > 0L }
    }
}
