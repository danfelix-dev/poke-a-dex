package com.zshock.pokeadex.ui.list

import MainDispatcherRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.zshock.pokeadex.data.repository.FakePokemonRepository
import com.zshock.pokeadex.util.getOrAwaitValueTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PokemonListViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: PokemonListViewModel

    @Before
    fun setup() {
        viewModel = PokemonListViewModel(FakePokemonRepository())
    }

    @Test
    fun `getPokemon should return list of Pokemon`() {
        viewModel.getPokemon()
        val value = viewModel.pokemon.getOrAwaitValueTest()
        assertThat(value.data).isNotNull()
        assertThat(value.data).isNotEmpty()
    }
}