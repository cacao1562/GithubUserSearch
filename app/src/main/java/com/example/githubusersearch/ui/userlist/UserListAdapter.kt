package com.example.githubusersearch.ui.userlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.githubusersearch.database.RoomEntity
import com.example.githubusersearch.databinding.ItemUserListBinding

class UserListAdapter(
    private val viewModel: UserListViewModel
): PagingDataAdapter<RoomEntity, UserListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        return UserListViewHolder.from(parent, viewModel)
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<RoomEntity> =
            object : DiffUtil.ItemCallback<RoomEntity>() {
                override fun areItemsTheSame(oldItem: RoomEntity, newItem: RoomEntity) =
                    oldItem == newItem

                override fun areContentsTheSame(oldItem: RoomEntity, newItem: RoomEntity) =
                    oldItem.node_id == newItem.node_id
            }
    }
}

class UserListViewHolder(
    private val binding: ItemUserListBinding,
    private val vm: UserListViewModel
): RecyclerView.ViewHolder(binding.root) {

    fun bind(data: RoomEntity) {
        binding.data = data
        binding.viewModel = vm
    }

    companion object {
        fun from(parent: ViewGroup, viewModel: UserListViewModel): UserListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemUserListBinding.inflate(layoutInflater, parent, false)
            return UserListViewHolder(binding, viewModel)
        }
    }
}