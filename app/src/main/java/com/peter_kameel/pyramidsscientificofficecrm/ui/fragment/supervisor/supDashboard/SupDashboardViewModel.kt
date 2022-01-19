package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.supervisor.supDashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peter_kameel.pyramidsscientificofficecrm.data.FirebaseDBRepo
import com.peter_kameel.pyramidsscientificofficecrm.pojo.LoginModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SupDashboardViewModel: ViewModel() {

    val medicalLiveData: MutableLiveData<ArrayList<LoginModel>> by lazy { MutableLiveData<ArrayList<LoginModel>>() }
    val massageLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun getListOfMedical(){
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseDBRepo.getListOfMedicalBySupID(
                onSuccess = {
                    medicalLiveData.postValue(it)
                },
                onError = {
                    massageLiveData.postValue(it)
                }
            )
        }
    }
}