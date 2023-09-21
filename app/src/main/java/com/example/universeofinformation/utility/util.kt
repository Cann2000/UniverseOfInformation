package com.example.universeofinformation.utility

import android.content.Context
import android.os.Handler
import android.widget.ImageView
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.universeofinformation.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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


fun ViewPager2.autoScroll(interval: Long) {

    val handler = Handler()
    var scrollPosition = 0

    val runnable = object : Runnable {

        override fun run() {
            val count = adapter?.itemCount ?: 0
            if (count > 0) {
                setCurrentItem(scrollPosition++ % count, true)
            }

            handler.postDelayed(this, interval)
        }
    }
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            scrollPosition = position + 1
        }

    })
    handler.post(runnable)
}

@BindingAdapter("android:starState")
fun toggleStarredState(view:ImageView,starredState:Boolean) {

    if (starredState) {
        view.setImageResource(R.drawable.starred)

    } else {
        view.setImageResource(R.drawable.not_starred)

    }
}

fun ImageView.starClickedUtil(starredState:Boolean){

    CoroutineScope(Dispatchers.Main).launch {

        if (!starredState) {
            this@starClickedUtil.setImageResource(R.drawable.starred)

        } else {
            this@starClickedUtil.setImageResource(R.drawable.not_starred)
        }
    }
}
