package com.vinova.dotify.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.vinova.dotify.R
import com.vinova.dotify.model.MusicCollection
import kotlinx.android.synthetic.main.list_album_custom_widget.view.*

class YourAlbumAdapter(private val context: Context,
                       private val listAlbulm : MutableList<MusicCollection>,
                       private val clickListener : (MusicCollection) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.list_album_custom_widget, parent, false)

        return AlbulmViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return listAlbulm.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AlbulmViewHolder).bind(listAlbulm[position], clickListener)
    }

    inner class AlbulmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(album: MusicCollection, clickListener: (MusicCollection) -> Unit){
            itemView.album_name.text = album.name
            itemView.album_artist.text = album.artist

            Glide.with(context?.applicationContext ?: return)
                .load(album.photoURL)
                .thumbnail(0.5f)
                .error(R.drawable.ic_launcher_background)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(itemView.album_img)

            itemView.setOnClickListener { clickListener(MusicCollection()) }
        }
    }

}