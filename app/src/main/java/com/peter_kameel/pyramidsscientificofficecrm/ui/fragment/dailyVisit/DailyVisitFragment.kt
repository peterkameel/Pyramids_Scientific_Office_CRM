package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.dailyVisit

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DailyVisitModel
import com.peter_kameel.pyramidsscientificofficecrm.util.Shared
import com.peter_kameel.pyramidsscientificofficecrm.util.SharedTag
import kotlinx.android.synthetic.main.daily_visit.view.*
import kotlinx.android.synthetic.main.daily_visit.view.Daily_Visit_Hospital_TextInputLayout
import kotlinx.android.synthetic.main.daily_visit.view.Location_Visit_Button
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

class DailyVisitFragment : Fragment() {
    private val viewModel by viewModels<DailyVisitViewModel>()
    private var userID: String? = null
    private var visitDate: String? = null
    //initialize for google play location services
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val cts = CancellationTokenSource()
    private var gpsEnabled: Boolean = false

    var doctorLatitude: Double = 0.0
    var doctorLongitude: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.daily_visit, container, false)
        val uid = Shared.readSharedString(context!!, SharedTag.UID, "false").toString()
        userID = uid

        //get location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)

        //set location manger
        val locationManger = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        //show date picker
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        //on chose date
        datePicker.addOnPositiveButtonClickListener {
            visitDate = convertLongToTime(view, it)
        }
        //get date of visit
        view.WeeklyPlan_Date_Button.setOnClickListener {
            datePicker.show(parentFragmentManager, "tag")
        }
        val am = arrayOf("AM", "PM")
        val adapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, am)
        view.daily_visit_AM.setAdapter(adapter)
        //on choose AM or PM
        view.daily_visit_AM.doAfterTextChanged {
            if (it.toString() == "AM") {
                viewModel.getHospitalList(uid)
                view.Daily_Visit_Hospital_TextInputLayout.isEnabled = true
                view.Daily_visit_Area_TextInputLayout.isEnabled = false
                view.Daily_Visit_Doctor_TextInputLayout.isEnabled = false
            } else if (it.toString() == "PM") {
                viewModel.getAreaList(uid)
                view.Daily_Visit_Hospital_TextInputLayout.isEnabled = false
                view.Daily_visit_Area_TextInputLayout.isEnabled = true
                view.Daily_Visit_Doctor_TextInputLayout.isEnabled = true
            }
        }
        viewModel.hospitalLiveData.observeForever {
            val hospitalList = arrayListOf<String>()
            for (item in it) {
                hospitalList.add(item.name.toString())
            }
            //set area down list
            val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, hospitalList)
            view.daily_visit_hospital.setAdapter(adapter)
        }
        viewModel.areaLiveData.observeForever {
            val arealList = arrayListOf<String>()
            for (item in it) {
                arealList.add(item.name.toString())
            }
            //set area down list
            val adapter =
                 ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, arealList)
            view.daily_visit_area.setAdapter(adapter)
        }
        view.daily_visit_hospital.doAfterTextChanged {
            viewModel.getSingleHospital(it.toString(), uid)
        }
        viewModel.singleHospitalLiveData.observeForever {
            val hospital = it[0]
            doctorLatitude = hospital.latitude!!.toDouble()
            doctorLongitude = hospital.longitude!!.toDouble()
        }
        view.daily_visit_area.doAfterTextChanged {
            viewModel.getDoctorList(it.toString(), uid)
        }
        viewModel.doctorLiveData.observeForever {
            val doctorList = arrayListOf<String>()
            for (item in it) {
                doctorList.add(item.name.toString())
            }
            //set area down list
            val adapter =
                ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, doctorList)
            view.daily_visit_doctor.setAdapter(adapter)
        }
        view.daily_visit_doctor.doAfterTextChanged {
            viewModel.getSingleDoctor(it.toString(), uid)
        }
        viewModel.singleDoctorLiveData.observeForever {
            val doctor = it[0]
            doctorLatitude = doctor.latitude!!.toDouble()
            doctorLongitude = doctor.longitude!!.toDouble()
        }
        //Get Location
        view.Location_Visit_Button.setOnClickListener {
            try {
                gpsEnabled = locationManger.isProviderEnabled(LocationManager.GPS_PROVIDER)
            } catch (ex: Exception) {
            }
            if (gpsEnabled) {
                if (ActivityCompat.checkSelfPermission(
                        context!!,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context!!,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                }
                fusedLocationClient.getCurrentLocation(
                    LocationRequest.PRIORITY_HIGH_ACCURACY,
                    cts.token
                )
                    .addOnSuccessListener { location: Location? ->
                        if (doctorLatitude != 0.0 && doctorLongitude != 0.0) {
                            val distance =
                                calculateDistance(location!!.longitude,location.latitude,doctorLongitude,doctorLatitude)
                            if (distance <= 200) {
                                view.daily_visit_create_button.isEnabled = true
                                view.Location_Visit_Button.text = "${String.format("%.2f", distance)} meter"
                            } else {
                                view.Location_Visit_Button.text = "Distance more than 200 meter"
                            }
                        }
                    }
            } else {
                Snackbar.make(view, "Enable GPS!", Snackbar.LENGTH_LONG).show()
            }
        }

        view.daily_visit_create_button.setOnClickListener {
            when{
                visitDate.isNullOrEmpty() -> {Snackbar.make(view, "Choose the Date", Snackbar.LENGTH_LONG).show()}
                view.daily_visit_AM.text.isNullOrEmpty() ->{Snackbar.make(view, "Choose the Time", Snackbar.LENGTH_LONG).show()}
                else -> {
                    if (view.daily_visit_AM.text.toString() == "AM"){
                        if (view.daily_visit_hospital.text.isNullOrEmpty()){Snackbar.make(view, "Choose the Hospital", Snackbar.LENGTH_LONG).show()}
                        else {
                            val visit = DailyVisitModel(
                                visitDate,
                                view.daily_visit_AM.text.toString(),
                                view.daily_visit_hospital.text.toString(),
                                null,null,
                                view.daily_visit_comment.text.toString()
                            )
                            viewModel.saveNewVisit(visit,uid)
                            clearView(view)
                        }
                    }else if (view.daily_visit_AM.text.toString() == "PM") {
                        if (view.daily_visit_area.text.isNullOrEmpty() or view.daily_visit_doctor.text.isNullOrEmpty()){
                            Snackbar.make(view, "Choose the Doctor", Snackbar.LENGTH_LONG).show()
                        }else{
                            val visit = DailyVisitModel(
                                visitDate,
                                view.daily_visit_AM.text.toString(),
                                null,
                                view.daily_visit_area.text.toString(),
                                view.daily_visit_doctor.text.toString(),
                                view.daily_visit_comment.text.toString()
                            )
                            viewModel.saveNewVisit(visit,uid)
                            clearView(view)
                        }
                    }else {
                        Snackbar.make(view, "Choose the Time", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }

        return view
    }

    private fun convertLongToTime(view: View, time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd-MM-yyyy")
        val dateString = format.format(date)
        view.WeeklyPlan_Date_Button.text = dateString
        return dateString
    }

    private fun calculateDistance(
       lon1: Double,
       lat1: Double,
       lon2: Double,
       lat2: Double
    ): Double {
        val theta: Double = lon1 - lon2
        var dist = (sin(deg2rad(lat1))
                * sin(deg2rad(lat2))
                + (cos(deg2rad(lat1))
                * cos(deg2rad(lat2))
                * cos(deg2rad(theta))))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515
        return dist * 1609.34
    }
    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }
    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    private fun clearView(view: View){
        //view.Location_Visit_Button.text = R.string.Save_Location.toString()
        view.daily_visit_create_button.isEnabled = false
        Toast.makeText(context,"Visit Add Successfully",Toast.LENGTH_LONG).show()
    }
}