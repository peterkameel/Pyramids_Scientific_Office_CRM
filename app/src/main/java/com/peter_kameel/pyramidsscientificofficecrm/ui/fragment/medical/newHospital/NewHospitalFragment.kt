package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.medical.newHospital

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.helper.adapters.HospitalRecyclerAdapter
import com.peter_kameel.pyramidsscientificofficecrm.pojo.HospitalModel
import kotlinx.android.synthetic.main.new_hospital_fragment.view.*

class NewHospitalFragment : Fragment() {
    private val viewModel by viewModels<NewHospitalViewModel>()
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

        //call area list
        viewModel.getHospitalList()
        //on live data set changed
        viewModel.hospitalLiveData.observeForever {
            view.HospitalRecyclerView.adapter = HospitalRecyclerAdapter(it,context!!)
        }
        //get the location
        view.Location_Hospital_Button.setOnClickListener {
            viewModel.getLocation(context!!)
        }
        //observe on location when coming
        viewModel.locationLiveData.observeForever {
            val lat = it.latitude
            val lon = it.longitude
            view.Location_Hospital_Button.text = "$lat , $lon"
            doctorLocation = it
            view.Create_Hospital.isEnabled = true
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
                viewModel.saveNewHospital(hospital)
                view.New_Hospital_Name_TextFiled.text!!.clear()
            }
        }

        viewModel.massageLiveData.observeForever {
            Toast.makeText(context,it,Toast.LENGTH_LONG).show()
        }
        return view
    }
}