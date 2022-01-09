package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.medical.newDoctor

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
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.snackbar.Snackbar
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.helper.adapters.DoctorRecyclerAdapter
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DoctorModel
import com.peter_kameel.pyramidsscientificofficecrm.util.Shared
import com.peter_kameel.pyramidsscientificofficecrm.util.SharedTag
import kotlinx.android.synthetic.main.new_doctor_fragment.view.*
import java.lang.Exception

class NewDoctorFragment : Fragment() {
    private val viewModel by viewModels<NewDoctorViewModel>() // initialize viewModel
    private var userID : String? = null
    //initialize for google play location services
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val cts = CancellationTokenSource()
    private var gpsEnabled: Boolean = false
    lateinit var doctorLocation: Location
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.new_doctor_fragment, container, false)
        val uid = Shared.readSharedString(context!!, SharedTag.UID, "false").toString()
        userID = uid
        //get location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
        //set location manger
        val locationManger = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // RecycleView set LayoutManager
        view.DoctorRecyclerView.setHasFixedSize(true)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        view.DoctorRecyclerView.layoutManager = manager
        //get and store list of area
        viewModel.areaLiveData.observeForever {
            val areaList = arrayListOf<String>()
            for (item in it){
                areaList.add(item.name.toString())
            }
            //set area down list
            val adapter =
                ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, areaList)
            view.New_Doctor_Area_TextFiled.setAdapter(adapter)
        }
        //call Doctor list
        viewModel.getDoctorList(uid)
        //on live data set changed
        viewModel.doctorLiveData.observeForever {
            view.DoctorRecyclerView.adapter = DoctorRecyclerAdapter(it)
        }
        //get the location
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
                        val lat = location!!.latitude
                        val lon = location.longitude
                        view.Location_Visit_Button.text = "$lat , $lon"
                        doctorLocation = location
                        view.Create_New_Doctor_Button.isEnabled = true
                    }

            } else {
                Snackbar.make(view, "Enable GPS!", Snackbar.LENGTH_LONG).show()
            }
        }
        //save the doctor
        view.Create_New_Doctor_Button.setOnClickListener {
            when{
                view.New_Doctor_Name_TextFiled.text.isNullOrEmpty() ->{
                    Snackbar.make(view, "Enter Doctor Name!", Snackbar.LENGTH_LONG).show()
                }
                view.New_Doctor_Specialization_TextFiled.text.isNullOrEmpty() ->{
                    Snackbar.make(view, "Enter Doctor Specialization!", Snackbar.LENGTH_LONG).show()
                }
                view.New_Doctor_Area_TextFiled.text.isNullOrEmpty() ->{
                    Snackbar.make(view, "Enter Doctor Area!", Snackbar.LENGTH_LONG).show()
                }
                else -> {
                    val doctor = DoctorModel(
                        view.New_Doctor_Name_TextFiled.text.toString(),
                        view.New_Doctor_Specialization_TextFiled.text.toString(),
                        view.New_Doctor_Area_TextFiled.text.toString(),
                        doctorLocation.latitude.toString(),
                        doctorLocation.longitude.toString()
                    )
                    viewModel.saveNewDoctor(doctor,uid)
                    view.New_Doctor_Name_TextFiled.text!!.clear()
                    view.New_Doctor_Specialization_TextFiled.text!!.clear()
                    view.New_Doctor_Area_TextFiled.text!!.clear()
                }
            }
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        userID?.let { viewModel.getAreaList(it) }
    }
}