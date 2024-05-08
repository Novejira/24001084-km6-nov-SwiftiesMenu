package com.berkah.swiftiesmenu.feature.presentation.home.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.berkah.swiftiesmenu.R
import com.berkah.swiftiesmenu.databinding.ItemFoodListBinding
import com.berkah.swiftiesmenu.feature.data.base.ViewHolderBinder
import com.berkah.swiftiesmenu.feature.data.model.Menu
import com.berkah.swiftiesmenu.feature.data.utils.toIndonesianFormat

class FoodListItemViewHolder(
    private val binding: ItemFoodListBinding,
    private val listener: MenuListAdapter.OnItemClickedListener<Menu>,
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Menu> {
    override fun bind(item: Menu) {
        item.let {
            binding.ivFoodPhoto.load(it.imgUrl) {
                crossfade(true)
                error(R.mipmap.ic_launcher)
            }
            binding.tvFoodName.text = it.name
            binding.tvFoodPrice.text = it.price.toIndonesianFormat()
            itemView.setOnClickListener {
                listener.onItemClicked(item)
            }
        }
    }
}
