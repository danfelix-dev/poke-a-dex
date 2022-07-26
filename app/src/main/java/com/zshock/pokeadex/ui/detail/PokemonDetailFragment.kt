package com.zshock.pokeadex.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.zshock.pokeadex.data.db.PokemonDb
import com.zshock.pokeadex.data.model.pokemon.Pokemon
import com.zshock.pokeadex.data.model.pokemon.getFlavorTexts
import com.zshock.pokeadex.data.model.pokemon.getNameString
import com.zshock.pokeadex.data.model.pokemon.getTypesString
import com.zshock.pokeadex.data.network.Network
import com.zshock.pokeadex.data.network.NetworkLiveData
import com.zshock.pokeadex.data.repository.DefaultPokemonRepository
import com.zshock.pokeadex.databinding.FragmentPokemonDetailBinding
import com.zshock.pokeadex.util.Resource

class PokemonDetailFragment : Fragment() {

    private val viewModel: PokemonDetailViewModel by viewModels()
    private val args: PokemonDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentPokemonDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)

        setupObservables()

        return binding.root
    }

    private fun setupObservables() {
        NetworkLiveData.observe(viewLifecycleOwner) {
            viewModel.isConnectionActive(it)
        }

        viewModel.pokemon.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    resource.data?.let { update(it) }
                }
            }
        }
    }

    private fun update(pokemon: Pokemon) {
        binding.apply {
            nameTextView.text = pokemon.getNameString()
            numberTextView.text = if (pokemon.id != null) "#${pokemon.id}" else ""
            typeTextView.text = pokemon.getTypesString()
            descriptionTextView.text = pokemon.getFlavorTexts()?.joinToString(separator = "\n\n")
            Glide.with(root).load(pokemon.sprites?.front_default).into(thumbnailImageView)
        }
    }

    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory {
        return PokemonDetailViewModelFactory(
            DefaultPokemonRepository(
                PokemonDb.invoke(requireContext()).pokemonDao(),
                Network.api
            ),
            args.pokemonId
        )
    }
}