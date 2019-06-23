package com.vinova.dotify.view.view_holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vinova.dotify.model.MusicCollection
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_genre_collection.*


class GenrePlaylistHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {
    fun bind(collection: MusicCollection?) {
        if (collection != null) {
            Glide.with(containerView.context).load(collection.photoURL).thumbnail(0.01f).into(background)
            genre.text=collection.name
        }
    }
}
