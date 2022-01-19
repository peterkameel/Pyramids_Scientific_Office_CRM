package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.medical.dailyVisit

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peter_kameel.pyramidsscientificofficecrm.data.FirebaseDBRepo
import com.peter_kameel.pyramidsscientificofficecrm.helper.CheckLocation
import com.peter_kameel.pyramidsscientificofficecrm.pojo.AreaModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DailyVisitModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DoctorModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.HospitalModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DailyVisitViewModel: ViewModel() {

    val hospitalLiveData: MutableLiveData<ArrayList<HospitalModel>> by lazy { MutableLiveData<ArrayList<HospitalModel>>() }
    val areaLiveData: MutableLiveData<ArrayList<AreaModel>> by lazy { MutableLiveData<ArrayList<AreaModel>>() }
    val doctorLiveData: MutableLiveData<ArrayList<DoctorModel>> by lazy { MutableLiveData<ArrayList<DoctorModel>>() }
    val singleDoctorLiveData: MutableLiveData<ArrayList<DoctorModel>> by lazy { MutableLiveData<ArrayList<DoctorModel>>() }
    val singleHospitalLiveData: MutableLiveData<ArrayList<HospitalModel>> by lazy { MutableLiveData<ArrayList<HospitalModel>>() }
    val distanceLiveData: MutableLiveData<Double> by lazy { MutableLiveData<Double>() }
    val massageLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun getHospitalList() {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseDBRepo.getHospitalList(
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
            FirebaseDBRepo.getAreaList(onSuccess = {
                areaLiveData.postValue(it)
            }, onError = {

            })
        }
    }

    fun getDoctorListByArea(area: String) {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseDBRepo.getDoctorListByArea(
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
            FirebaseDBRepo.getSingleDoctor(
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
            FirebaseDBRepo.getSingleHospital(
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

    fun saveNewVisit(visit: DailyVisitModel) {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseDBRepo.saveNewVisits(
                visit,
                onSuccess = {
                    massageLiveData.postValue(it)
                },
                onError = {
                    massageLiveData.postValue(it)
                }
            )
        }
    }


    suspend fun checkDistance(ctx: Context, lat: Double, lon: Double) {
        CheckLocation.getDistanceTotLocation(ctx, lat, lon, onSuccess = {
            distanceLiveData.postValue(it)
        }, onError = {
            massageLiveData.postValue(it)
        })
    }

}