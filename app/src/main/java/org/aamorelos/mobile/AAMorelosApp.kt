package org.aamorelos.mobile

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import org.aamorelos.mobile.data.cache.EventosFileFetcher
import org.aamorelos.mobile.data.cache.EventosFileKeyer
import org.aamorelos.mobile.data.cache.EventosImagesCache
import org.aamorelos.mobile.data.cache.ReflexionesFileFetcher
import org.aamorelos.mobile.data.cache.ReflexionesFileKeyer
import org.aamorelos.mobile.data.cache.ReflexionesImagesCache
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class AAMorelosApp : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        // Crear las carpetas de caché al iniciar, así existen en
        // files/reflexiones/ y files/eventos/ aunque todavía no se haya abierto la pantalla.
        ReflexionesImagesCache.ensureDirectory(this)
        EventosImagesCache.ensureDirectory(this)
    }

    override fun newImageLoader(): ImageLoader {
        val client = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        return ImageLoader.Builder(this)
            .components {
                add(ReflexionesFileFetcher.Factory(this@AAMorelosApp, client))
                add(ReflexionesFileKeyer())
                add(EventosFileFetcher.Factory(this@AAMorelosApp, client))
                add(EventosFileKeyer())
            }
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            // La caché de disco para reflexiones vive en filesDir/reflexiones/ (ver Fetcher).
            // Desactivamos la DiskCache interna de Coil para no duplicar con nombres hasheados.
            .diskCachePolicy(CachePolicy.DISABLED)
            .crossfade(true)
            .logger(DebugLogger())
            .build()
    }
}
