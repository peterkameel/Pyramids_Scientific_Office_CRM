package com.peter_kameel.pyramidsscientificofficecrm.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.*
import com.google.firebase.ktx.Firebase
import com.peter_kameel.pyramidsscientificofficecrm.pojo.*
import com.peter_kameel.pyramidsscientificofficecrm.util.Massages
import com.peter_kameel.pyramidsscientificofficecrm.util.Shared
import com.peter_kameel.pyramidsscientificofficecrm.util.SharedTag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseDBRepo
@Inject constructor(
    private val shared: Shared
){
    private val db = Firebase.firestore
    private val uid: String by lazy {
        shared.readSharedString(
            SharedTag.UID,
            FirebaseAuth.getInstance().currentUser!!.uid)
            .toString()
    }

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
                var hospitalList = ArrayList<HospitalModel>()
                var doctorList = ArrayList<DoctorModel>()
                val list = it.toObject(WeeklyPlanModel::class.java)
                if (list != null) {
                    hospitalList = list.AM!!
                    doctorList = list.PM!!
                }
                onSuccess(hospitalList, doctorList)
            }
            .addOnFailureListener {
                onError(it.toString())
            }
    }

    fun getWeeklyPlanByDateList(
        dates: ArrayList<String>,
        medicalRPID: String,
        onSuccess: (ArrayList<WeeklyPlanModel>) -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("Users")
            .document(medicalRPID)
            .collection("Plan")
            .whereIn("date", dates)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    onError(error.toString())
                    return@addSnapshotListener
                }
                val list = ArrayList<WeeklyPlanModel>()
                for (doc in value!!) {
                    doc.let { data -> list.add(data.toObject(WeeklyPlanModel::class.java)) }
                }
                onSuccess(list)
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
        id: String,
        onSuccess: (ArrayList<LoginModel>) -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("Users")
            .whereEqualTo("supervisor_ID", id)
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