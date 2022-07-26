package com.zshock.pokeadex.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zshock.pokeadex.data.repository.DefaultPokemonRepository

@Suppress("UNCHECKED_CAST")
class PokemonDetailViewModelFactory(
    private val repository: DefaultPokemonRepository,
    private val pokemonId: Int
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PokemonDetailViewModel(repository, pokemonId) as T
    }
}