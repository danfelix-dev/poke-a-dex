package com.zshock.pokeadex.data.model.species

/**
 * The Pokédex entry object provided by the PokéAPI.
 * It comes with the [flavorText] itself and its [language].
 */
data class FlavorText(
    var flavorText: String,
    val language: Language
)
