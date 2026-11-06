package org.aamorelos.mobile.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.aamorelos.mobile.data.local.AAMorelosDatabase

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @javax.inject.Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AAMorelosDatabase = AAMorelosDatabase(context)
}
