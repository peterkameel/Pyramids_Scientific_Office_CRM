package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.medical.fragmentMain

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peter_kameel.pyramidsscientificofficecrm.data.FirebaseDBRepo
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DailyVisitModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DoctorModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.HospitalModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel
    @Inject constructor(private var firebaseDBRepo: FirebaseDBRepo)
    : ViewModel() {
    val visitLiveData: MutableLiveData<ArrayList<DailyVisitModel>> by lazy { MutableLiveData<ArrayList<DailyVisitModel>>() }
    val weeklyPlanHospitalLiveData: MutableLiveData<ArrayList<HospitalModel>> by lazy { MutableLiveData<ArrayList<HospitalModel>>() }
    val weeklyPlanDoctorLiveData: MutableLiveData<ArrayList<DoctorModel>> by lazy { MutableLiveData<ArrayList<DoctorModel>>() }
    val massageLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun getVisitListBYDate(date: String) {
        CoroutineScope(Dispatchers.IO).launch {
            firebaseDBRepo.getVisitsList(
                date,
                onSuccess = {
                    visitLiveData.postValue(it)
                },
                onError = {
                    massageLiveData.postValue(it)
                }
            )
        }
    }

    fun getVisitListBYDateAndID(date: String,mID: String) {
        CoroutineScope(Dispatchers.IO).launch {
            firebaseDBRepo.getVisitListBYDateAndID(
                date,
                mID,
                onSuccess = {
                    visitLiveData.postValue(it)
                },
                onError = {
                    massageLiveData.postValue(it)
                }
            )
        }
    }

    fun getWeeklyPlanByDateAndID(date: String,mID: String){
        CoroutineScope(Dispatchers.IO).launch {
            firebaseDBRepo.getWeeklyPlanByDate(
                date,
                mID,
                onSuccess = {hospitalList,doctorList->
                    weeklyPlanHospitalLiveData.postValue(hospitalList)
                    weeklyPlanDoctorLiveData.postValue(doctorList)
                },
                onError = {
                    massageLiveData.postValue(it)
                }
            )
        }
    }
}