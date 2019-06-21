package com.vinova.dotify.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.vinova.dotify.R
import com.vinova.dotify.model.Music
import kotlinx.android.synthetic.main.list_album_custom_widget.view.*

class YourSongAdapter(private val context: Context,
                      private val listMusic : MutableList<Music>,
                      private val clickListener : (Music) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.list_album_custom_widget, parent, false)

        return SongViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return listMusic.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SongViewHolder).bind(listMusic[position], clickListener)
    }

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(music: Music, clickListener: (Music) -> Unit){
            itemView.album_name.text = music.name
            itemView.album_artist.text = music.artist

            Glide.with(context?.applicationContext ?: return)
                .load(music.photoURL)
                .thumbnail(0.5f)
                .error(R.drawable.ic_launcher_background)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(itemView.album_img)

            itemView.setOnClickListener { clickListener(Music()) }
        }
    }

}