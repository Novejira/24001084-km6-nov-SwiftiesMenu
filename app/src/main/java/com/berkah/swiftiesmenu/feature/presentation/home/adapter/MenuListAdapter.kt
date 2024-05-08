package com.berkah.swiftiesmenu.feature.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.berkah.swiftiesmenu.databinding.ItemFoodGridBinding
import com.berkah.swiftiesmenu.databinding.ItemFoodListBinding
import com.berkah.swiftiesmenu.feature.data.base.ViewHolderBinder
import com.berkah.swiftiesmenu.feature.data.model.Menu

class MenuListAdapter(
    private val listener: OnItemClickedListener<Menu>,
    var listMode: Int = MODE_LIST,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val MODE_LIST = 1
        const val MODE_GRID = 0
    }

    private val asyncDataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<Menu>() {
                override fun areItemsTheSame(
                    oldItem: Menu,
                    newItem: Menu,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Menu,
                    newItem: Menu,
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            },
        )

    fun submitData(data: List<Menu>) {
        asyncDataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == MODE_GRID) {
            val binding = ItemFoodGridBinding.inflate(inflater, parent, false)
            FoodGridItemViewHolder(binding, listener)
        } else {
            val binding = ItemFoodListBinding.inflate(inflater, parent, false)
            FoodListItemViewHolder(binding, listener)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        if (holder !is ViewHolderBinder<*>) return
        (holder as ViewHolderBinder<Menu>).bind(asyncDataDiffer.currentList[position])
    }

    override fun getItemCount(): Int = asyncDataDiffer.currentList.size

    interface OnItemClickedListener<T> {
        fun onItemClicked(item: T)
    }

    override fun getItemViewType(position: Int): Int {
        return listMode
    }
}
