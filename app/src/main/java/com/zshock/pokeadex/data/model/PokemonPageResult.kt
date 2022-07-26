package com.zshock.pokeadex.data.model

import com.zshock.pokeadex.data.model.pokemon.Pokemon

/**
 * The object provided by the PokéAPI to solve pagination.
 * @param count Represents the total number of resources available for fetching.
 * @param next A convenience URL with the request for the next page.
 * @param previous A convenience URL with the assumed request for the previous page.
 * @param results The list of fetched resources.
 */
data class PokemonPageResult(
    val count: Int,
    val next: String? = null,
    val previous: String?,
    val results: MutableList<Pokemon>
)