package com.vinova.dotify.view.view_holder

import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vinova.dotify.model.MusicCollection
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_suggested_playlists.*
import java.time.LocalDateTime

class SuggestedPlaylistHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {
    fun bind(playlist: MusicCollection?) {
        if (playlist != null) {
            Glide.with(containerView.context).load(playlist.photoURL).thumbnail(0.01f).into(imgView)
            playlistName.text=playlist.name
        }
    }
}