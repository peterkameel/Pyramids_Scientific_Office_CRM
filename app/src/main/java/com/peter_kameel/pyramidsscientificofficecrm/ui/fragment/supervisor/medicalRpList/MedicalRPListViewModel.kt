package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.supervisor.medicalRpList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peter_kameel.pyramidsscientificofficecrm.data.FirebaseDBRepo
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DoctorModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.HospitalModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicalRPListViewModel
@Inject constructor(private var firebaseDBRepo: FirebaseDBRepo) : ViewModel() {
    val doctorLiveData: MutableLiveData<ArrayList<DoctorModel>> by lazy { MutableLiveData<ArrayList<DoctorModel>>() }
    val hospitalLiveData: MutableLiveData<ArrayList<HospitalModel>> by lazy { MutableLiveData<ArrayList<HospitalModel>>() }
    val massageLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun getListOfDoctors(uid: String) {
        CoroutineScope(Dispatchers.IO).launch {
            firebaseDBRepo.getDoctorListByMedicalID(
                uid,
                onSuccess = {
                    doctorLiveData.postValue(it)
                },
                onError = {
                    massageLiveData.postValue(it)
                }
            )
        }
    }

    fun getListOfHospitals(uid: String) {
        CoroutineScope(Dispatchers.IO).launch {
            firebaseDBRepo.getHospitalsListByMedicalID(
                uid,
                onSuccess = {
                    hospitalLiveData.postValue(it)
                },
                onError = {
                    massageLiveData.postValue(it)
                }
            )
        }
    }
}