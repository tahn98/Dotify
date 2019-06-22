package com.vinova.dotify.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vinova.dotify.R
import com.vinova.dotify.model.MusicCollection
import com.vinova.dotify.view.view_holder.SuggestedPlaylistHolder
import java.util.ArrayList

class SuggestedPlaylistAdapter(private val context: Context) : RecyclerView.Adapter<SuggestedPlaylistHolder>() {
    private var listPlaylist: MutableCollection<MusicCollection?> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestedPlaylistHolder {
        return SuggestedPlaylistHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_suggested_playlists,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
       return listPlaylist.size
    }

    override fun onBindViewHolder(holder: SuggestedPlaylistHolder, position: Int) {
        holder.bind(listPlaylist.elementAt(position))
    }

    fun addAll(input: List<MusicCollection>) {
        val lastSize = listPlaylist.size
        listPlaylist.addAll(input)
        notifyItemRangeInserted(lastSize, input.size)
    }

    fun add(playlist: MusicCollection?) {
        val lastSize = listPlaylist.size
        listPlaylist.add(playlist)
        notifyItemRangeInserted(lastSize, 1)
    }

    fun removeLastItem() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            listPlaylist.removeIf { it == null }
        }
        notifyDataSetChanged()
    }

    fun updateSource(input: List<MusicCollection>) {
        listPlaylist.clear()
        listPlaylist.addAll(input)
        notifyDataSetChanged()
    }
}