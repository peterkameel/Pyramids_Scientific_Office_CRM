package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.newHospital

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.peter_kameel.pyramidsscientificofficecrm.helper.adapters.HospitalRecyclerAdapter
import com.peter_kameel.pyramidsscientificofficecrm.pojo.HospitalModel
import com.peter_kameel.pyramidsscientificofficecrm.util.Shared
import com.peter_kameel.pyramidsscientificofficecrm.util.SharedTag
import kotlinx.android.synthetic.main.new_hospital_fragment.view.*
import java.lang.Exception

class NewHospitalFragment : Fragment() {
    private val viewModel by viewModels<NewHospitalViewModel>()

    //initialize for google play location services
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val cts = CancellationTokenSource()
    private var gpsEnabled: Boolean = false
    private lateinit var doctorLocation: Location
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.new_hospital_fragment, container, false)
        // RecycleView set LayoutManager
        view.HospitalRecyclerView.setHasFixedSize(true)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        view.HospitalRecyclerView.layoutManager = manager
        //get location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
        //set location manger
        val locationManger = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        //get user id
        val uid = Shared.readSharedString(context!!, SharedTag.UID, "false").toString()
        //call area list
        viewModel.getHospitalList(uid)
        //on live data set changed
        viewModel.hospitalLiveData.observeForever {
            view.HospitalRecyclerView.adapter = HospitalRecyclerAdapter(it)
        }
        //get the location
        view.Location_Hospital_Button.setOnClickListener {
            try {
                gpsEnabled = locationManger.isProviderEnabled(LocationManager.GPS_PROVIDER)
            } catch (ex: Exception) { }
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
                        view.Location_Hospital_Button.text = "$lat , $lon"
                        doctorLocation = location
                        view.Create_Hospital.isEnabled = true
                    }

            } else {
                Snackbar.make(view, "Enable GPS!", Snackbar.LENGTH_LONG).show()
            }
        }
        //save the doctor
        view.Create_Hospital.setOnClickListener {
            if (view.New_Hospital_Name_TextFiled.text.isNullOrEmpty()) {
                Snackbar.make(view, "Enter Doctor Name!", Snackbar.LENGTH_LONG).show()
            } else {
                val hospital = HospitalModel(
                    view.New_Hospital_Name_TextFiled.text.toString(),
                    doctorLocation.latitude.toString(),
                    doctorLocation.longitude.toString()
                )
                viewModel.saveNewHospital(hospital, uid)
                view.New_Hospital_Name_TextFiled.text!!.clear()
            }
        }
        return view
    }
}