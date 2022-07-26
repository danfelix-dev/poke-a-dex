package com.zshock.pokeadex.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zshock.pokeadex.data.model.pokemon.Pokemon
import com.zshock.pokeadex.data.model.species.filtered
import com.zshock.pokeadex.data.repository.DefaultPokemonRepository
import com.zshock.pokeadex.util.Resource
import kotlinx.coroutines.launch

class PokemonDetailViewModel(repository: DefaultPokemonRepository, pokemonId: Int) : ViewModel() {

    val pokemon: MutableLiveData<Resource<Pokemon>> = MutableLiveData(Resource.Loading())
    var isConnectionActive: Boolean? = null

    init {
        viewModelScope.launch {
            getPokemon(repository, pokemonId)
        }
    }

    private suspend fun getPokemon(
        repository: DefaultPokemonRepository,
        pokemonId: Int
    ) {
        val hasConnection = isConnectionActive
        if (hasConnection != null && !hasConnection) {
            pokemon.postValue(Resource.Error("No internet connection"))
        } else {
            try {
                val fetchedPokemon = repository.getPokemonDetail(pokemonId.toString())
                if (fetchedPokemon != null) {
                    val species = repository.getSpecies(pokemonId.toString())
                    fetchedPokemon.species = species?.filtered()
                    pokemon.postValue(Resource.Success(fetchedPokemon))
                } else {
                    pokemon.postValue(Resource.Error("Unexpected error"))
                }
            } catch (e: Exception) {
                pokemon.postValue(Resource.Error("Unexpected error"))
            }
        }
    }

    fun isConnectionActive(value: Boolean) {
        this.isConnectionActive = value
    }

}
