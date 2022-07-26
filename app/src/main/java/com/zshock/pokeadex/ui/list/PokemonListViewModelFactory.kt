package com.zshock.pokeadex.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zshock.pokeadex.data.repository.DefaultPokemonRepository

@Suppress("UNCHECKED_CAST")
class PokemonListViewModelFactory(
    private val repository: DefaultPokemonRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PokemonListViewModel(repository) as T
    }
}