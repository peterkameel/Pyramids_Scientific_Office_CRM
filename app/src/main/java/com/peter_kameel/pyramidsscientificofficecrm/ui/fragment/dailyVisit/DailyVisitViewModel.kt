package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.dailyVisit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.peter_kameel.pyramidsscientificofficecrm.pojo.AreaModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DailyVisitModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DoctorModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.HospitalModel
import kotlin.random.Random

class DailyVisitViewModel: ViewModel() {

    val hospitalLiveData: MutableLiveData<ArrayList<HospitalModel>> by lazy { MutableLiveData<ArrayList<HospitalModel>>()}

    val areaLiveData: MutableLiveData<ArrayList<AreaModel>> by lazy { MutableLiveData<ArrayList<AreaModel>>()}

    val doctorLiveData: MutableLiveData<ArrayList<DoctorModel>> by lazy { MutableLiveData<ArrayList<DoctorModel>>()}

    val singleDoctorLiveData: MutableLiveData<ArrayList<DoctorModel>> by lazy { MutableLiveData<ArrayList<DoctorModel>>()}

    val singleHospitalLiveData: MutableLiveData<ArrayList<HospitalModel>> by lazy { MutableLiveData<ArrayList<HospitalModel>>()}
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

    fun getDoctorList(doctorArea: String ,uid: String) {
        database.child("USERS")
            .child(uid)
            .child("Doctor")
            .orderByChild("area").equalTo(doctorArea)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = ArrayList<DoctorModel>()
                    for (postSnapshot in snapshot.children) {
                        if (postSnapshot.hasChildren()){
                            postSnapshot.getValue<DoctorModel>()?.let { list.add(it) }
                        }
                    }
                    doctorLiveData.postValue(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun getSingleDoctor(doctorName: String ,uid: String){
        database.child("USERS")
            .child(uid)
            .child("Doctor")
            .orderByChild("name").equalTo(doctorName)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = ArrayList<DoctorModel>()
                    for (postSnapshot in snapshot.children) {
                        if (postSnapshot.hasChildren()){
                            postSnapshot.getValue<DoctorModel>()?.let { list.add(it) }
                        }
                    }
                    singleDoctorLiveData.postValue(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun getSingleHospital(hospitalName: String ,uid: String){
        database.child("USERS")
            .child(uid)
            .child("Hospital")
            .orderByChild("name").equalTo(hospitalName)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = ArrayList<HospitalModel>()
                    for (postSnapshot in snapshot.children) {
                        if (postSnapshot.hasChildren()){
                            postSnapshot.getValue<HospitalModel>()?.let { list.add(it) }
                        }
                    }
                    singleHospitalLiveData.postValue(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun saveNewVisit(visit: DailyVisitModel,uid: String) {
        database.child("USERS")
            .child(uid)
            .child("Visit")
            .child(visit.date.toString())
            .child(Random.nextInt(0, 100).toString())
            .setValue(visit)
    }
}