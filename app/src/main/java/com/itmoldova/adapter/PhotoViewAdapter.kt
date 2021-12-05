package com.itmoldova.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.github.chrisbanes.photoview.PhotoViewAttacher

import com.itmoldova.R
import com.squareup.picasso.Picasso

class PhotoViewAdapter(private val context: Context, private val urls: List<String>) : PagerAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int = urls.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = inflater.inflate(R.layout.item_photo, container, false)

        val imageView = view.findViewById<ImageView>(R.id.photo)
        PhotoViewAttacher(imageView)
        Picasso.with(context)
                .load(urls[position])
                .into(imageView)

        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, any: Any): Boolean = (view == any)

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        container.removeView(any as View)
    }
}
