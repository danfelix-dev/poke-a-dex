package com.zshock.pokeadex.data.network

import com.zshock.pokeadex.data.model.PokemonPageResult
import com.zshock.pokeadex.data.model.pokemon.Pokemon
import com.zshock.pokeadex.data.model.species.Species
import com.zshock.pokeadex.util.Constants.Companion.PAGE_SIZE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface API {

    /**
     * Retrieves the details for the provided [Pokemon]'s [id].
     * The provided [id] can also be the [Pokemon.name] field.
     */
    @GET("v2/pokemon/{id}")
    suspend fun getPokemon(
        @Path("id") id: String? = null
    ): Response<Pokemon>

    /**
     * Retrieves a [PokemonPageResult] for the provided [offset].
     * [limit] defines up to how many results should be retrieved.
     */
    @GET("v2/pokemon")
    suspend fun getPokemon(
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = PAGE_SIZE,
    ): Response<PokemonPageResult>

    /**
     * Retrieves the [Species] for the provided [Pokemon]'s id.
     * The provided [id] can also be the [Pokemon.name] field.
     */
    @GET("v2/pokemon-species/{id}")
    suspend fun getPokemonSpecies(
        @Path("id") id: String
    ): Response<Species>

}