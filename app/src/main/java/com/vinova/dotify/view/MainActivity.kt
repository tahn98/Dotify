package com.vinova.dotify.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vinova.dotify.R
import kotlinx.android.synthetic.main.activity_main.*
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import androidx.fragment.app.FragmentActivity
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                Log.i("HASH", "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("HASH", "printHashKey()", e)
        } catch (e: Exception) {
            Log.e("HASH", "printHashKey()", e)
        }

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
