package org.aamorelos.mobile.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.aamorelos.mobile.domain.ports.GruposRepositoryPort
import org.aamorelos.mobile.domain.ports.GrupoDetailRepositoryPort
import org.aamorelos.mobile.domain.ports.DistritosRepositoryPort
import org.aamorelos.mobile.domain.ports.DistritoDetailRepositoryPort
import org.aamorelos.mobile.domain.ports.ReflexionesRepositoryPort
import org.aamorelos.mobile.domain.ports.ReflexionDetailRepositoryPort
import org.aamorelos.mobile.domain.ports.ReflexionDiariaRepositoryPort
import org.aamorelos.mobile.domain.ports.EventosRepositoryPort
import org.aamorelos.mobile.domain.ports.EventoDetailRepositoryPort
import org.aamorelos.mobile.domain.ports.DirectorioRepositoryPort
import org.aamorelos.mobile.infrastructure.adapters.outbound.CachedDistritosRepository
import org.aamorelos.mobile.infrastructure.adapters.outbound.CachedGruposRepository
import org.aamorelos.mobile.infrastructure.adapters.outbound.DistritoDetailRepositoryAdapter
import org.aamorelos.mobile.infrastructure.adapters.outbound.GrupoDetailRepositoryAdapter
import org.aamorelos.mobile.infrastructure.adapters.outbound.ReflexionesRepositoryAdapter
import org.aamorelos.mobile.infrastructure.adapters.outbound.ReflexionDetailRepositoryAdapter
import org.aamorelos.mobile.infrastructure.adapters.outbound.ReflexionDiariaRepositoryAdapter
import org.aamorelos.mobile.infrastructure.adapters.outbound.EventosRepositoryAdapter
import org.aamorelos.mobile.infrastructure.adapters.outbound.EventoDetailRepositoryAdapter
import org.aamorelos.mobile.infrastructure.adapters.outbound.DirectorioRepositoryAdapter

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @javax.inject.Singleton
    abstract fun bindGruposRepository(
        cachedRepository: CachedGruposRepository
    ): GruposRepositoryPort

    @Binds
    @javax.inject.Singleton
    abstract fun bindDistritosRepository(
        cachedDistritosRepository: CachedDistritosRepository
    ): DistritosRepositoryPort

    @Binds
    @javax.inject.Singleton
    abstract fun bindGrupoDetailRepository(
        adapter: GrupoDetailRepositoryAdapter
    ): GrupoDetailRepositoryPort

    @Binds
    @javax.inject.Singleton
    abstract fun bindDistritoDetailRepository(
        adapter: DistritoDetailRepositoryAdapter
    ): DistritoDetailRepositoryPort

    @Binds
    @javax.inject.Singleton
    abstract fun bindReflexionesRepository(
        adapter: ReflexionesRepositoryAdapter
    ): ReflexionesRepositoryPort

    @Binds
    @javax.inject.Singleton
    abstract fun bindReflexionDetailRepository(
        adapter: ReflexionDetailRepositoryAdapter
    ): ReflexionDetailRepositoryPort

    @Binds
    @javax.inject.Singleton
    abstract fun bindReflexionDiariaRepository(
        adapter: ReflexionDiariaRepositoryAdapter
    ): ReflexionDiariaRepositoryPort

    @Binds
    @javax.inject.Singleton
    abstract fun bindEventosRepository(
        adapter: EventosRepositoryAdapter
    ): EventosRepositoryPort

    @Binds
    @javax.inject.Singleton
    abstract fun bindEventoDetailRepository(
        adapter: EventoDetailRepositoryAdapter
    ): EventoDetailRepositoryPort

    @Binds
    @javax.inject.Singleton
    abstract fun bindDirectorioRepository(
        adapter: DirectorioRepositoryAdapter
    ): DirectorioRepositoryPort
}
