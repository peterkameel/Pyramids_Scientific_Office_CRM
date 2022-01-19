package com.peter_kameel.pyramidsscientificofficecrm.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.peter_kameel.pyramidsscientificofficecrm.pojo.*
import com.peter_kameel.pyramidsscientificofficecrm.util.Massages

object FirebaseDBRepo {

    private val db = Firebase.firestore
    private val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()

    //General functions
    fun getAreaList(
        onSuccess: (ArrayList<AreaModel>) -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("Users")
            .document(uid)
            .collection("Area")
            .addSnapshotListener { it, e ->
                if (e != null) {
                    onError(e.toString())
                    return@addSnapshotListener
                }
                val list = ArrayList<AreaModel>()
                for (doc in it!!) {
                    doc.let { data -> list.add(data.toObject(AreaModel::class.java)) }
                }
                onSuccess(list)
            }
    }

    fun getHospitalList(
        onSuccess: (ArrayList<HospitalModel>) -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("Users")
            .document(uid)
            .collection("Hospitals")
            .addSnapshotListener { it, e ->
                if (e != null) {
                    onError(e.toString())
                    return@addSnapshotListener
                }
                val list = ArrayList<HospitalModel>()
                for (doc in it!!) {
                    doc.let { data -> list.add(data.toObject(HospitalModel::class.java)) }
                }
                onSuccess(list)
            }
    }

    fun getDoctorList(
        onSuccess: (ArrayList<DoctorModel>) -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("Users")
            .document(uid)
            .collection("Doctors")
            .addSnapshotListener { it, e ->
                if (e != null) {
                    onError(e.toString())
                    return@addSnapshotListener
                }
                val list = ArrayList<DoctorModel>()
                for (doc in it!!) {
                    doc.let { data -> list.add(data.toObject(DoctorModel::class.java)) }
                }
                onSuccess(list)
            }
    }

    fun getSingleDoctor(
        doctorName: String,
        onSuccess: (ArrayList<DoctorModel>) -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("Users")
            .document(uid)
            .collection("Doctors")
            .whereEqualTo("name", doctorName)
            .addSnapshotListener { it, e ->
                if (e != null) {
                    onError(e.toString())
                    return@addSnapshotListener
                }
                val list = ArrayList<DoctorModel>()
                for (doc in it!!) {
                    doc.let { data -> list.add(data.toObject(DoctorModel::class.java)) }
                }
                onSuccess(list)
            }
    }

    fun getSingleHospital(
        hospitalName: String,
        onSuccess: (ArrayList<HospitalModel>) -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("Users")
            .document(uid)
            .collection("Hospitals")
            .whereEqualTo("name", hospitalName)
            .addSnapshotListener { it, e ->
                if (e != null) {
                    onError(e.toString())
                    return@addSnapshotListener
                }
                val list = ArrayList<HospitalModel>()
                for (doc in it!!) {
                    doc.let { data -> list.add(data.toObject(HospitalModel::class.java)) }
                }
                onSuccess(list)
            }
    }

    //Login activity
    fun getUserData(
        id: String,
        onSuccess: (LoginModel) -> Unit) {
        // Add a new document with a generated ID
        db.collection("Users")
            .document(id)
            .get()
            .addOnSuccessListener {
                val user = it.toObject(LoginModel::class.java)
                onSuccess(user!!)
            }
    }

    //NewAreaFragment functions
    fun addNewArea(
        area: AreaModel,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        // Add a new document with a generated ID
        db.collection("Users")
            .document(uid)
            .collection("Area")
            .add(area)
            .addOnSuccessListener {
                onSuccess(Massages.successful)
            }
            .addOnFailureListener { e ->
                onError(e.toString())
            }
    }

    //NewHospitalFragment functions
    fun addNewHospital(
        hospital: HospitalModel,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        // Add a new document with a generated ID
        db.collection("Users")
            .document(uid)
            .collection("Hospitals")
            .add(hospital)
            .addOnSuccessListener {
                onSuccess(Massages.successful)
            }
            .addOnFailureListener { e ->
                onError(e.toString())
            }
    }

    //NewDoctorFragment functions
    fun addNewDoctor(
        doctor: DoctorModel,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        // Add a new document with a generated ID
        db.collection("Users")
            .document(uid)
            .collection("Doctors")
            .add(doctor)
            .addOnSuccessListener {
                onSuccess(Massages.successful)
            }
            .addOnFailureListener { e ->
                onError(e.toString())
            }
    }

    //WeeklyPlanFragment functions
    fun createPlan(
        date: String, plan: WeeklyPlanModel,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        // Add a new document with a generated ID
        db.collection("Users")
            .document(uid)
            .collection("Plan")
            .document(date)
            .set(plan)
            .addOnSuccessListener {
                onSuccess(Massages.successful)
            }
            .addOnFailureListener { e ->
                onError(e.toString())
            }
    }

