package com.example.universeofinformation.utility

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.uploadImage(url:String?, placeholder: CircularProgressDrawable)
{
    val options = RequestOptions.placeholderOf(placeholder)
    Glide.with(this).setDefaultRequestOptions(options).load(url).into(this)
}

fun makePlaceHolder(context: Context): CircularProgressDrawable
{
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }
}

@BindingAdapter("android:downloadImage")
fun downloadImage(view: ImageView, url: String?){

    view.uploadImage(url, makePlaceHolder(view.context))
}