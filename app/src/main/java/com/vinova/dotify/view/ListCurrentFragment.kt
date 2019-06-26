package com.vinova.dotify.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vinova.dotify.R
import com.vinova.dotify.adapter.YourSongAdapter
import com.vinova.dotify.model.Music
import kotlinx.android.synthetic.main.list_current_fragment.*

class ListCurrentFragment() : Fragment(){

    private val listMusic : MutableList<Music>? = null
    private var listAdapter : YourSongAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.list_current_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        list_current_container.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        listAdapter = YourSongAdapter( context!! , listMusic!!) {
                music : Music -> itemClicked(music)}
        }

    private fun itemClicked(music: Music) {

    }
}
