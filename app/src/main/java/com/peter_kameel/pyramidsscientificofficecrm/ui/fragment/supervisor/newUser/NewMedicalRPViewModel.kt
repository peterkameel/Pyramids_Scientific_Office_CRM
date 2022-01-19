package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.supervisor.newUser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.peter_kameel.pyramidsscientificofficecrm.data.FirebaseDBRepo
import com.peter_kameel.pyramidsscientificofficecrm.pojo.LoginModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewMedicalRPViewModel: ViewModel() {

    //ADD Account
    private val auth = FirebaseAuth.getInstance()

    val massageLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun saveMedicalRPData(name:String, uid:String){
        val currentUid = auth.currentUser?.uid
        val medical = LoginModel(name,currentUid,uid,"mr")
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseDBRepo.addMedicalRep(
                currentUid.toString(),
                medical,
                onSuccess = {
                    massageLiveData.postValue(it)
                },
                onError = {
                    massageLiveData.postValue(it)
                }
            )
        }
    }
}