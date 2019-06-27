package com.vinova.dotify.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vinova.dotify.R
import com.vinova.dotify.model.User
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
            ft.replace(R.id.fragment_holder, LogInFragment())
            ft.commit()
        }
        else
        {
            ft.replace(R.id.fragment_holder, SignUpFragment())
            ft.commit()
        }
    }
    fun moveToMainScreen(user: User)
    {
        val browseIntent = Intent(this, MainScreen::class.java)
        browseIntent.putExtra("curUser", user)
        startActivity(browseIntent)
        finish()
    }
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
