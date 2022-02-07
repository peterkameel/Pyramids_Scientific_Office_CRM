package com.peter_kameel.pyramidsscientificofficecrm.ui.activity

import android.animation.Animator
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.data.FirebaseDBRepo
import com.peter_kameel.pyramidsscientificofficecrm.databinding.ActivityLoginBinding
import com.peter_kameel.pyramidsscientificofficecrm.helper.objects.CheckNetwork
import com.peter_kameel.pyramidsscientificofficecrm.util.Massages
import com.peter_kameel.pyramidsscientificofficecrm.util.Shared
import com.peter_kameel.pyramidsscientificofficecrm.util.SharedTag
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    @Inject
    lateinit var shared: Shared
    @Inject
    lateinit var firebaseDBRepo: FirebaseDBRepo
    @Inject
    lateinit var checkNetwork: CheckNetwork
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Mack a timer for SplashScreen
        object : CountDownTimer(1000, 1000) {
            override fun onFinish() {
                binding.appName.visibility = View.GONE
                binding.loadingProgressBar.visibility = View.GONE
                binding.rootView.setBackgroundColor(
                    ContextCompat.getColor(
                        this@LoginActivity,
                        R.color.white
                    )
                )
                binding.bookIconImageView.setImageResource(R.drawable.ic_logo)
                startAnimation()
            }

            override fun onTick(p0: Long) {}
        }.start()

        //if user and password is ok it will skip login activity
        checker()

        //on button login clicked
        binding.loginButton.setOnClickListener {
            //check if username field is empty
            when {
                binding.email.text!!.isEmpty() -> binding.email.error = "Enter Email"
                //check if password field is empty
                binding.password.text!!.isEmpty() -> binding.password.error = "Enter Password"
                //if connection
                checkNetwork.isNetworkAvailable() -> login(binding.email.text.toString(), binding.password.text.toString())
                //if no connection
                else -> Toast.makeText(this, "Check Internet Connection", Toast.LENGTH_LONG).show()
            }//End When
        }//End OnClickListener

        //on Button Reset Password Clicked
        binding.forgetPass.setOnClickListener {
            //Check internet connection using Interconnection::Class and save it in boolean value
            val check: Boolean = CheckNetwork(this).isNetworkAvailable()
            when {
                //check if email is empty
                binding.email.text!!.isEmpty() -> binding.email.error = "Enter Your Email!"
                //if connection
                check -> reset(binding.email.text.toString())
                //if no connection
                else -> Toast.makeText(this, Massages.connection, Toast.LENGTH_LONG).show()
            }//End When
        }//End OnClickListener
    }

    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            //Request The Permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1001
            )
        }
    }

    //Start Animation to show content
    private fun startAnimation() {
        binding.bookIconImageView.animate().apply {
            x(50f)
            y(100f)
            duration = 1000
        }.setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationEnd(p0: Animator?) {
                binding.afterAnimationView.visibility = View.VISIBLE
            }

            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationStart(p0: Animator?) {}
        })
    }

    //Skip Login Activity If User Save Login Data By Remember me CheckBox
    private fun checker() {
        val check = java.lang.Boolean.valueOf(
            shared.readSharedBoolean(SharedTag.User_Found,false)
        )
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(SharedTag.User_Found, check)
        //The Value if you click on Login Activity and Set the value is FALSE and whe false the activity will be visible
        if (check) {
            startActivity(intent)
            finish()
        } //If no the Admin Activity not Do Anything
    }

    //Login Request
    private fun login(email: String, password: String) {
        //show the Login progress bar
        binding.LoginProgressBar.visibility = View.VISIBLE
        //Sign In
        val auth = FirebaseAuth.getInstance()
        //Firebase SignIn With Email And Password
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (binding.checkbox.isChecked) {
                    shared.saveSharedBoolean(
                        SharedTag.User_Found,
                        true
                    ) //use this line to skip the activity
                }//End if (checkBox.isChecked)
                saveLoginData(auth.currentUser!!.uid)

            } else {
                //Remove LoginProgressBar
                binding.LoginProgressBar.visibility = View.GONE
                Toast.makeText(this, "Error Check Username and Password", Toast.LENGTH_LONG).show()
            }//else
        }//End addOnCompleteListener
    }//End Login

    //Send reset email
    private fun reset(email: String) {
        val auth = FirebaseAuth.getInstance()
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Email send successfully Check your email",
                        Toast.LENGTH_LONG
                    ).show()
                }//End if
            }//End Listener
    }

    private fun saveLoginData(id: String) {
        CoroutineScope(Dispatchers.Main).launch {
            firebaseDBRepo.getUserData(
                id,
                onSuccess = {
                    shared.saveSharedString(
                        SharedTag.UID,
                        it.id.toString()
                    )
                    shared.saveSharedString(
                        SharedTag.user_name,
                        it.user_name.toString()
                    )
                    shared.saveSharedString(
                        SharedTag.supervisor_ID,
                        it.supervisor_ID.toString()
                    )
                    shared.saveSharedString(
                        SharedTag.permission,
                        it.permission.toString()
                    )
                    startActivity(
                        Intent(
                            applicationContext,
                            MainActivity::class.java
                        )
                    ) //move to activity
                    finish() // finish the activity
                })
        }
    }
}