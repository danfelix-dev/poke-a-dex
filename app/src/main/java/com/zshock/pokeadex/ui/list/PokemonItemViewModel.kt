package com.zshock.pokeadex.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zshock.pokeadex.data.model.pokemon.Pokemon
import com.zshock.pokeadex.data.repository.DefaultPokemonRepository
import com.zshock.pokeadex.util.Resource
import kotlinx.coroutines.launch

class PokemonItemViewModel(
    item: Pokemon?,
    private val repository: DefaultPokemonRepository
) : ViewModel() {

    val pokemon: MutableLiveData<Resource<Pokemon?>> = MutableLiveData(Resource.Success(item))

    init {
        if (item != null) {
            update(item)
        }
    }

    private fun update(newItem: Pokemon?) {
        pokemon.value = Resource.Success(newItem)
        if (newItem?.id == null && newItem?.name != null) {
            viewModelScope.launch {
                try {
                    val fullPokemonItem = repository.getPokemonDetail(newItem.name!!)
                    val shouldUpdateValue = pokemon.value?.data?.name == fullPokemonItem?.name
                    if (shouldUpdateValue) {
                        pokemon.value = Resource.Success(fullPokemonItem)
                        pokemon.postValue(pokemon.value)
                    }
                } catch (e: Exception) {
                    pokemon.postValue(Resource.Error("No internet connection"))
                }
            }
        } else {
            pokemon.postValue(Resource.Success(newItem))
        }
    }

    fun setPokemon(newPokemon: Pokemon?) {
        update(newPokemon)
    }

}
