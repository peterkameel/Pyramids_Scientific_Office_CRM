package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.medical.newArea


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peter_kameel.pyramidsscientificofficecrm.data.FirebaseDBRepo
import com.peter_kameel.pyramidsscientificofficecrm.pojo.AreaModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewAreaViewModel: ViewModel() {

    val areaLiveData: MutableLiveData<ArrayList<AreaModel>> by lazy { MutableLiveData<ArrayList<AreaModel>>() }
    val massageLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun getAreaList() {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseDBRepo.getAreaList(onSuccess = {
                areaLiveData.postValue(it)
            }, onError = {

            })
        }
    }

    fun saveNewArea(area: AreaModel) {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseDBRepo.addNewArea(area, onSuccess = {
                massageLiveData.postValue(it)
            }, onError = {
                massageLiveData.postValue(it)
            })
        }
    }
}