package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.supervisor.supDashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peter_kameel.pyramidsscientificofficecrm.data.FirebaseDBRepo
import com.peter_kameel.pyramidsscientificofficecrm.pojo.LoginModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupDashboardViewModel
@Inject constructor(private var firebaseDBRepo: FirebaseDBRepo) : ViewModel() {
    val medicalLiveData: MutableLiveData<ArrayList<LoginModel>> by lazy { MutableLiveData<ArrayList<LoginModel>>() }
    val massageLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun getListOfMedical(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            firebaseDBRepo.getListOfMedicalBySupID(
                id,
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