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
import com.peter_kameel.pyramidsscientificofficecrm.helper.InterConn.InternetConnection
import com.peter_kameel.pyramidsscientificofficecrm.util.Massages
import com.peter_kameel.pyramidsscientificofficecrm.util.Shared
import com.peter_kameel.pyramidsscientificofficecrm.util.SharedTag
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    //Check internet connection and save it in boolean value
    private val checkConnection: Boolean by lazy {
        InternetConnection(
            this
        ).isConnectToInternet
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Mack a timer for SplashScreen
        object : CountDownTimer(1000, 1000) {
            override fun onFinish() {
                appName.visibility = View.GONE
                loadingProgressBar.visibility = View.GONE
                rootView.setBackgroundColor(
                    ContextCompat.getColor(
                        this@LoginActivity,
                        R.color.white
                    )
                )
                bookIconImageView.setImageResource(R.drawable.ic_lancher)
                startAnimation()
            }

            override fun onTick(p0: Long) {}
        }.start()

        //if user and password is ok it will skip login activity
        checker()

        //on button login clicked
        loginButton.setOnClickListener {
            //check if username field is empty
            when {
                email.text!!.isEmpty() -> email.error = "Enter Email"
                //check if password field is empty
                password.text!!.isEmpty() -> password.error = "Enter Password"
                //if connection
                checkConnection -> login(email.text.toString(), password.text.toString())
                //if no connection
                else -> Toast.makeText(this, "Check Internet Connection", Toast.LENGTH_LONG).show()
            }//End When
        }//End OnClickListener

        //on Button Reset Password Clicked
        forgetPass.setOnClickListener {
            //Check internet connection using Interconnection::Class and save it in boolean value
            val check: Boolean = InternetConnection(this).isConnectToInternet
            when {
                //check if email is empty
                email.text!!.isEmpty() -> email.error = "Enter Your Email!"
                //if connection
                check -> reset(email.text.toString())
                //if no connection
                else -> Toast.makeText(this, Massages.connection, Toast.LENGTH_LONG).show()
            }//End When
        }//End OnClickListener
    }

    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
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
        bookIconImageView.animate().apply {
            x(50f)
            y(100f)
            duration = 1000
        }.setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationEnd(p0: Animator?) {
                afterAnimationView.visibility = View.VISIBLE
            }

            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationStart(p0: Animator?) {}
        })
    }

    //Skip Login Activity If User Save Login Data By Remember me CheckBox
    private fun checker() {
        val check = java.lang.Boolean.valueOf(
            Shared.readSharedBoolean(
                this,
                SharedTag.User_Found, false
            )
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
        LoginProgressBar.visibility = View.VISIBLE
        //Sign In
        val auth = FirebaseAuth.getInstance()
        //Firebase SignIn With Email And Password
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (checkbox.isChecked) {
                    Shared.saveSharedBoolean(
                        applicationContext,
                        SharedTag.User_Found,
                        true
                    ) //use this line to skip the activity
                }//End if (checkBox.isChecked)
                saveLoginData(auth.currentUser!!.uid)

            } else {
                //Remove LoginProgressBar
                LoginProgressBar.visibility = View.GONE
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
            FirebaseDBRepo.getUserData(
                id,
                onSuccess = {
                    Shared.saveSharedString(
                        applicationContext,
                        SharedTag.user_name,
                        it.user_name.toString()
                    )
                    Shared.saveSharedString(
                        applicationContext,
                        SharedTag.supervisor_ID,
                        it.supervisor_ID.toString()
                    )
                    Shared.saveSharedString(
                        applicationContext,
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