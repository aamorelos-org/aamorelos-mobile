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
 * Modelo propio para las imágenes de reflexiones.
 * Usar un tipo distinto a String/Uri es clave: Coil mapea String -> Uri
 * antes de elegir Fetcher y el HttpUriFetcher por defecto ganaría.
 * Al pasar [ReflexionImage], nuestro Fetcher es el único que puede
 * manejarlo y la prioridad queda garantizada.
 */
data class ReflexionImage(
    val url: String,
    val slogan: String
)

/**
 * Keyer: la clave es el nombre del archivo ("slogan.webp").
 */
class ReflexionesFileKeyer : Keyer<ReflexionImage> {
    override fun key(data: ReflexionImage, options: Options): String? {
        val name = ReflexionesImagesCache.fileNameFor(data.slogan, data.url)
        return name.takeIf { it.isNotBlank() } ?: data.url
    }
}

/**
 * Fetcher propio para reflexiones con caché en disco en `reflexiones/`.
 *
 * - Si `filesDir/reflexiones/<slogan>.webp` existe -> sirve desde DISK (sin red).
 * - Si no existe -> descarga con OkHttp, guarda como `<slogan>.webp` y sirve.
 */
class ReflexionesFileFetcher(
    private val image: ReflexionImage,
    private val targetFile: File,
    private val client: OkHttpClient,
    private val options: Options
) : Fetcher {

    override suspend fun fetch(): FetchResult? {
        // Fast path: ya existe en disco
        if (targetFile.exists() && targetFile.length() > 0L) {
            val bitmap = withContext(Dispatchers.IO) {
                BitmapFactory.decodeFile(targetFile.absolutePath)
            } ?: return null
            val drawable = BitmapDrawable(options.context.resources, bitmap)
            return DrawableResult(drawable, isSampled = false, dataSource = DataSource.DISK)
        }
        // Slow path: descargar y guardar
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
                android.util.Log.w("ReflexionesFileFetcher", "Falló descarga ${image.url}: ${e.message}")
                null
            }
        }
    }

    /**
     * Factoría registrada en el ComponentRegistry. Solo maneja [ReflexionImage].
     */
    class Factory(
        private val context: Context,
        private val client: OkHttpClient = defaultClient()
    ) : Fetcher.Factory<ReflexionImage> {

        override fun create(
            data: ReflexionImage,
            options: Options,
            imageLoader: coil.ImageLoader
        ): Fetcher? {
            if (data.url.isBlank()) return null
            val targetFile = ReflexionesImagesCache.fileFor(context, data.slogan, data.url)
            return ReflexionesFileFetcher(
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
