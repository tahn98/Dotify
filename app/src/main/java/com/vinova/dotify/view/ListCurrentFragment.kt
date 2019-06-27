package com.vinova.dotify.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vinova.dotify.R
import com.vinova.dotify.adapter.ListCurrentAdapter
import com.vinova.dotify.adapter.YourSongAdapter
import com.vinova.dotify.model.Music
import kotlinx.android.synthetic.main.list_current_fragment.*
import kotlinx.android.synthetic.main.list_current_fragment.view.*

class ListCurrentFragment : Fragment(){
    private val listMusic : MutableList<Music>? = arrayListOf()
    private var listAdapter : ListCurrentAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list_current_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        list_current_container.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        listAdapter = ListCurrentAdapter( context!! , listMusic!!) {
                music : Music -> itemClicked(music)
        }
        list_current_container.adapter = listAdapter
        listAdapter!!.notifyDataSetChanged()
    }

    fun add(music: Music){
        this.listMusic?.add(music)
        listAdapter?.notifyDataSetChanged()
    }

    fun add(lisMusic : MutableList<Music>){
        this.listMusic?.clear()
        this.listMusic?.addAll(lisMusic)
        this.listAdapter?.notifyDataSetChanged()
    }

    private fun itemClicked(music: Music) {
        if (listMusic != null) {
            (activity as MainScreen).play(music, true)
        }
    }
}
