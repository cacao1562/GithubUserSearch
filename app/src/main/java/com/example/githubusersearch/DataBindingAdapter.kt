package com.example.githubusersearch

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

object DataBindingAdapter {

    @JvmStatic
    @BindingAdapter("loadImageOrDefault")
    fun loadImageOrDefault(imageView: ImageView, imgUrl: String?) {
        if (!imgUrl.isNullOrEmpty())
            Glide.with(imageView)
                .load(imgUrl)
                .apply(RequestOptions()
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.stat_notify_error))
                .into(imageView)
        else
            imageView.setImageResource(android.R.drawable.star_off)
    }

    @JvmStatic
    @BindingAdapter("isSelected")
    fun isSelected(view: View, selected: Boolean?) {
        selected?.let {
            view.isSelected = it
        }
    }


}
