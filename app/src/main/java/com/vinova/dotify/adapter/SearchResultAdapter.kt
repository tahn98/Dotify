package com.vinova.dotify.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vinova.dotify.R
import com.vinova.dotify.model.Music
import kotlinx.android.synthetic.main.list_album_music_widget.view.*
import kotlinx.android.synthetic.main.result_search_item_layout.view.*

class SearchResultAdapter(private val context: Context,
                   private val listMusic : MutableList<Music>,
                   private val clickListener : (Music) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.result_search_item_layout, parent, false)
        return ResultViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return listMusic.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ResultViewHolder).bind( listMusic[position], clickListener)
    }

    inner class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind( music: Music, clickListener: (Music) -> Unit){
            itemView.setOnClickListener { clickListener(music) }
            itemView.name_song.text=music.name
            itemView.artist_song.text=music.artist
            Glide.with(context).load(music.posterURL).thumbnail(0.001f).into(itemView.img_song)
        }
    }
}