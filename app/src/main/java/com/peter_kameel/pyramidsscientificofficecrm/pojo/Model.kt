package com.peter_kameel.pyramidsscientificofficecrm.pojo

data class AreaModel ( var name: String? = "")

data class DoctorModel (
    var name: String? = "",
    var specialization: String? = "",
    var area: String? = "",
    var latitude: String? = "",
    var longitude: String? = ""
        )

data class HospitalModel(
    var name: String? = "",
    var latitude: String? = "",
    var longitude: String? = ""
)

data class DailyVisitModel(
    var date: String? = "",
    var time: String? = "",
    var hospital: HospitalModel? = null ,
    var area: String? = "",
    var doctor: DoctorModel? = null,
    var comment: String? =""
)

data class LoginModel(
    var user_name: String? ="",
    var id: String? ="",
    var supervisor_ID: String? = "",
    var permission: String? = ""
)

data class WeeklyPlanModel(
    var AM: ArrayList<HospitalModel>? = null,
    var PM: ArrayList<DoctorModel>? = null
)

data class WeeklyPlanRecyclerModel(
    var name: String? = ""
)
