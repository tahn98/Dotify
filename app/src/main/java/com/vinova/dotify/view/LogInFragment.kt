package com.vinova.dotify.view


import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.kaopiz.kprogresshud.KProgressHUD
import com.vinova.dotify.R
import com.vinova.dotify.databinding.LogInBinding
import com.vinova.dotify.model.User
import com.vinova.dotify.viewmodel.UserViewModel

class LogInFragment : Fragment() {
    private var email: ObservableField<String> = ObservableField()
    private var password: ObservableField<String> = ObservableField()
    private var isFull: ObservableField<Boolean> = ObservableField(false)
    private var mViewModel: UserViewModel? = null
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

        password.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                checkEmpty()
            }
        })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AppEventsLogger.activateApp(activity?.application)
        mCallbackManager = CallbackManager.Factory.create()
        mViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        //Binding view with xml
        val binding = DataBindingUtil.inflate<LogInBinding>(inflater, R.layout.log_in, container, false)
        val view = binding.root
        binding.email = email
        binding.password = password
        binding.check = isFull

        binding.loginBtn.setOnClickListener {
            val loading= KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Logging in ...")
                .setCancellable(false)
                .setAnimationSpeed(1)
                .setDimAmount(5f)
            loading.show()
            mViewModel?.logInUser(email.get()!!,password.get()!!)?.observe(this, Observer<String> { data ->
                run {
                    if (data != "-1") {
                        mViewModel?.getUser(data)?.observe(this, Observer<User> { user ->
                            run {
                                if (user.uid != "-1") {
                                    var browseIntent = Intent(activity, MainScreen::class.java)
                                    browseIntent.putExtra("curUser", user)
                                    startActivity(browseIntent)
                                    this@LogInFragment.activity?.finish()
                                } else {
                                    createErrorDialog()
                                }
                                loading.dismiss()
                            }
                        })
                    }
                    else {
                        loading.dismiss()
                        createErrorDialog()
                    }
                }
            })
        }

        binding.fbLogin.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(
                this@LogInFragment, listOf(
                    "public_profile", "email"
                )
            )
            LoginManager.getInstance().registerCallback(mCallbackManager, mCallback)
        }
        mAuth = FirebaseAuth.getInstance()
        return view
    }

    private fun createErrorDialog() {
        AlertDialog.Builder(activity)
            .setTitle("Thông báo")
            .setMessage("Đã xảy ra lỗi trong quá trình đăng nhập. Vui lòng thử lại sau")
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        mCallbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        val loading= KProgressHUD.create(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Logging in ...")
            .setCancellable(false)
            .setAnimationSpeed(1)
            .setDimAmount(5f)
        loading.show()
        mViewModel?.logInUser(credential)?.observe(this, Observer<String> { data ->
            run {
                if (data != "-1") {
                    mViewModel?.getUser(data)?.observe(this, Observer<User> { user ->
                        run {
                            if (user.uid != "-1") {
                                val browseIntent = Intent(activity, MainScreen::class.java)
                                browseIntent.putExtra("curUser", user)
                                startActivity(browseIntent)
                                this@LogInFragment.activity?.finish()
                            } else {
                                createErrorDialog()
                            }
                        }
                    })
                    loading.dismiss()
                }
                else
                {
                    loading.dismiss()
                }
            }
        })
    }
    fun checkEmpty() {
        if (email.get().isNullOrEmpty() ||
            password.get().isNullOrEmpty()
        ) {
            isFull.set(false)
        }
        else isFull.set(true)
    }


}
