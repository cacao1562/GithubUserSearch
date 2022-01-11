package com.example.githubusersearch.ui.like

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.githubusersearch.database.FavoriteEntity
import com.example.githubusersearch.databinding.ItemLikeListBinding

class LikeListAdapter(
    private val viewModel: LikeListViewModel
): ListAdapter<FavoriteEntity, LikeListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeListViewHolder {
        return LikeListViewHolder.from(parent, viewModel)
    }

    override fun onBindViewHolder(holder: LikeListViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<FavoriteEntity> =
            object : DiffUtil.ItemCallback<FavoriteEntity>() {
                override fun areItemsTheSame(oldItem: FavoriteEntity, newItem: FavoriteEntity) =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: FavoriteEntity, newItem: FavoriteEntity) =
                    oldItem == newItem
            }
    }
}

class LikeListViewHolder(
    private val binding: ItemLikeListBinding,
    private val vm: LikeListViewModel
): RecyclerView.ViewHolder(binding.root) {

    fun bind(data: FavoriteEntity) {
        binding.data = data
        binding.viewModel = vm
    }

    companion object {
        fun from(parent: ViewGroup, viewModel: LikeListViewModel): LikeListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemLikeListBinding.inflate(layoutInflater, parent, false)
            return LikeListViewHolder(binding, viewModel)
        }
    }
}