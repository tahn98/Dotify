package com.vinova.dotify.view

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vinova.dotify.R
import com.vinova.dotify.model.Music
import com.vinova.dotify.model.MusicCollection
import com.vinova.dotify.model.User
import com.vinova.dotify.utils.BaseConst
import com.vinova.dotify.viewmodel.UserViewModel

class SplashScreen : AppCompatActivity() {
    private var mViewModel: UserViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        mViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        val mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        val mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase.child("music").addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
               if(dataSnapshot.exists())
               {
                   for ( postSnapshot in dataSnapshot.children) {
                       var data=postSnapshot.getValue(Music::class.java)
                       BaseConst.musicStorage.add(data!!)
                   }
               }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
        if (currentUser != null) {
            mViewModel?.getUser(currentUser.uid)?.observe(this, Observer<User> { user-> run {
                if(user.uid!="-1" && user.uid!="" && user!=null)
                {
                    val browseIntent = Intent(this, MainScreen::class.java)
                    browseIntent.putExtra("curUser", user)
                    startActivity(browseIntent)
                    finish()
                }
                else
                {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            } })
        }
        else{

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
