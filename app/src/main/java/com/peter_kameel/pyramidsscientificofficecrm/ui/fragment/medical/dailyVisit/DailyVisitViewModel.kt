package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.medical.dailyVisit

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peter_kameel.pyramidsscientificofficecrm.data.FirebaseDBRepo
import com.peter_kameel.pyramidsscientificofficecrm.helper.objects.CheckLocation
import com.peter_kameel.pyramidsscientificofficecrm.pojo.AreaModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DailyVisitModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DoctorModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.HospitalModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyVisitViewModel
@Inject constructor(
    private val firebaseDBRepo: FirebaseDBRepo,
    private val checkLocation: CheckLocation
    ) : ViewModel() {
    val hospitalLiveData: MutableLiveData<ArrayList<HospitalModel>> by lazy { MutableLiveData<ArrayList<HospitalModel>>() }
    val areaLiveData: MutableLiveData<ArrayList<AreaModel>> by lazy { MutableLiveData<ArrayList<AreaModel>>() }
    val doctorLiveData: MutableLiveData<ArrayList<DoctorModel>> by lazy { MutableLiveData<ArrayList<DoctorModel>>() }
    val singleDoctorLiveData: MutableLiveData<DoctorModel> by lazy { MutableLiveData<DoctorModel>() }
    val singleHospitalLiveData: MutableLiveData<HospitalModel> by lazy { MutableLiveData<HospitalModel>() }
    val distanceLiveData: MutableLiveData<Double> by lazy { MutableLiveData<Double>() }
    val massageLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }

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
                    for (item in it){
                        singleDoctorLiveData.postValue(item)
                    }
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
                    for (item in it){
                        singleHospitalLiveData.postValue(item)
                    }
                },
                onError = {
                    massageLiveData.postValue(it)
                }
            )
        }
    }

    fun saveNewVisit(visit: DailyVisitModel) {
        CoroutineScope(Dispatchers.IO).launch {
            firebaseDBRepo.saveNewVisits(
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
        checkLocation.getDistanceTotLocation(ctx, lat, lon, onSuccess = {
            distanceLiveData.postValue(it)
        }, onError = {
            massageLiveData.postValue(it)
        })
    }

}