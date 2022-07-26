package com.zshock.pokeadex.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zshock.pokeadex.data.db.PokemonDb
import com.zshock.pokeadex.data.model.pokemon.Pokemon
import com.zshock.pokeadex.data.network.Network
import com.zshock.pokeadex.data.network.NetworkLiveData
import com.zshock.pokeadex.data.repository.DefaultPokemonRepository
import com.zshock.pokeadex.databinding.FragmentPokemonListBinding
import com.zshock.pokeadex.util.Constants.Companion.PAGE_SIZE
import com.zshock.pokeadex.util.Resource

class PokemonListFragment : Fragment() {

    private val viewModel: PokemonListViewModel by viewModels()
    private lateinit var binding: FragmentPokemonListBinding
    private lateinit var adapter: PokemonListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonListBinding.inflate(inflater, container, false)

        setupObservables()
        setupViews()

        viewModel.getPokemon()

        return binding.root
    }

    private fun setupObservables() {
        NetworkLiveData.observe(viewLifecycleOwner) {
            viewModel.isConnectionActive(it)
        }

        viewModel.pokemon.observe(viewLifecycleOwner) { newList ->
            when (newList) {
                is Resource.Error -> {
                    binding.noInternetView.root.visibility = View.VISIBLE
                }
                is Resource.Loading -> {}
                is Resource.Success -> {
                    binding.noInternetView.root.visibility = View.INVISIBLE
                    adapter.differ.submitList(newList.data?.toMutableList())
                }
            }
        }

        viewModel.pendingResults.observe(viewLifecycleOwner) {
            if (it == false) {
                areThereMoreResults = false
            }
        }
    }

    private fun setupViews() {
        binding.noInternetView.retryButton.setOnClickListener {
            binding.noInternetView.root.visibility = View.INVISIBLE
            viewModel.getPokemon()
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = PokemonListAdapter(
            DefaultPokemonRepository(
                PokemonDb.invoke(requireContext()).pokemonDao(),
                Network.api
            )
        )
        adapter.onItemClickListener = object : (Pokemon?) -> Unit {
            override fun invoke(pokemon: Pokemon?) {
                pokemon?.id?.let { id ->
                    val action =
                        PokemonListFragmentDirections.actionPokemonListFragmentToPokemonDetailFragment()
                    action.pokemonId = id
                    findNavController().navigate(action)
                }

            }

        }
        binding.recyclerView.apply {
            adapter = this@PokemonListFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(scrollListener)
        }
    }

    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    var areThereMoreResults = true

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= PAGE_SIZE
            val shouldPaginate = isNoErrors &&
                    isNotLoadingAndNotLastPage &&
                    isAtLastItem &&
                    isNotAtBeginning &&
                    isTotalMoreThanVisible &&
                    areThereMoreResults &&
                    isScrolling
            if (shouldPaginate) {
                viewModel.getPokemon()
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory {
        return PokemonListViewModelFactory(
            DefaultPokemonRepository(
                PokemonDb.invoke(requireContext()).pokemonDao(),
                Network.api
            )
        )
    }

}