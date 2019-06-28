package com.vinova.dotify.view


import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.vinova.dotify.R
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
                if(rcv_search_result!=null)
                {
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
                                    hideKeyboard()
                                }
                            }
                        rcv_search_result.layoutManager = LinearLayoutManager(context)
                    }
                    else {
                        rcv_search_result.adapter =
                            SearchResultAdapter(context!!, ArrayList<Music>()) { music: Music ->
                                run {
                                    (activity as MainScreen).play(music)
                                    (activity as MainScreen).onBackPressed()
                                }
                            }
                        rcv_search_result.layoutManager = LinearLayoutManager(context)
                    }
                }
                return true
            }

        })
    }

    private fun hideKeyboard(): Boolean {
        val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity?.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        return imm.hideSoftInputFromWindow(view.windowToken, 0)
    }




}
