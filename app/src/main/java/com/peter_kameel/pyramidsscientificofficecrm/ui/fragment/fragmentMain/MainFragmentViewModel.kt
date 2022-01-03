package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.fragmentMain

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DailyVisitModel

class MainFragmentViewModel: ViewModel() {

    val visitLiveData: MutableLiveData<ArrayList<DailyVisitModel>> by lazy { MutableLiveData<ArrayList<DailyVisitModel>>() }

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
}