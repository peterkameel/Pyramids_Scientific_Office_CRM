package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.medical.weeklyPlan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peter_kameel.pyramidsscientificofficecrm.data.FirebaseDBRepo
import com.peter_kameel.pyramidsscientificofficecrm.pojo.AreaModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DoctorModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.HospitalModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.WeeklyPlanModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeeklyPlanViewModel
@Inject constructor(private var firebaseDBRepo: FirebaseDBRepo) : ViewModel() {
    val hospitalLiveData: MutableLiveData<ArrayList<HospitalModel>> by lazy { MutableLiveData<ArrayList<HospitalModel>>() }
    val areaLiveData: MutableLiveData<ArrayList<AreaModel>> by lazy { MutableLiveData<ArrayList<AreaModel>>() }
    val doctorLiveData: MutableLiveData<ArrayList<DoctorModel>> by lazy { MutableLiveData<ArrayList<DoctorModel>>() }
    val massageLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val singleDoctorLiveData: MutableLiveData<ArrayList<DoctorModel>> by lazy { MutableLiveData<ArrayList<DoctorModel>>() }
    val singleHospitalLiveData: MutableLiveData<ArrayList<HospitalModel>> by lazy { MutableLiveData<ArrayList<HospitalModel>>() }

    fun getHospitalList() {
        CoroutineScope(Dispatchers.IO).launch {
            firebaseDBRepo.getHospitalList(
                onSuccess = {
                    hospitalLiveData.postValue(it)
                },
                onError = {
                    massageLiveData.postValue(it)
                }
            )
        }
    }

    fun getAreaList() {
        CoroutineScope(Dispatchers.IO).launch {
            firebaseDBRepo.getAreaList(onSuccess = {
                areaLiveData.postValue(it)
            }, onError = {

            })
        }
    }

    fun getDoctorListByArea(area: String) {
        CoroutineScope(Dispatchers.IO).launch {
            firebaseDBRepo.getDoctorListByArea(
                area,
                onSuccess = {
                    doctorLiveData.postValue(it)
                },
                onError = {
                    massageLiveData.postValue(it)
                }
            )
        }
    }

    fun getSingleDoctor(doctorName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            firebaseDBRepo.getSingleDoctor(
                doctorName,
                onSuccess = {
                    singleDoctorLiveData.postValue(it)
                },
                onError = {
                    massageLiveData.postValue(it)
                }
            )
        }
    }

    fun getSingleHospital(hospitalName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            firebaseDBRepo.getSingleHospital(
                hospitalName,
                onSuccess = {
                    singleHospitalLiveData.postValue(it)
                },
                onError = {
                    massageLiveData.postValue(it)
                }
            )
        }
    }

    fun createPlan(date: String, plan: WeeklyPlanModel) {
        CoroutineScope(Dispatchers.IO).launch {
            firebaseDBRepo.createPlan(
                date,
                plan,
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