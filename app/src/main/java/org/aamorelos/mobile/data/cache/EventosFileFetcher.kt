package org.aamorelos.mobile.data.cache

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import coil.decode.DataSource
import coil.fetch.DrawableResult
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.key.Keyer
import coil.request.Options
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Modelo propio para las imágenes de eventos.
 * Usar un tipo distinto a String/Uri es clave: Coil mapea String -> Uri
 * antes de elegir Fetcher y el HttpUriFetcher por defecto ganaría.
 */
data class EventoImage(
    val url: String,
    val slogan: String
)

/**
 * Keyer: la clave es el nombre del archivo ("slogan.webp").
 */
class EventosFileKeyer : Keyer<EventoImage> {
    override fun key(data: EventoImage, options: Options): String? {
        val name = EventosImagesCache.fileNameFor(data.slogan, data.url)
        return name.takeIf { it.isNotBlank() } ?: data.url
    }
}

/**
 * Fetcher propio para eventos con caché en disco en `eventos/`.
 *
 * - Si `filesDir/eventos/<slogan>.webp` existe -> sirve desde DISK (sin red).
 * - Si no existe -> descarga con OkHttp, guarda como `<slogan>.webp` y sirve.
 */
class EventosFileFetcher(
    private val image: EventoImage,
    private val targetFile: File,
    private val client: OkHttpClient,
    private val options: Options
) : Fetcher {

    override suspend fun fetch(): FetchResult? {
        if (targetFile.exists() && targetFile.length() > 0L) {
            val bitmap = withContext(Dispatchers.IO) {
                BitmapFactory.decodeFile(targetFile.absolutePath)
            } ?: return null
            val drawable = BitmapDrawable(options.context.resources, bitmap)
            return DrawableResult(drawable, isSampled = false, dataSource = DataSource.DISK)
        }
        return withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder().url(image.url).build()
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) return@withContext null
                    val bytes = response.body.bytes()
                    if (bytes.isEmpty()) return@withContext null
                    targetFile.parentFile?.mkdirs()
                    val tmp = File(targetFile.parentFile, "tmp_${targetFile.name}")
                    tmp.writeBytes(bytes)
                    if (!tmp.renameTo(targetFile)) {
                        tmp.copyTo(targetFile, overwrite = true)
                        tmp.delete()
                    }
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: return@withContext null
                    val drawable = BitmapDrawable(options.context.resources, bitmap)
                    DrawableResult(drawable, isSampled = false, dataSource = DataSource.NETWORK)
                }
            } catch (e: Exception) {
                android.util.Log.w("EventosFileFetcher", "Falló descarga ${image.url}: ${e.message}")
                null
            }
        }
    }

    class Factory(
        private val context: Context,
        private val client: OkHttpClient = defaultClient()
    ) : Fetcher.Factory<EventoImage> {

        override fun create(
            data: EventoImage,
            options: Options,
            imageLoader: coil.ImageLoader
        ): Fetcher? {
            if (data.url.isBlank()) return null
            val targetFile = EventosImagesCache.fileFor(context, data.slogan, data.url)
            return EventosFileFetcher(
                image = data,
                targetFile = targetFile,
                client = client,
                options = options
            )
        }

        companion object {
            private fun defaultClient(): OkHttpClient =
                OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build()
        }
    }
}
