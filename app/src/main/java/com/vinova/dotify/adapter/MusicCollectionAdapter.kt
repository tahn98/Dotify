package com.vinova.dotify.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vinova.dotify.R
import com.vinova.dotify.model.MusicCollection
import com.vinova.dotify.utils.OnClickListener
import com.vinova.dotify.view.view_holder.FeaturedAlbumHolder
import com.vinova.dotify.view.view_holder.GenrePlaylistHolder
import com.vinova.dotify.view.view_holder.SuggestedPlaylistHolder
import java.util.ArrayList

class MusicCollectionAdapter(private val context: Context, private val type: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listCollection: MutableCollection<MusicCollection?> = ArrayList()
    private var itemClick: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (type) {
            1 -> return SuggestedPlaylistHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_suggested_playlists,
                    parent,
                    false
                )
            )
            2 -> return GenrePlaylistHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_genre_collection,
                    parent,
                    false
                )
            )
            else -> return FeaturedAlbumHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_featured_album,
                    parent,
                    false
                )
            )
        }

    }

    override fun getItemCount(): Int {
        return listCollection.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (type) {
            1 -> (holder as SuggestedPlaylistHolder).bind(listCollection.elementAt(position),itemClick)
            2 -> (holder as GenrePlaylistHolder).bind(listCollection.elementAt(position),itemClick)
            else -> (holder as FeaturedAlbumHolder).bind(listCollection.elementAt(position),itemClick)
        }

    }

    fun setOnClick(listener: OnClickListener) {
        this.itemClick = listener
    }

    fun addAll(input: List<MusicCollection>) {
        val lastSize = listCollection.size
        listCollection.addAll(input)
        notifyItemRangeInserted(lastSize, input.size)
    }

    fun add(collection: MusicCollection?) {
        val lastSize = listCollection.size
        listCollection.add(collection)
        notifyItemRangeInserted(lastSize, 1)
    }

    fun removeLastItem() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            listCollection.removeIf { it == null }
        }
        notifyDataSetChanged()
    }

    fun updateSource(input: List<MusicCollection>) {
        listCollection.clear()
        listCollection.addAll(input)
        notifyDataSetChanged()
    }
}