package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.medical.fragmentMain

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DailyVisitModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DoctorModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.HospitalModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.WeeklyPlanModel

class MainFragmentViewModel: ViewModel() {

    val visitLiveData: MutableLiveData<ArrayList<DailyVisitModel>> by lazy { MutableLiveData<ArrayList<DailyVisitModel>>() }

    val weeklyPlanHospitalLiveData: MutableLiveData<ArrayList<HospitalModel>> by lazy { MutableLiveData<ArrayList<HospitalModel>>() }

    val weeklyPlanDoctorLiveData: MutableLiveData<ArrayList<DoctorModel>> by lazy { MutableLiveData<ArrayList<DoctorModel>>() }

    private val database = Firebase.database.reference

    fun getVisitList(date: String, uid: String) {
        database.child("USERS")
            .child(uid)
            .child("Visit")
            .child(date)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = ArrayList<DailyVisitModel>()
                    for (postSnapshot in snapshot.children) {
                        if (postSnapshot.hasChildren()) {
                            postSnapshot.getValue<DailyVisitModel>()?.let { list.add(it) }
                        }
                    }
                    visitLiveData.postValue(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    fun getPlanListHospital(date: String, uid: String) {
        database.child("USERS")
            .child(uid)
            .child("Plan")
            .child(date)
            .child("am")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = ArrayList<HospitalModel>()
                    for (postSnapshot in snapshot.children) {
                        if (postSnapshot.hasChildren()) {
                            postSnapshot.getValue<HospitalModel>()?.let { list.add(it) }
                        }
                    }
                    weeklyPlanHospitalLiveData.postValue(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun getPlanListDoctor(date: String, uid: String) {
        database.child("USERS")
            .child(uid)
            .child("Plan")
            .child(date)
            .child("pm")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = ArrayList<DoctorModel>()
                    for (postSnapshot in snapshot.children) {
                        if (postSnapshot.hasChildren()) {
                            postSnapshot.getValue<DoctorModel>()?.let { list.add(it) }
                        }
                    }
                    weeklyPlanDoctorLiveData.postValue(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}