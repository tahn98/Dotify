package com.vinova.dotify.view


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vinova.dotify.R
import kotlinx.android.synthetic.main.log_in.view.*

class LogIn : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var loginView : View = inflater.inflate(R.layout.log_in, container, false)

        loginView.button_login.setOnClickListener {
            goToBrowseScreen()
        }

        return loginView
    }

    private fun goToBrowseScreen(){
        var browseIntent = Intent(activity, BrowseScreen::class.java)
        startActivity(browseIntent)
    }

}
