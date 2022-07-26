package com.zshock.pokeadex.data.repository

import com.zshock.pokeadex.data.model.PokemonPageResult
import com.zshock.pokeadex.data.model.pokemon.Pokemon
import com.zshock.pokeadex.data.model.species.Species
import retrofit2.Response

/**
 * Representation of a local/remote library of Pokémon
 */
interface PokemonRepository {
    suspend fun getPokemonDetail(id: String): Pokemon?
    suspend fun getSpecies(id: String): Species?
    suspend fun getPokemon(offset: Int): Response<PokemonPageResult>
    suspend fun insert(pokemon: Pokemon)
}