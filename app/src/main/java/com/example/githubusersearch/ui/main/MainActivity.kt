package com.example.githubusersearch.ui.main

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.viewpager2.widget.ViewPager2
import com.example.githubusersearch.R
import com.example.githubusersearch.databinding.ActivityMainBinding
import com.example.githubusersearch.ui.base.BindingActivity
import com.example.githubusersearch.ui.like.LikeListFragment
import com.example.githubusersearch.ui.userlist.UserListFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val tabTitle = arrayOf("검색 결과", "좋아요 목록")

    private val fragments = listOf(
        UserListFragment.newInstance(),
        LikeListFragment.newInstance()
    )

    enum class FragmentType {
        USER_LIST, LIKE_LIST
    }
    private var mSelectedView = FragmentType.USER_LIST

    private val viewpagerCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            when(position) {
                0 -> mSelectedView = FragmentType.USER_LIST
                1 -> mSelectedView = FragmentType.LIKE_LIST
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.vp2Main.isUserInputEnabled = false
        binding.vp2Main.registerOnPageChangeCallback(viewpagerCallback)
        binding.vp2Main.adapter = MainViewPagerAdapter(fragments,this)
        val userFr = fragments[0] as UserListFragment
        val likeFr = fragments[1] as LikeListFragment
        TabLayoutMediator(binding.tlTab, binding.vp2Main) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()

        binding.searchV.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Timber.d("[MainActivity>onQueryTextSubmit] query=$query")
                when (mSelectedView) {
                    FragmentType.USER_LIST -> userFr.getRooms(query.toString())
                    FragmentType.LIKE_LIST -> likeFr.setLocalSearchWord(query.toString())
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Timber.d("[MainActivity>onQueryTextChange] newText=$newText")
                when (mSelectedView) {
                    FragmentType.LIKE_LIST -> likeFr.setLocalSearchWord(newText.toString())
                }
                return true
            }
        })

    }

}