package com.example.opengldemo.widget

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class ShaderViewAdapter(private val items: List<View>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = items[position]
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(items[position])
    }
}