package com.zshock.pokeadex.data.repository

import com.zshock.pokeadex.data.model.PokemonPageResult
import com.zshock.pokeadex.data.model.pokemon.Pokemon
import com.zshock.pokeadex.data.model.species.FlavorText
import com.zshock.pokeadex.data.model.species.Language
import com.zshock.pokeadex.data.model.species.Species
import retrofit2.Response

class FakePokemonRepository : PokemonRepository {

    private val localPokemonItems = mutableListOf<Pokemon>()
    private val remotePokemonItems = mutableListOf<Pokemon>()
    private val remotePokemonSpeciesItems = mutableListOf<Species>()
    private var shouldReturnNetworkError = false

    fun toggleNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    init {
        remotePokemonItems.add(Pokemon(1, "bulbasaur"))
        remotePokemonItems.add(Pokemon(2, "ivysaur"))
        remotePokemonItems.add(Pokemon(3, "venusaur"))
        remotePokemonItems.add(Pokemon(4, "charmander"))
        remotePokemonItems.add(Pokemon(5, "charmeleon"))
        remotePokemonItems.add(Pokemon(6, "charizar"))
        remotePokemonItems.add(Pokemon(7, "squirtle"))
        remotePokemonItems.add(Pokemon(8, "wartortle"))
        remotePokemonItems.add(Pokemon(9, "blastoise"))
        for (i in 0 until remotePokemonItems.size) {
            val pokemon = remotePokemonItems[i]
            remotePokemonSpeciesItems.add(
                Species(
                    pokemon.id,
                    listOf(
                        FlavorText(
                            "Random description for ${pokemon.name?.uppercase()}",
                            Language("en")
                        )
                    )
                )
            )
        }

    }

    override suspend fun getPokemon(offset: Int): Response<PokemonPageResult> {
        return Response.success(
            PokemonPageResult(
                remotePokemonItems.size,
                null,
                null,
                remotePokemonItems.subList(offset, remotePokemonItems.size - offset)
            )
        )
    }

    override suspend fun getPokemonDetail(id: String): Pokemon? {
        return remotePokemonItems.find { it.id.toString() == id }
    }

    override suspend fun insert(pokemon: Pokemon) {
        val filteredPokemonList = localPokemonItems.filter { it.name == pokemon.name }
        if (filteredPokemonList.isNotEmpty()) {
            localPokemonItems[localPokemonItems.indexOf(filteredPokemonList.first())] = pokemon
        } else {
            localPokemonItems.add(pokemon)
        }
    }

    override suspend fun getSpecies(id: String): Species? {
        return remotePokemonSpeciesItems.find { it.id.toString() == id }
    }
}