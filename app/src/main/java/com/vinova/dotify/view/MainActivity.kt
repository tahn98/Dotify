package com.vinova.dotify.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vinova.dotify.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        login_btn.setOnClickListener {
            val intent = Intent(this, Authentication::class.java)
            intent.putExtra("screen", "login")
            startActivity(intent)
        }
        signup_btn.setOnClickListener {
            val intent = Intent(this, Authentication::class.java)
            intent.putExtra("screen", "singup")
            startActivity(intent)
        }
    }
}
