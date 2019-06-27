package com.vinova.dotify.view


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
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
import com.vinova.dotify.R
import com.vinova.dotify.databinding.SignUpBinding
import com.vinova.dotify.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import androidx.lifecycle.Observer
import com.kaopiz.kprogresshud.KProgressHUD
import com.vinova.dotify.model.User
import java.util.*


class SignUpFragment : Fragment() {

    private var email: ObservableField<String> = ObservableField()
    private var username: ObservableField<String> = ObservableField()
    private var password: ObservableField<String> = ObservableField()
    private var gender: ObservableField<String> = ObservableField()
    private var birthdate: ObservableField<String> = ObservableField()
    var isFull: ObservableField<Boolean> = ObservableField(false)
    private var mViewModel: UserViewModel? = null
    private val myCalendar = Calendar.getInstance()
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
    private var date = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, month)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        updateLabel()
    }

    init {
        email.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                checkEmpty()
                isEmailValid(email.get()!!)
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AppEventsLogger.activateApp(activity?.application)
        mCallbackManager = CallbackManager.Factory.create()
        mViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        //Binding view with xml
        val binding = DataBindingUtil.inflate<SignUpBinding>(inflater, R.layout.sign_up, container, false)
        val view = binding.root
        binding.email = email
        binding.username = username
        binding.password = password
        binding.date = birthdate
        binding.gender = gender
        binding.check = isFull
        binding.birthdateField.keyListener = null
        binding.birthdateField.setOnClickListener {
            DatePickerDialog(
                context!!, R.style.datepicker, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        binding.fbSignup.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(
                this@SignUpFragment, listOf(
                    "public_profile", "email"
                )
            )
            LoginManager.getInstance().registerCallback(mCallbackManager, mCallback)
        }
        binding.signup.setOnClickListener {
            val loading= KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Creating account ...")
                .setCancellable(false)
                .setAnimationSpeed(1)
                .setDimAmount(5f)
            loading.show()
            mViewModel?.createUser(email.get()!!,password.get()!!)?.observe(this, Observer<String>{data->run {
                if(data != "-")
                {
                    val user = User()
                    user.uid = data
                    user.email = email.get()!!
                    user.username = username.get()!!
                    user.profile_photo = ""
                    user.gender = gender.get()!!
                    user.birthdate = birthdate.get()!!
                    mViewModel!!.postUser(user)
                    (activity as Authentication).moveToMainScreen(user)
                }
                else
                {
                    AlertDialog.Builder(activity)
                        .setTitle("Thông báo")
                        .setMessage("Đã xảy ra lỗi trong quá trình đăng ký. Vui lòng thử lại sau.")
                        .setPositiveButton(android.R.string.ok, null)
                        .show()
                }
                loading.dismiss()
            }})
        }
        binding.emailField.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (!isEmailValid(email.get()!!))
                    binding.emailContainer.error = "Your email is not correct."
                else binding.emailContainer.error = null
            }
            else binding.emailContainer.error = null
        }
        binding.passField.onFocusChangeListener=View.OnFocusChangeListener{_,hasFocus->
            if(!hasFocus){
                if(!isPasswordValid(password.get()!!))
                    binding.passContainer.error="Password must contain at least 8 characters, including upper, lowercase and number."
                else binding.passContainer.error=null
            }
            else binding.passContainer.error=null
        }
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
        val loading= KProgressHUD.create(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Logging in ...")
            .setCancellable(false)
            .setAnimationSpeed(1)
            .setDimAmount(5f)
        loading.show()
        mAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener(
                activity!!
            ) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    var res = task.result
                    mViewModel!!.checkUser(res?.user!!.uid)?.observe(this, Observer<Boolean> { data ->
                        run {
                            if (!data) {
                                AlertDialog.Builder(activity)
                                    .setTitle("Thông báo")
                                    .setMessage("Email liên kết với tài khoản Facebook này đã được sử dụng.")
                                    .setPositiveButton(android.R.string.ok, null)
                                    .show()
                            } else {
                                val user = User()
                                user.uid = res.user.uid
                                user.email = res.user.email!!
                                user.username = res.user.displayName!!
                                user.profile_photo = res.user.photoUrl.toString()
                                user.gender = "Male"
                                user.birthdate = "01/01/2000"
                                mViewModel!!.postUser(user)
                                (activity as Authentication).moveToMainScreen(user)
                            }
                        }
                        loading.dismiss()
                    })


                } else {
                    loading.dismiss()
                    AlertDialog.Builder(activity)
                        .setTitle("Thông báo")
                        .setMessage("Đã xảy ra lỗi trong quá trình liên kết với tài khoản Facebook.")
                        .setPositiveButton(android.R.string.ok, null)
                        .show()
                }
            }
    }

    fun checkEmpty() {
        if (email.get().isNullOrEmpty() ||
            username.get().isNullOrEmpty() ||
            birthdate.get().isNullOrEmpty() ||
            gender.get().isNullOrEmpty() ||
            password.get().isNullOrEmpty()|| !isEmailValid(email.get()!!) || !isPasswordValid(password.get()!!)
        ) {
            isFull.set(false)
        }
        else isFull.set(true)
    }

    private fun updateLabel() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        birthdate.set(sdf.format(myCalendar.time))
    }

    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        val regex = """(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}""".toRegex()
        return regex.matches(password)
    }

}
