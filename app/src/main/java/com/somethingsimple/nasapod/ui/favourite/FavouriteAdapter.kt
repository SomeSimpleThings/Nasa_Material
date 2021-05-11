package com.somethingsimple.nasapod.ui.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.somethingsimple.nasapod.R
import com.somethingsimple.nasapod.data.PictureOfDay
import com.somethingsimple.nasapod.databinding.FavouriteListItemBinding

class FavouriteAdapter : RecyclerView.Adapter<FavouriteAdapter.FavouriteItemViewHolder>() {


    private var favourites: List<PictureOfDay> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteItemViewHolder {
        val binding = FavouriteListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavouriteItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouriteItemViewHolder, position: Int) {
        val item = favourites[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return favourites.size
    }

    fun setFavourites(list: List<PictureOfDay>) {
        favourites = list
        notifyDataSetChanged()
    }

    class FavouriteItemViewHolder(private val binding: FavouriteListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PictureOfDay) {
            binding.imageView.load(item.url) {
                error(R.drawable.ic_load_error_24)
                placeholder(R.drawable.ic_baseline_image_not_supported_24)
            }
            binding.descriptionHeader.text = item.title
            binding.bottomSheetDescription.text = item.explanation
        }

    }
}