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

import androidx.room.*
import com.zshock.pokeadex.data.model.pokemon.Pokemon

@Dao
interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemon: List<Pokemon>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokemon: Pokemon)

    @Query("SELECT * FROM pokemon")
    suspend fun getAll(): List<Pokemon>

    @Query("SELECT * FROM pokemon WHERE name = :name")
    suspend fun getByName(name: String): List<Pokemon>

    @Query("SELECT * FROM pokemon WHERE id = :id")
    suspend fun getById(id: String): List<Pokemon>

    @Delete
    suspend fun delete(pokemon: Pokemon)

    @Query("DELETE FROM pokemon")
    suspend fun clearAll()
}
