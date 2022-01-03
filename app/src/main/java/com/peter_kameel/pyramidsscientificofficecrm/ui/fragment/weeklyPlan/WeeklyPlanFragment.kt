package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.weeklyPlan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.helper.adapters.WeeklyPlanAdapterList
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DoctorModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.HospitalModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.WeeklyPlanModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.WeeklyPlanRecyclerModel
import com.peter_kameel.pyramidsscientificofficecrm.util.Shared
import com.peter_kameel.pyramidsscientificofficecrm.util.SharedTag
import kotlinx.android.synthetic.main.daily_visit.view.WeeklyPlan_Date_Button
import kotlinx.android.synthetic.main.weekly_plan.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WeeklyPlanFragment : Fragment() {
    private val viewModel by viewModels<WeeklyPlanViewModel>()
    private var userID: String? = null
    private var visitDate: String? = null
    private var hospitalList: ArrayList<HospitalModel> = ArrayList()
    private var doctorList: ArrayList<DoctorModel> = ArrayList()
    private var adapterList: ArrayList<WeeklyPlanRecyclerModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.weekly_plan, container, false)
        val uid = Shared.readSharedString(context!!, SharedTag.UID, "false").toString()
        userID = uid
        // RecycleView set LayoutManager
        view.WeeklyPlanRecyclerView.setHasFixedSize(true)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        view.WeeklyPlanRecyclerView.layoutManager = manager
        //get lists
        viewModel.getAreaList(uid)
        viewModel.getHospitalList(uid)
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

        //get the list of hospitals
        viewModel.hospitalLiveData.observeForever {
            val hospitalList = arrayListOf<String>()
            for (item in it) {
                hospitalList.add(item.name.toString())
            }
            //set area down list
            val adapter =
                ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, hospitalList)
            view.Weekly_Plan_Hospital_text.setAdapter(adapter)
        }

        //get the list of area
        viewModel.areaLiveData.observeForever {
            val arealList = arrayListOf<String>()
            for (item in it) {
                arealList.add(item.name.toString())
            }
            //set area down list
            val adapter =
                ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, arealList)
            view.Weekly_Plan_Area_text.setAdapter(adapter)
        }

        //on text changed get the doctor list
        view.Weekly_Plan_Area_text.doAfterTextChanged {
            viewModel.getDoctorList(it.toString(), uid)
        }
        //set the list of doctor
        viewModel.doctorLiveData.observeForever {
            val doctorList = arrayListOf<String>()
            for (item in it) {
                doctorList.add(item.name.toString())
            }
            //set area down list
            val adapter =
                ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, doctorList)
            view.Weekly_Plan_doctor.setAdapter(adapter)
        }

        //save hospital
        view.Weekly_Plan_Save_Hospital.setOnClickListener {
            if (view.Weekly_Plan_Hospital_text.text.isNullOrEmpty()) {
                Toast.makeText(context, SharedTag.hospital, Toast.LENGTH_LONG).show()
            } else {
                viewModel.getSingleHospital(view.Weekly_Plan_Hospital_text.text.toString(), uid)
            }
        }
        viewModel.singleHospitalLiveData.observeForever {
            it?.let {
                hospitalList.add(it[0])
                adapterList.add(WeeklyPlanRecyclerModel(it[0].name.toString()))
                view.WeeklyPlanRecyclerView.adapter = WeeklyPlanAdapterList(adapterList)
                Log.e("Hospital", it.toString())
            }
        }

        //save doctor
        view.Weekly_Plan_Save_Doctor.setOnClickListener {
            if (view.Weekly_Plan_doctor.text.isNullOrEmpty()) {
                Toast.makeText(context, SharedTag.doctor, Toast.LENGTH_LONG).show()
            } else {
                viewModel.getSingleDoctor(view.Weekly_Plan_doctor.text.toString(), uid)
            }
        }
        viewModel.singleDoctorLiveData.observeForever {
            it?.let {
                doctorList.add(it[0])
                adapterList.add(WeeklyPlanRecyclerModel(it[0].name.toString()))
                view.WeeklyPlanRecyclerView.adapter = WeeklyPlanAdapterList(adapterList)
                Log.e("Doctor", it.toString())
            }
        }

        //save the plan for the date and clear data
        view.Weekly_Plan_Save_Plan_Date.setOnClickListener {
            when {
                visitDate.isNullOrEmpty() -> {
                    Toast.makeText(context, SharedTag.date, Toast.LENGTH_LONG).show()
                }
                hospitalList.isNullOrEmpty() -> {
                    Toast.makeText(context, SharedTag.hospital, Toast.LENGTH_LONG).show()
                }
                doctorList.isNullOrEmpty() -> {
                    Toast.makeText(context, SharedTag.doctor, Toast.LENGTH_LONG).show()
                }
                else -> {
                    val plan = WeeklyPlanModel(hospitalList, doctorList)
                    viewModel.createPlan(visitDate.toString(), plan, uid)
                    Toast.makeText(context, SharedTag.addPlan, Toast.LENGTH_LONG).show()
                    clearView(view)
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

    private fun clearView(view: View) {
        view.WeeklyPlan_Date_Button.text = SharedTag.getDate
        visitDate = null
        hospitalList.clear()
        doctorList.clear()
        adapterList.clear()
        view.Weekly_Plan_Hospital_text.text?.clear()
        view.Weekly_Plan_Area_text.text?.clear()
        view.Weekly_Plan_doctor.text?.clear()
        view.WeeklyPlanRecyclerView.adapter = WeeklyPlanAdapterList(adapterList)
    }
}