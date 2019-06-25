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
import com.vinova.dotify.model.MusicCollection
import kotlinx.android.synthetic.main.list_artist_widget_custom.view.*

class YourArtistAdapter(private val context: Context,
                        private val listArtist : MutableList<MusicCollection>,
                        private val clickListener : (MusicCollection) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.list_artist_widget_custom, parent, false)

        return ArtistViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return listArtist.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ArtistViewHolder).bind(listArtist[position], clickListener)
    }

    inner class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(music: MusicCollection, clickListener: (MusicCollection) -> Unit){
            itemView.artist_name.text = music.artist

            Glide.with(context?.applicationContext ?: return)
                .load(music.photoURL)
                .thumbnail(0.5f)
                .error(R.drawable.artist_icon)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(itemView.artist_img)

            itemView.setOnClickListener { clickListener(music) }
        }
    }

}