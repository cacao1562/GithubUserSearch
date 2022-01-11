package com.example.githubusersearch.ui.like

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.githubusersearch.R
import com.example.githubusersearch.databinding.FragmentLikeListBinding
import com.example.githubusersearch.ui.base.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LikeListFragment: BindingFragment<FragmentLikeListBinding>(R.layout.fragment_like_list) {

    companion object {
        fun newInstance() = LikeListFragment()
    }

    private val viewModel: LikeListViewModel by viewModels()

    private lateinit var mAdapter: LikeListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = LikeListAdapter(viewModel)
        binding.rvLikeList.apply {
            setHasFixedSize(true)
            adapter = mAdapter
        }

        viewModel.likeList.observe(viewLifecycleOwner) {
            mAdapter.submitList(it)
        }
    }

    fun setLocalSearchWord(word: String) {
        viewModel.setLocalSearchWord(word)
    }
}