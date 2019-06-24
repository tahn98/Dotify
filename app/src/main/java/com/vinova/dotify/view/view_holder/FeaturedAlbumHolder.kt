package com.vinova.dotify.view.view_holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vinova.dotify.model.MusicCollection
import com.vinova.dotify.utils.OnClickListener
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_featured_album.*

class FeaturedAlbumHolder (override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {
    fun bind(collection: MusicCollection?,itemClick:OnClickListener?) {
        if (collection != null) {
            if(itemClick!=null)
                containerView.setOnClickListener { itemClick.onClick(collection) }
            Glide.with(containerView.context).load(collection.photoURL).thumbnail(0.01f).into(poster_album)
            album_name.text=collection.name
            if(collection.itemCount>1)
                artist.text=collection.artist+" • "+collection.itemCount+" songs"
            else
                artist.text=collection.artist+" • "+collection.itemCount+" song"
        }
    }
}