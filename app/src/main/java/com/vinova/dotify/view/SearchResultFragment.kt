package com.vinova.dotify.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.*
import androidx.recyclerview.widget.LinearLayoutManager


import com.vinova.dotify.R
import com.vinova.dotify.adapter.MusicAdapter
import com.vinova.dotify.adapter.SearchResultAdapter
import com.vinova.dotify.model.Music
import com.vinova.dotify.utils.BaseConst
import kotlinx.android.synthetic.main.fragment_search_result.*


class SearchResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainScreen).searchView?.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query != "") {
                    query!!.toLowerCase()
                    val result = BaseConst.musicStorage.filter {
                        it.artist!!.toLowerCase().contains(query as CharSequence) || it.name.toLowerCase().contains(
                            query as CharSequence
                        )
                    }
                    rcv_search_result.adapter =
                        SearchResultAdapter(context!!, result as MutableList<Music>) { music: Music ->
                            run {
                                (activity as MainScreen).play(music)
                                (activity as MainScreen).onBackPressed()
                            }
                        }
                    rcv_search_result.layoutManager = LinearLayoutManager(context)
                } else {
                    rcv_search_result.adapter =
                        SearchResultAdapter(context!!, ArrayList<Music>()) { music: Music ->
                            run {
                                (activity as MainScreen).play(music)
                                (activity as MainScreen).onBackPressed()
                            }
                        }
                    rcv_search_result.layoutManager = LinearLayoutManager(context)
                }
                return true
            }

        })
    }


}
