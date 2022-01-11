package com.example.githubusersearch.ui.userlist

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.example.githubusersearch.R
import com.example.githubusersearch.databinding.FragmentUserListBinding
import com.example.githubusersearch.toast
import com.example.githubusersearch.ui.base.BindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class UserListFragment: BindingFragment<FragmentUserListBinding>(R.layout.fragment_user_list) {

    companion object {
        fun newInstance() = UserListFragment()
    }

    private val viewModel: UserListViewModel by viewModels()
    private lateinit var mAdapter: UserListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = UserListAdapter(viewModel)
        binding.rvUserList.apply {
            setHasFixedSize(true)
            adapter = mAdapter
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.datalist.collectLatest {
                mAdapter.submitData(it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            mAdapter.loadStateFlow.collectLatest { loadStates ->
                Timber.d("error addLoadStateListener=\n$loadStates")
                val isError = loadStates.mediator?.refresh is LoadState.Error
                binding.pbUserList.isVisible = loadStates.mediator?.append is LoadState.Loading
                if (isError) {
                    val errorMsg = (loadStates.mediator?.refresh as? LoadState.Error)?.error?.message
                    if (errorMsg.isNullOrEmpty().not()) {
                        toast(errorMsg!!)
                    }
                }

            }
        }
    }

    fun getRooms(query: String) {
        mAdapter.submitData(viewLifecycleOwner.lifecycle, PagingData.empty())
        viewModel.setRemoteSearchWord(query)
    }

}