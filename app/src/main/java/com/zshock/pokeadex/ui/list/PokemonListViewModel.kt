package com.zshock.pokeadex.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zshock.pokeadex.data.model.PokemonPageResult
import com.zshock.pokeadex.data.model.pokemon.Pokemon
import com.zshock.pokeadex.data.repository.PokemonRepository
import com.zshock.pokeadex.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class PokemonListViewModel(private val repository: PokemonRepository) : ViewModel() {

    var pokemon: MutableLiveData<Resource<MutableList<Pokemon>>> = MutableLiveData()
    var lastResponse: Resource<MutableList<Pokemon>>? = null
    var currentOffset = 0
    var pendingResults = MutableLiveData<Boolean?>(null)
    var isConnectionActive: Boolean? = null

    fun getPokemon() = viewModelScope.launch {
        val hasConnection = isConnectionActive
        if (hasConnection != null && !hasConnection) {
            pokemon.postValue(Resource.Error("No internet connection"))
        } else {
            try {
                pokemon.postValue(Resource.Loading())
                val pokemonPageResult = repository.getPokemon(currentOffset)
                val fusedResponse = handleResponse(pokemonPageResult)
                pokemon.postValue(fusedResponse)
            } catch (e: Exception) {
                pokemon.postValue(e.localizedMessage?.let { Resource.Error(it) })
            }
        }
    }


    private fun handleResponse(response: Response<PokemonPageResult>): Resource<MutableList<Pokemon>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                currentOffset += resultResponse.results.count()
                if (currentOffset == resultResponse.count) {
                    pendingResults.postValue(false)
                }
                if (lastResponse == null) {
                    val mutableLiveData = resultResponse.results.toMutableList()
                    lastResponse = Resource.Success(mutableLiveData)
                } else {
                    lastResponse?.data?.addAll(resultResponse.results.toMutableList())
                }

                lastResponse?.let { return (it) }
            }
        }
        return Resource.Error(response.message())
    }

    fun isConnectionActive(value: Boolean) {
        this.isConnectionActive = value
    }

}