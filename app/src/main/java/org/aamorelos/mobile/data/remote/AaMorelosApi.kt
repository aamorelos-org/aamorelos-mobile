package org.aamorelos.mobile.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface AaMorelosApi {

    @GET("estados/{estadoId}/grupos")
    suspend fun getGrupos(
        @Path("estadoId") estadoId: String = "17"
    ): GruposResponse

    @GET("estados/{estadoId}/grupos/{grupoId}")
    suspend fun getGrupoDetail(
        @Path("estadoId") estadoId: String = "17",
        @Path("grupoId") grupoId: String
    ): GrupoDetailDto

    @GET("estados/{estadoId}/distritos")
    suspend fun getDistritos(
        @Path("estadoId") estadoId: String = "17"
    ): DistritosResponse

    @GET("distritos/{distritoId}")
    suspend fun getDistritoDetail(
        @Path("distritoId") distritoId: String
    ): DistritoDetailDto

    @GET("reflexiones-diarias/")
    suspend fun getReflexionDiaria(
        @retrofit2.http.Query("fecha") fecha: String
    ): ReflexionDiariaResponse

    @GET("reflexiones/")
    suspend fun getReflexiones(
        @retrofit2.http.Query("skip") skip: Int = 0,
        @retrofit2.http.Query("limit") limit: Int = 20
    ): ReflexionesResponse

    @GET("reflexiones/{slogan}")
    suspend fun getReflexionDetail(
        @Path("slogan") slogan: String
    ): ReflexionDetailResponse

    @GET("directorio/")
    suspend fun getDirectorio(
        @retrofit2.http.Query("skip") skip: Int = 0,
        @retrofit2.http.Query("limit") limit: Int = 20
    ): DirectorioResponse

    @GET("eventos/")
    suspend fun getEventos(
        @retrofit2.http.Query("skip") skip: Int = 0,
        @retrofit2.http.Query("limit") limit: Int = 20
    ): EventosResponse

    @GET("eventos/{id}")
    suspend fun getEventoDetail(
        @Path("id") id: Int
    ): EventoDetailResponse
}
