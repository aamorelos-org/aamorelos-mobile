package org.aamorelos.mobile.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.aamorelos.mobile.data.local.AAMorelosDatabase
import org.aamorelos.mobile.data.local.EventoEntity
import org.aamorelos.mobile.data.remote.AaMorelosApi
import org.aamorelos.mobile.data.remote.toModel
import org.aamorelos.mobile.domain.model.EventoItemModel

/**
 * PagingSource personalizado para eventos con caché SQLite.
 *
 * - Estrategia principal: consume la API (`skip`/`limit`), guarda la página
 *   en SQLite y entrega los datos.
 * - Estrategia fallback: ante cualquier fallo de red lee directamente desde
 *   SQLite con `LIMIT`/`OFFSET`.
 *
 * La clave del Paging es el offset (`skip`). El `limit` siempre es el
 * tamaño de página (p. ej. 20), nunca acumulado: pag. 2 es skip=20&limit=20.
 */
class EventosPagingSource(
    private val api: AaMorelosApi,
    private val database: AAMorelosDatabase
) : PagingSource<Int, EventoItemModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EventoItemModel> {
        val offset = params.key ?: STARTING_OFFSET
        val pageSize = params.loadSize
        return try {
            val response = withContext(Dispatchers.IO) {
                api.getEventos(skip = offset, limit = pageSize)
            }
            val items = response.data.map { it.toModel() }
            try {
                withContext(Dispatchers.IO) {
                    database.saveEventos(
                        items.mapIndexed { index, model ->
                            EventoEntity(
                                id = model.id,
                                tipo = model.tipo,
                                slogan = model.slogan,
                                url = model.url,
                                titulo = model.titulo,
                                subtitulo = model.subtitulo,
                                imagen = model.imagen,
                                alt = model.alt,
                                pie = model.pie,
                                position = offset + index,
                                ultimaActualizacion = System.currentTimeMillis()
                            )
                        }
                    )
                }
            } catch (cacheError: Exception) {
                android.util.Log.w("EventosPagingSource", "No se pudo cachear página offset=$offset: ${cacheError.message}")
            }

            val endReached = items.size < pageSize ||
                (response.totalRegistros != null && (offset + items.size) >= response.totalRegistros)
            val nextKey = if (endReached) null else offset + items.size
            LoadResult.Page(
                data = items,
                prevKey = if (offset == STARTING_OFFSET) null else offset - pageSize,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            try {
                val cached = withContext(Dispatchers.IO) {
                    database.getEventos(limit = pageSize, offset = offset)
                }
                if (cached.isEmpty()) {
                    LoadResult.Error(e)
                } else {
                    val endReached = cached.size < pageSize
                    LoadResult.Page(
                        data = cached,
                        prevKey = if (offset == STARTING_OFFSET) null else offset - pageSize,
                        nextKey = if (endReached) null else offset + pageSize
                    )
                }
            } catch (dbError: Exception) {
                LoadResult.Error(dbError)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, EventoItemModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(pageSize(state))
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(pageSize(state))
        }
    }

    private fun pageSize(state: PagingState<Int, EventoItemModel>): Int =
        state.config.pageSize

    companion object {
        const val STARTING_OFFSET = 0
    }
}
