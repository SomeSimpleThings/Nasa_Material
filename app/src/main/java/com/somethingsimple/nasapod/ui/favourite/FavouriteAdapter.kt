package com.somethingsimple.nasapod.ui.favourite

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.somethingsimple.nasapod.R
import com.somethingsimple.nasapod.data.PictureOfDay
import com.somethingsimple.nasapod.databinding.FavouriteListItemBinding

class FavouriteAdapter(val onItemRemovedCallBack: OnItemRemovedCallBack) :
    RecyclerView.Adapter<FavouriteAdapter.FavouriteItemViewHolder>(),
    ItemTouchHelperAdapter {


    private var favourites: MutableList<PictureOfDay> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteItemViewHolder {
        val binding = FavouriteListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavouriteItemViewHolder(binding, onItemRemovedCallBack)
    }

    override fun onBindViewHolder(holder: FavouriteItemViewHolder, position: Int) {
        val item = favourites[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return favourites.size
    }

    fun setFavourites(list: List<PictureOfDay>) {
        favourites = list.toMutableList()
        notifyDataSetChanged()
    }

    class FavouriteItemViewHolder(
        private val binding: FavouriteListItemBinding,
        private val onItemRemovedCallBack: OnItemRemovedCallBack
    ) :
        RecyclerView.ViewHolder(binding.root), ItemTouchHelperViewHolder {
        fun bind(item: PictureOfDay) {
            binding.imageView.load(item.url) {
                error(R.drawable.ic_load_error_24)
                placeholder(R.drawable.ic_baseline_image_not_supported_24)
            }
            binding.favIcon.apply {
                setOnClickListener {
                    item.liked = !item.liked
                    setBackgroundResource(
                        if (item.liked) R.drawable.ic_baseline_favorite_24
                        else R.drawable.ic_baseline_favorite_border_24
                    )
                    onItemRemovedCallBack.onItemRemoved(item)
                }
            }
            binding.descriptionHeader.text = item.title
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }

    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        favourites.removeAt(fromPosition).apply {
            favourites.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, this)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        val item = favourites[position]
        item.liked = false
        onItemRemovedCallBack.onItemRemoved(item)
        favourites.removeAt(position)
        notifyItemRemoved(position)

    }
}

interface OnItemRemovedCallBack {
    fun onItemRemoved(pictureOfDay: PictureOfDay)
}