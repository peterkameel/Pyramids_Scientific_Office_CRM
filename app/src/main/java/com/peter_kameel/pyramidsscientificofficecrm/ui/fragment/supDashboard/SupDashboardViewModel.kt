package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.supDashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.peter_kameel.pyramidsscientificofficecrm.pojo.LoginModel

class SupDashboardViewModel: ViewModel() {

    val medicalLiveData: MutableLiveData<ArrayList<LoginModel>> by lazy { MutableLiveData<ArrayList<LoginModel>>() }

    private val database = Firebase.database.reference

    fun getListOfMedical(uid:String){
        database.child("USERS")
            .orderByChild("supervisor_ID").equalTo(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = ArrayList<LoginModel>()
                    for (postSnapshot in snapshot.children) {
                        if (postSnapshot.hasChildren()) {
                            postSnapshot.getValue<LoginModel>()?.let { list.add(it) }
                        }
                    }
                    medicalLiveData.postValue(list)
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
}