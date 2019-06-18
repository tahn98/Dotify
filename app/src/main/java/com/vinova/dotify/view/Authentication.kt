package com.vinova.dotify.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vinova.dotify.R
import kotlinx.android.synthetic.main.activity_authentication.*

class Authentication : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        var type=intent.getStringExtra("screen")
        val ft = supportFragmentManager.beginTransaction()
        if(type=="login")
        {
            ft.replace(R.id.fragment_holder, LogIn())
            ft.commit()
        }
        else
        {
            ft.replace(R.id.fragment_holder, SignUp())
            ft.commit()
        }
    }
}
