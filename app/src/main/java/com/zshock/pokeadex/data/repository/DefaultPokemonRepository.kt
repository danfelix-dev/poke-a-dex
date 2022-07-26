package com.zshock.pokeadex.data.repository

import com.zshock.pokeadex.data.db.PokemonDao
import com.zshock.pokeadex.data.db.PokemonDb
import com.zshock.pokeadex.data.model.PokemonPageResult
import com.zshock.pokeadex.data.model.pokemon.Pokemon
import com.zshock.pokeadex.data.model.species.Species
import com.zshock.pokeadex.data.network.API
import com.zshock.pokeadex.util.Constants.Companion.PAGE_SIZE
import javax.inject.Inject

class DefaultPokemonRepository @Inject constructor(
    private val dao: PokemonDao,
    private val api: API
) : PokemonRepository {

    /**
     * Checks for a [Pokemon] with the provided [id] and returns it.
     * If no resource is found, it's retrieved via a network call.
     */
    override suspend fun getPokemonDetail(id: String): Pokemon? {
        val cachedPokemon = dao.getByName(id)
        if (cachedPokemon.isNotEmpty()) {
            return cachedPokemon.first()
        } else {
            val networkPokemon = api.getPokemon(id).body()
            networkPokemon?.let { insert(it) }
            return networkPokemon
        }
    }

    /**
     * Returns the [Species] for the given [id] via a network call.
     */
    override suspend fun getSpecies(id: String): Species? {
        val species = api.getPokemonSpecies(id)
        return species.body()
    }

    /**
     * Returns a [PokemonPageResult] via a network call.
     * The provided (not required) [offset] is used for pagination purposes.
     */
    override suspend fun getPokemon(offset: Int) =
        api.getPokemon(offset, PAGE_SIZE)

    /**
     * Inserts the provided [pokemon] into the local database.
     */
    override suspend fun insert(pokemon: Pokemon) {
        dao.insert(pokemon)
    }
}