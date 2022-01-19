package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.medical.newDoctor

import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peter_kameel.pyramidsscientificofficecrm.data.FirebaseDBRepo
import com.peter_kameel.pyramidsscientificofficecrm.helper.CheckLocation
import com.peter_kameel.pyramidsscientificofficecrm.pojo.AreaModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DoctorModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewDoctorViewModel: ViewModel(){

    val doctorLiveData: MutableLiveData<ArrayList<DoctorModel>> by lazy { MutableLiveData<ArrayList<DoctorModel>>() }
    val areaLiveData: MutableLiveData<ArrayList<AreaModel>> by lazy { MutableLiveData<ArrayList<AreaModel>>() }
    val locationLiveData: MutableLiveData<Location> by lazy { MutableLiveData<Location>() }
    val massageLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun getAreaList() {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseDBRepo.getAreaList(onSuccess = {
                areaLiveData.postValue(it)
            }, onError = {

            })
        }
    }

    fun getDoctorList() {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseDBRepo.getDoctorList(
                onSuccess = {
                    doctorLiveData.postValue(it)
                },
                onError = {
                    massageLiveData.postValue(it)
                }
            )
        }
    }

    fun saveNewDoctor(doctor: DoctorModel) {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseDBRepo.addNewDoctor(doctor, onSuccess = {
                massageLiveData.postValue(it)
            }, onError = {
                massageLiveData.postValue(it)
            })
        }
    }

    fun getLocation(ctx: Context){
        CoroutineScope(Dispatchers.Main).launch {
            CheckLocation.getCurrent1location(ctx, onSuccess = {
                locationLiveData.postValue(it)
            }, onError = {
                massageLiveData.postValue(it)
            })
        }
    }
}