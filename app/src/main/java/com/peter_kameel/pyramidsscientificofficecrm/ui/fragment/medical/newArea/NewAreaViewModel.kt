package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.medical.newArea


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.peter_kameel.pyramidsscientificofficecrm.pojo.AreaModel

class NewAreaViewModel: ViewModel() {

    val areaLiveData: MutableLiveData<ArrayList<AreaModel>> by lazy { MutableLiveData<ArrayList<AreaModel>>() }

    private val database = Firebase.database.reference

    fun getAreaList(uid: String) {
        database.child("USERS")
            .child(uid)
            .child("Area")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = ArrayList<AreaModel>()
                    for (postSnapshot in snapshot.children) {
                        if (postSnapshot.hasChildren()){
                            postSnapshot.getValue<AreaModel>()?.let { list.add(it) }
                        }
                    }
                    areaLiveData.postValue(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun saveNewArea(areaName: String,uid: String) {
        val area = AreaModel(areaName)
        database.child("USERS")
            .child(uid)
            .child("Area")
            .child(areaName)
            .setValue(area)
    }

}