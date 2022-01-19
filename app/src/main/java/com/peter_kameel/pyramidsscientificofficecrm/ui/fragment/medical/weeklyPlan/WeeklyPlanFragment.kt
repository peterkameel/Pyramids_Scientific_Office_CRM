package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.medical.weeklyPlan

import android.os.Bundle
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
import com.peter_kameel.pyramidsscientificofficecrm.util.Massages
import kotlinx.android.synthetic.main.daily_visit.view.WeeklyPlan_Date_Button
import kotlinx.android.synthetic.main.weekly_plan.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WeeklyPlanFragment : Fragment() {
    private val viewModel by viewModels<WeeklyPlanViewModel>()
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
        // RecycleView set LayoutManager
        view.WeeklyPlanRecyclerView.setHasFixedSize(true)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        view.WeeklyPlanRecyclerView.layoutManager = manager
        //get lists
        viewModel.getAreaList()
        viewModel.getHospitalList()
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
            val arrayAdapter =
                ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, hospitalList)
            view.Weekly_Plan_Hospital_text.setAdapter(arrayAdapter)
        }

        //get the list of area
        viewModel.areaLiveData.observeForever {
            val arealList = arrayListOf<String>()
            for (item in it) {
                arealList.add(item.name.toString())
            }
            //set area down list
            val arrayAdapter =
                ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, arealList)
            view.Weekly_Plan_Area_text.setAdapter(arrayAdapter)
        }

        //on text changed get the doctor list
        view.Weekly_Plan_Area_text.doAfterTextChanged {
            viewModel.getDoctorListByArea(it.toString())
        }
        //set the list of doctor
        viewModel.doctorLiveData.observeForever {
            val doctorList = arrayListOf<String>()
            for (item in it) {
                doctorList.add(item.name.toString())
            }
            //set area down list
            val arrayAdapter =
                ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, doctorList)
            view.Weekly_Plan_doctor.setAdapter(arrayAdapter)
        }

        //save hospital
        view.Weekly_Plan_Save_Hospital.setOnClickListener {
            if (view.Weekly_Plan_Hospital_text.text.isNullOrEmpty()) {
                Toast.makeText(context,Massages.hospital, Toast.LENGTH_LONG).show()
            } else {
                viewModel.getSingleHospital(view.Weekly_Plan_Hospital_text.text.toString())
            }
        }
        viewModel.singleHospitalLiveData.observeForever {
            it?.let {
                hospitalList.add(it[0])
                adapterList.add(WeeklyPlanRecyclerModel(it[0].name.toString()))
                view.WeeklyPlanRecyclerView.adapter = WeeklyPlanAdapterList(adapterList)
                view.Weekly_Plan_Hospital_text.text.clear()
            }
        }

        //save doctor
        view.Weekly_Plan_Save_Doctor.setOnClickListener {
            if (view.Weekly_Plan_doctor.text.isNullOrEmpty()) {
                Toast.makeText(context, Massages.doctor, Toast.LENGTH_LONG).show()
            } else {
                viewModel.getSingleDoctor(view.Weekly_Plan_doctor.text.toString())
            }
        }
        viewModel.singleDoctorLiveData.observeForever {
            it?.let {
                doctorList.add(it[0])
                adapterList.add(WeeklyPlanRecyclerModel(it[0].name.toString()))
                view.WeeklyPlanRecyclerView.adapter = WeeklyPlanAdapterList(adapterList)
                view.Weekly_Plan_doctor.text.clear()
            }
        }

        //save the plan for the date and clear data
        view.Weekly_Plan_Save_Plan_Date.setOnClickListener {
            when {
                visitDate.isNullOrEmpty() -> {
                    Toast.makeText(context, Massages.date, Toast.LENGTH_LONG).show()
                }
                hospitalList.isNullOrEmpty() -> {
                    Toast.makeText(context, Massages.hospital, Toast.LENGTH_LONG).show()
                }
                doctorList.isNullOrEmpty() -> {
                    Toast.makeText(context, Massages.doctor, Toast.LENGTH_LONG).show()
                }
                else -> {
                    val plan = WeeklyPlanModel(hospitalList, doctorList)
                    viewModel.createPlan(visitDate.toString(), plan)
                    Toast.makeText(context, Massages.addPlan, Toast.LENGTH_LONG).show()
                    clearView(view)
                }
            }
        }
        viewModel.massageLiveData.observeForever {
            Toast.makeText(context,it,Toast.LENGTH_LONG).show()
        }
        return view
    }

    private fun convertLongToTime(view: View, time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val dateString = format.format(date)
        view.WeeklyPlan_Date_Button.text = dateString
        return dateString
    }

    private fun clearView(view: View) {
        view.WeeklyPlan_Date_Button.text = Massages.getDate
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