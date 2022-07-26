package com.zshock.pokeadex.data.model.species

import androidx.room.Entity

@Entity
data class Species(
    val id: Int?,
    var flavorTextEntries: List<FlavorText>? = null
)

/**
 * Convenience method that removes non-english flavor texts and line breaks for the [Species].
 * It also does its best to remove duplicates but the dev decided to spend their time in
 * other parts of the code instead (sorry!).
 */
fun Species.filtered(): Species {
    flavorTextEntries?.let { flavorTextEntries ->
        var filteredEntries = flavorTextEntries
            .filter { it.language.name == "en" }

        val iterator = filteredEntries.iterator()
        while (iterator.hasNext()) {
            val flavorTextEntry = iterator.next()
            flavorTextEntry.flavorText = flavorTextEntry.flavorText.replace("\n", " ")
        }

        filteredEntries = filteredEntries
            .distinctBy { it.flavorText }
            .toMutableList()

        this.flavorTextEntries = filteredEntries
    }
    return this
}