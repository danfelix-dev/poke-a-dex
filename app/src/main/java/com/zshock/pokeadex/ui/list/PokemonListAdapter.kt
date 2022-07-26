package com.zshock.pokeadex.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zshock.pokeadex.data.model.pokemon.Pokemon
import com.zshock.pokeadex.data.repository.DefaultPokemonRepository
import com.zshock.pokeadex.databinding.ItemPokemonBinding
import com.zshock.pokeadex.util.Resource

class PokemonListAdapter(private val repository: DefaultPokemonRepository) :
    RecyclerView.Adapter<PokemonListAdapter.PokemonViewHolder>() {

    var onItemClickListener: ((Pokemon?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPokemonBinding.inflate(layoutInflater, parent, false)
        return PokemonViewHolder(binding, repository)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.binding.apply {
            val currentPokemon = differ.currentList[position]
            holder.viewModel.setPokemon(currentPokemon)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallback =
        object : DiffUtil.ItemCallback<Pokemon>() {
            override fun areItemsTheSame(
                oldItem: Pokemon,
                newItem: Pokemon
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Pokemon,
                newItem: Pokemon
            ): Boolean {
                return oldItem == newItem
            }
        }

    val differ = AsyncListDiffer(this, differCallback)

    inner class PokemonViewHolder(
        val binding: ItemPokemonBinding,
        repository: DefaultPokemonRepository
    ) : RecyclerView.ViewHolder(binding.root) {

        val viewModel = PokemonItemViewModel(null, repository)

        init {
            viewModel.pokemon.observe(itemView.context as LifecycleOwner) { resource ->
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
                if (pokemon.id == null) {
                    textView.text = "Loading..."
                } else {
                    textView.text =
                        "#${pokemon.id} - ${pokemon.name?.replaceFirstChar { it.uppercase() }}"
                }

                val thumbnailUrl = pokemon.sprites?.front_default
                if (thumbnailUrl != null) {
                    thumbnailImageView.visibility = View.VISIBLE
                    Glide.with(itemView).load(thumbnailUrl).into(thumbnailImageView)
                } else {
                    thumbnailImageView.visibility = View.INVISIBLE
                }

                root.setOnClickListener { onItemClickListener?.invoke(pokemon) }
            }
        }
    }

}