    fun getDoctorListByArea(
        area: String,
        onSuccess: (ArrayList<DoctorModel>) -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("Users")
            .document(uid)
            .collection("Doctors")
            .whereEqualTo("area", area)
            .addSnapshotListener { it, e ->
                if (e != null) {
                    onError(e.toString())
                    return@addSnapshotListener
                }
                val list = ArrayList<DoctorModel>()
                for (doc in it!!) {
                    doc.let { data -> list.add(data.toObject(DoctorModel::class.java)) }
                }
                onSuccess(list)
            }
    }

    //DailyVisitFragment functions
    fun saveNewVisits(
        visit: DailyVisitModel,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        // Add a new document with a generated ID
        db.collection("Users")
            .document(uid)
            .collection("Visits")
            .document(visit.date.toString())
            .collection(visit.date.toString())
            .add(visit)
            .addOnSuccessListener {
                onSuccess(Massages.successful)
            }
            .addOnFailureListener { e ->
                onError(e.toString())
            }
    }


    //Main Fragment
    fun getVisitsList(
        date: String,
        onSuccess: (ArrayList<DailyVisitModel>) -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("Users")
            .document(uid)
            .collection("Visits")
            .document(date)
            .collection(date)
            .addSnapshotListener { it, e ->
                if (e != null) {
                    onError(e.toString())
                    return@addSnapshotListener
                }
                val list = ArrayList<DailyVisitModel>()
                for (doc in it!!) {
                    doc.let { data -> list.add(data.toObject(DailyVisitModel::class.java)) }
                }
                onSuccess(list)
            }
    }

    fun getVisitListBYDateAndID(
        date: String,
        mID: String,
        onSuccess: (ArrayList<DailyVisitModel>) -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("Users")
            .document(mID)
            .collection("Visits")
            .document(date)
            .collection(date)
            .addSnapshotListener { it, e ->
                if (e != null) {
                    onError(e.toString())
                    return@addSnapshotListener
                }
                val list = ArrayList<DailyVisitModel>()
                for (doc in it!!) {
                    doc.let { data -> list.add(data.toObject(DailyVisitModel::class.java)) }
                }
                onSuccess(list)
            }
    }

    fun getWeeklyPlanByDate(
        date: String,
        medicalRPID: String,
        onSuccess: (ArrayList<HospitalModel>, ArrayList<DoctorModel>) -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("Users")
            .document(medicalRPID)
            .collection("Plan")
            .document(date)
            .get()
            .addOnSuccessListener {
                val list = it.toObject(WeeklyPlanModel::class.java)
                onSuccess(list?.AM!!, list.PM!!)
            }
            .addOnFailureListener {
                onError(it.toString())
            }
    }

    //Medical Rep. List
    fun getDoctorListByMedicalID(
        id: String,
        onSuccess: (ArrayList<DoctorModel>) -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("Users")
            .document(id)
            .collection("Doctors")
            .addSnapshotListener { it, e ->
                if (e != null) {
                    onError(e.toString())
                    return@addSnapshotListener
                }
                val list = ArrayList<DoctorModel>()
                for (doc in it!!) {
                    doc.let { data -> list.add(data.toObject(DoctorModel::class.java)) }
                }
                onSuccess(list)
            }
    }

    fun getHospitalsListByMedicalID(
        id: String,
        onSuccess: (ArrayList<HospitalModel>) -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("Users")
            .document(id)
            .collection("Hospitals")
            .addSnapshotListener { it, e ->
                if (e != null) {
                    onError(e.toString())
                    return@addSnapshotListener
                }
                val list = ArrayList<HospitalModel>()
                for (doc in it!!) {
                    doc.let { data -> list.add(data.toObject(HospitalModel::class.java)) }
                }
                onSuccess(list)
            }
    }

    fun addMedicalRep(
        id: String,
        medicalRP: LoginModel,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("Users")
            .document(id)
            .set(medicalRP)
            .addOnSuccessListener {
                onSuccess(Massages.successful)
            }
            .addOnFailureListener { e ->
                onError(e.toString())
            }
    }

    //SuperVisor Dashboard
    fun getListOfMedicalBySupID(
        onSuccess: (ArrayList<LoginModel>) -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("Users")
            .whereEqualTo("supervisor_ID", uid)
            .addSnapshotListener { it, e ->
                if (e != null) {
                    onError(e.toString())
                    return@addSnapshotListener
                }
                val list = ArrayList<LoginModel>()
                for (doc in it!!) {
                    doc.let { data -> list.add(data.toObject(LoginModel::class.java)) }
                }
                onSuccess(list)
            }
    }
}