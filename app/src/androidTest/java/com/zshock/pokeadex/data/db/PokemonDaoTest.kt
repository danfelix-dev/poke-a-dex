package com.zshock.pokeadex.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.zshock.pokeadex.data.model.pokemon.Pokemon
import com.zshock.pokeadex.data.model.pokemon.Type
import com.zshock.pokeadex.data.model.pokemon.TypeX
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class PokemonDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: PokemonDb

    private lateinit var dao: PokemonDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.pokemonDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertPokemon() = runTest {
        val pokemon = Pokemon(id = 1, name = "bulbasaur")
        dao.insert(pokemon)
        val pokemonByNameQuery = dao.getByName("bulbasaur")
        assertThat(pokemonByNameQuery).contains(pokemon)
    }

    @Test
    fun replacePokemon() = runTest {
        val pokemon = Pokemon(id = 1, name = "bulbasaur")
        dao.insert(pokemon)
        pokemon.types = listOf(Type(0, TypeX("grass", "")))
        dao.insert(pokemon)
        val retrievedPokemon = dao.getByName("bulbasaur")
        assertThat(retrievedPokemon.size).isEqualTo(1)
        assertThat(retrievedPokemon.first().types).isEqualTo(pokemon.types)
    }
}













