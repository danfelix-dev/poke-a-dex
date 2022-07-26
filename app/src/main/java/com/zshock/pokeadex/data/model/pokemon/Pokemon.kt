package com.zshock.pokeadex.data.model.pokemon

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zshock.pokeadex.data.model.species.Species

/**
 * Represents a Pokémon.
 * @property id The unique identifier provided by the PokéApi.
 * @property name The name of the Pokémon (it can
 * also contain other modifiers like mega evolutions and regions).
 * @property url The URL for the full detail of the Pokémon.
 * @property sprites Contains URLs for different image representation of the Pokémon.
 * @property types A list of the types that the Pokémon conforms.
 * @property species Contains Pokédex entries from different Pokémon games.
 */
@Entity
data class Pokemon(
    @PrimaryKey
    var id: Int? = null,
    var name: String? = null,
    var url: String? = null,
    var sprites: Sprites? = null,
    var types: List<Type>? = null,
    var species: Species? = null
) {

    override fun toString(): String {
        if (id != null && name != null) {
            return "$id: $name"
        }
        return super.toString()
    }
}

// Convenience extension methods

/**
 * Returns the types for the [Pokemon] on a single [String].
 */
fun Pokemon.getTypesString(): String {
    if (types != null) {
        return types!!.joinToString(separator = " / ") { it.type.name.replaceFirstChar { it.uppercase() } }
    } else {
        return "Unknown"
    }
}

/**
 * Returns the capitalized name for the [Pokemon].
 */
fun Pokemon.getNameString(): String? {
    return name?.replaceFirstChar { it.uppercase() }
}

/**
 * Returns all the flavor texts found for the [Pokemon] as a list of [String].
 */
fun Pokemon.getFlavorTexts(): List<String>? {
    return species?.flavorTextEntries?.map { it.flavorText }
}