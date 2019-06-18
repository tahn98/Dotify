package com.vinova.dotify.view


import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.vinova.dotify.R
import org.json.JSONException
import com.vinova.dotify.databinding.SignUpBinding
import com.vinova.dotify.model.Registration


class SignUp : Fragment() {

    private var email: ObservableField<String> = ObservableField()
    private var username: ObservableField<String> = ObservableField()
    private var password: ObservableField<String> = ObservableField()
    private var gender: ObservableField<String> = ObservableField()
    private var birthdate: ObservableField<String> = ObservableField()
    private var mAuth: FirebaseAuth? = null
    private var mCallbackManager: CallbackManager? = null
    private val mCallback = object : FacebookCallback<LoginResult> {
        override fun onSuccess(loginResult: LoginResult) {
            handleFacebookAccessToken(loginResult.accessToken)
        }

        override fun onCancel() {

        }

        override fun onError(e: FacebookException) {

            AlertDialog.Builder(activity)
                .setTitle("Thông báo")
                .setMessage("Đã xảy ra lỗi trong quá trình xử lý.\n Xin vui lòng thử lại sau.")
                .setPositiveButton(android.R.string.ok, null)
                .show()
        }
    }


    init {
        email.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                checkEmpty()
            }
        })
        username.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                checkEmpty()
            }
        })
        password.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                checkEmpty()
            }
        })
        gender.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                checkEmpty()
            }
        })
        birthdate.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                checkEmpty()
            }
        })
    }

    var isFull: ObservableField<Boolean> = ObservableField(false)
    fun checkEmpty() {
        if (email.get().isNullOrEmpty() ||
            username.get().isNullOrEmpty() ||
            birthdate.get().isNullOrEmpty() ||
            gender.get().isNullOrEmpty() ||
            password.get().isNullOrEmpty()
        ) {
            isFull.set(false)
        }
        isFull.set(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AppEventsLogger.activateApp(activity?.application)
        mCallbackManager = CallbackManager.Factory.create()
        //Binding view with xml
        var binding = DataBindingUtil.inflate<SignUpBinding>(inflater, R.layout.sign_up, container, false)
        var view = binding.root
        binding.email = email
        binding.username = username
        binding.password = password
        binding.date = birthdate
        binding.gender = gender
        binding.check = isFull
        binding.fbSignup.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(
                this, listOf(
                    "public_profile", "email"
                )
            )
            LoginManager.getInstance().registerCallback(mCallbackManager, mCallback)
        }
        binding.signup.setOnClickListener {  }

        //init instance to firebase authentication
        mAuth = FirebaseAuth.getInstance()
        return view
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        mCallbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener(activity!!
            ) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information



                } else {
                    AlertDialog.Builder(activity)
                        .setTitle("Thông báo")
                        .setMessage("Đã xảy ra lỗi trong quá trình liên kết với tài khoản Facebook.")
                        .setPositiveButton(android.R.string.ok, null)
                        .show()

                }
            }
    }


}
