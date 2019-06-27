package com.vinova.dotify.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vinova.dotify.R
import com.vinova.dotify.model.Music
import com.vinova.dotify.view.BottomSheetAdd
import com.vinova.dotify.view.MainScreen
import kotlinx.android.synthetic.main.list_album_music_widget.view.*

class MusicAdapter(private val context: Context,
                      private val listMusic : MutableList<Music>,
                      private val clickListener : (Music, Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.list_album_music_widget, parent, false)
        return SongViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return listMusic.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SongViewHolder).bind((position + 1).toString(), listMusic[position], clickListener)
    }

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(pos : String, music: Music, clickListener: (Music, Int) -> Unit){
            itemView.music_name.text = music.name
            itemView.music_artist.text = music.artist
            itemView.music_number.text = pos

            itemView.setOnClickListener { clickListener(music, pos.toInt()) }

            itemView.ic_more_add.setOnClickListener {
                var bottomsheet : BottomSheetDialogFragment = BottomSheetAdd(music, this@MusicAdapter, listMusic)
                bottomsheet.show((context as MainScreen).supportFragmentManager, bottomsheet.tag)
            }
        }
    }
}