package com.zshock.pokeadex.di

import android.content.Context
import androidx.room.Room
import com.zshock.pokeadex.data.db.PokemonDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("test_db")
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, PokemonDb::class.java)
            .allowMainThreadQueries()
            .build()
}