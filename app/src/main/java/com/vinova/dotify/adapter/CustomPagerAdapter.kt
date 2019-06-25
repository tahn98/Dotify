package com.vinova.dotify.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.vinova.dotify.databinding.ItemLatestCollectionBinding
import com.vinova.dotify.model.Music
import com.vinova.dotify.utils.OnClickListener
import com.vinova.dotify.utils.OnViewPagerItemClickListener

class CustomPagerAdapter(val context: Context, private val musics: List<Music> ) : PagerAdapter() {
    var listener: OnViewPagerItemClickListener?=null
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = ItemLatestCollectionBinding.inflate(LayoutInflater.from(context), container, false)
        Glide.with(context).load(musics[position].backdropURL!!).thumbnail(0.01f).into(binding.backdrop)
        Glide.with(context).load(musics[position].posterURL!!).thumbnail(0.01f).into(binding.poster)
        binding.name.text=musics[position].name
        binding.description.text=musics[position].description
        binding.root.setOnClickListener {
            listener?.onClick(musics[position])
        }
        container.addView(binding.root)
        return binding.root
    }
    fun setOnClickListener(listener: OnViewPagerItemClickListener){
        this.listener=listener
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view==`object`
    }

    override fun getCount(): Int {
        return musics.size
    }
}