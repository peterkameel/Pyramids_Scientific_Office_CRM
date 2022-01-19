package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.supervisor.newUser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.helper.InterConn.InternetConnection
import com.peter_kameel.pyramidsscientificofficecrm.util.Massages
import com.peter_kameel.pyramidsscientificofficecrm.util.Shared
import com.peter_kameel.pyramidsscientificofficecrm.util.SharedTag
import kotlinx.android.synthetic.main.create_new_user.view.*

class NewMedicalRP : Fragment() {
    private val viewModel by viewModels<NewMedicalRPViewModel>()
    private var userID: String? = null

    //ADD Account
    private val auth = FirebaseAuth.getInstance()

    //Check internet connection and save it in boolean value
    private val checkConnection: Boolean by lazy {
        InternetConnection(
            context
        ).isConnectToInternet
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.create_new_user, container, false)
        val uid = Shared.readSharedString(context!!, SharedTag.UID, "false").toString()
        userID = uid
        //create new user
        view.Create_New_User_Button.setOnClickListener {
            when {
                view.New_User_Name_TextFiled.text.isNullOrEmpty() -> {
                    Toast.makeText(context, Massages.medicalName, Toast.LENGTH_LONG).show()
                }
                view.New_User_Password_TextFiled.text.isNullOrEmpty() -> {
                    Toast.makeText(context, Massages.medicalPassword, Toast.LENGTH_LONG).show()
                }
                view.New_User_Email_TextFiled.text.isNullOrEmpty() -> {
                    Toast.makeText(context, Massages.medicalEmail, Toast.LENGTH_LONG).show()
                }
                !checkConnection -> {
                    Toast.makeText(context, Massages.connection, Toast.LENGTH_LONG).show()
                }
                else -> {
                    createNewMedicalRP(
                        view.New_User_Email_TextFiled.text.toString(),
                        view.New_User_Password_TextFiled.text.toString(),
                        view.New_User_Name_TextFiled.text.toString()
                    )
                }
            }
        }
        //on user add successfully
        viewModel.massageLiveData.observeForever {
            if (it == Massages.successful){
                clearView(view)
            }
        }
        return view
    }

    private fun createNewMedicalRP(email: String, password: String, name: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewModel.saveMedicalRPData(name, userID.toString())
                } else {
                    Toast.makeText(context, Massages.error, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun clearView(view: View){
        view.New_User_Name_TextFiled.text?.clear()
        view.New_User_Password_TextFiled.text?.clear()
        view.New_User_Email_TextFiled.text?.clear()
        Toast.makeText(context,Massages.successful,Toast.LENGTH_LONG).show()
    }
}