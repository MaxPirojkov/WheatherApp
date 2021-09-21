package com.android.weatherapp

import android.widget.ImageView

fun ImageView.loadImage(imageUrl: String?) {
    GlideApp
        .with(this.context)
        .load(imageUrl)
        .error(R.drawable.ic_broken)
        .into(this)
}