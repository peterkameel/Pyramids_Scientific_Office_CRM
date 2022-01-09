package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.medical.newHospital

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.peter_kameel.pyramidsscientificofficecrm.pojo.HospitalModel

class NewHospitalViewModel: ViewModel() {

    val hospitalLiveData: MutableLiveData<ArrayList<HospitalModel>> by lazy { MutableLiveData<ArrayList<HospitalModel>>() }

    private val database = Firebase.database.reference

    fun getHospitalList(uid: String) {
        database.child("USERS")
            .child(uid)
            .child("Hospital")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = ArrayList<HospitalModel>()
                    for (postSnapshot in snapshot.children) {
                        if (postSnapshot.hasChildren()){
                            postSnapshot.getValue<HospitalModel>()?.let { list.add(it) }
                        }
                    }
                    hospitalLiveData.postValue(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun saveNewHospital(hospital: HospitalModel,uid: String) {
        database.child("USERS")
            .child(uid)
            .child("Hospital")
            .child(hospital.name.toString())
            .setValue(hospital)
    }

}