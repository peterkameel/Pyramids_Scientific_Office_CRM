package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.medical.newHospital

import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peter_kameel.pyramidsscientificofficecrm.data.FirebaseDBRepo
import com.peter_kameel.pyramidsscientificofficecrm.helper.objects.CheckLocation
import com.peter_kameel.pyramidsscientificofficecrm.pojo.HospitalModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewHospitalViewModel
@Inject constructor(
    private var firebaseDBRepo: FirebaseDBRepo,
    private val checkLocation: CheckLocation
    ) : ViewModel() {
    val hospitalLiveData: MutableLiveData<ArrayList<HospitalModel>> by lazy { MutableLiveData<ArrayList<HospitalModel>>() }
    val massageLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val locationLiveData: MutableLiveData<Location> by lazy { MutableLiveData<Location>() }
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

    fun saveNewHospital(hospital: HospitalModel) {
        CoroutineScope(Dispatchers.IO).launch {
            firebaseDBRepo.addNewHospital(
                hospital,
                onSuccess = {
                    massageLiveData.postValue(it)
                }, onError = {
                    massageLiveData.postValue(it)
                }
            )
        }
    }

    fun getLocation(ctx: Context){
        CoroutineScope(Dispatchers.Main).launch {
            checkLocation.getCurrentLocation(ctx, onSuccess = {
                locationLiveData.postValue(it)
            }, onError = {
                massageLiveData.postValue(it)
            })
        }
    }
}