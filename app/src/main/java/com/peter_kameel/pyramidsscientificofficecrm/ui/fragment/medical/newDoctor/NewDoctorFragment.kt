package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.medical.newDoctor

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.helper.adapters.DoctorRecyclerAdapter
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DoctorModel
import com.peter_kameel.pyramidsscientificofficecrm.util.Massages
import com.peter_kameel.pyramidsscientificofficecrm.util.Shared
import com.peter_kameel.pyramidsscientificofficecrm.util.SharedTag
import kotlinx.android.synthetic.main.new_doctor_fragment.view.*

class NewDoctorFragment : Fragment() {
    private val viewModel by viewModels<NewDoctorViewModel>() // initialize viewModel
    private val uid by lazy { Shared.readSharedString(context!!, SharedTag.UID, "null").toString() }
    private lateinit var doctorLocation: Location
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.new_doctor_fragment, container, false)
        // RecycleView set LayoutManager
        view.DoctorRecyclerView.setHasFixedSize(true)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        view.DoctorRecyclerView.layoutManager = manager
        //get and store list of area
        viewModel.areaLiveData.observeForever {
            val areaList = arrayListOf<String>()
            for (item in it) {
                areaList.add(item.name.toString())
            }
            //set area down list
            val adapter =
                ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, areaList)
            view.New_Doctor_Area_TextFiled.setAdapter(adapter)
        }
        //call Doctor list
        viewModel.getDoctorList()
        //on live data set changed
        viewModel.doctorLiveData.observeForever {
            view.DoctorRecyclerView.adapter = DoctorRecyclerAdapter(it,context!!)
        }
        //get the location
        view.Location_Visit_Button.setOnClickListener {
            viewModel.getLocation(context!!)
        }
        viewModel.locationLiveData.observeForever {
            val lat = it.latitude
            val lon = it.longitude
            view.Location_Visit_Button.text = "$lat , $lon"
            doctorLocation = it
            view.Create_New_Doctor_Button.isEnabled = true
        }
        //save the doctor
        view.Create_New_Doctor_Button.setOnClickListener {
            when {
                view.New_Doctor_Name_TextFiled.text.isNullOrEmpty() -> {
                    Snackbar.make(view, Massages.enterDoctorName, Snackbar.LENGTH_LONG).show()
                }
                view.New_Doctor_Specialization_TextFiled.text.isNullOrEmpty() -> {
                    Snackbar.make(view, Massages.enterDoctorSp, Snackbar.LENGTH_LONG).show()
                }
                view.New_Doctor_Area_TextFiled.text.isNullOrEmpty() -> {
                    Snackbar.make(view, Massages.enterDoctorArea, Snackbar.LENGTH_LONG).show()
                }
                else -> {
                    val doctor = DoctorModel(
                        view.New_Doctor_Name_TextFiled.text.toString(),
                        view.New_Doctor_Specialization_TextFiled.text.toString(),
                        view.New_Doctor_Area_TextFiled.text.toString(),
                        doctorLocation.latitude.toString(),
                        doctorLocation.longitude.toString()
                    )
                    viewModel.saveNewDoctor(doctor)
                    view.New_Doctor_Name_TextFiled.text!!.clear()
                    view.New_Doctor_Specialization_TextFiled.text!!.clear()
                    view.New_Doctor_Area_TextFiled.text!!.clear()
                }
            }
        }

        //if gps not enabled show the massage
        viewModel.massageLiveData.observeForever {
            Toast.makeText(context!!, it, Toast.LENGTH_LONG).show()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        uid.let { viewModel.getAreaList() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
}