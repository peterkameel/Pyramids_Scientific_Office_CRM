package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.supervisor.newUser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.peter_kameel.pyramidsscientificofficecrm.pojo.LoginModel
import com.peter_kameel.pyramidsscientificofficecrm.util.Massages

class NewMedicalRPViewModel: ViewModel() {
    private val database = Firebase.database.reference
    //ADD Account
    private val auth = FirebaseAuth.getInstance()

    val successLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun saveMedicalRPData(name:String, uid:String){
        val currentUid = auth.currentUser?.uid
        val medical = LoginModel(name,currentUid,uid,"mr")
        database.child("USERS")
            .child(currentUid.toString())
            .setValue(medical)
        successLiveData.postValue(Massages.successful)
    }
}