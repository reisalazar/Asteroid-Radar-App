package com.udacity.asteroidradar.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.database.Asteroid
import com.udacity.asteroidradar.databinding.ListItemAsteroidBinding


class AsteroidListAdapter(private val itemClickListener: AsteroidItemOnClickListener) :
    ListAdapter<Asteroid, AsteroidListAdapter.AsteroidsViewHolder>(AsteroidsViewHolder.DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidsViewHolder {
        return AsteroidsViewHolder(
            ListItemAsteroidBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: AsteroidsViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(item)
        }
        holder.bind(item)
    }


    class AsteroidsViewHolder(private val binding: ListItemAsteroidBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind( item: Asteroid) {
            binding.asteroid= item
            binding.executePendingBindings()
        }

        class DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
            override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
                return oldItem == newItem
            }
        }
    }
    class AsteroidItemOnClickListener(val clickListener: (asteroid:Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }
}