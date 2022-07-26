/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zshock.pokeadex.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zshock.pokeadex.data.model.pokemon.Pokemon

/**
 * Database schema used by the DbRedditPostRepository
 */
@Database(
    entities = [Pokemon::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PokemonDb : RoomDatabase() {

    companion object {
        @Volatile
        private var instance: PokemonDb? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                PokemonDb::class.java,
                "pokemonDb.db"
            ).build()
    }

    abstract fun pokemonDao(): PokemonDao
}