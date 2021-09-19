package com.android.weatherapp

import android.widget.ImageView
import com.android.weatherapp.GlideApp
import com.android.wheatherapp.R
import com.bumptech.glide.Glide

fun ImageView.loadImage(imageUrl: String?) {
    GlideApp
        .with(this.context)
        .load(imageUrl)
//        .override(200, 200)
        .error(R.drawable.ic_broken)
        .into(this)

}