package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.medical.dailyVisit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DailyVisitModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DoctorModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.HospitalModel
import com.peter_kameel.pyramidsscientificofficecrm.util.Massages
import kotlinx.android.synthetic.main.daily_visit.view.*
import kotlinx.android.synthetic.main.daily_visit.view.Daily_Visit_Hospital_TextInputLayout
import kotlinx.android.synthetic.main.daily_visit.view.Location_Visit_Button
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DailyVisitFragment : Fragment() {
    private val viewModel by viewModels<DailyVisitViewModel>()
    private var visitDate: String? = null
    private var doctor = DoctorModel()
    private var hospital = HospitalModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.daily_visit, container, false)
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
                viewModel.getHospitalList()
                view.Daily_Visit_Hospital_TextInputLayout.isEnabled = true
                view.Daily_visit_Area_TextInputLayout.isEnabled = false
                view.Daily_Visit_Doctor_TextInputLayout.isEnabled = false
            } else if (it.toString() == "PM") {
                viewModel.getAreaList()
                view.Daily_Visit_Hospital_TextInputLayout.isEnabled = false
                view.Daily_visit_Area_TextInputLayout.isEnabled = true
                view.Daily_Visit_Doctor_TextInputLayout.isEnabled = true
            }
        }
        //show the list of hospitals
        viewModel.hospitalLiveData.observeForever {
            val hospitalList = arrayListOf<String>()
            for (item in it) {
                hospitalList.add(item.name.toString())
            }
            //set area down list
            val arrayAdapter =
                ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, hospitalList)
            view.daily_visit_hospital.setAdapter(arrayAdapter)
        }
        //show the list of area
        viewModel.areaLiveData.observeForever {
            val arealList = arrayListOf<String>()
            for (item in it) {
                arealList.add(item.name.toString())
            }
            //set area down list
            val arrayAdapter =
                ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, arealList)
            view.daily_visit_area.setAdapter(arrayAdapter)
        }
        //on choose hospital get it`s data
        view.daily_visit_hospital.doAfterTextChanged {
            viewModel.getSingleHospital(it.toString())
        }
        //on choose hospital get it`s data
        viewModel.singleHospitalLiveData.observeForever {
            hospital = it[0]
           //calculate the distance between current location and hospital location
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.checkDistance(context!!,hospital.latitude!!.toDouble(),hospital.longitude!!.toDouble())
            }
        }
        //on choose area will appear call a list of doctors
        view.daily_visit_area.doAfterTextChanged {
            viewModel.getDoctorListByArea(it.toString())
        }
        //show the list of doctors
        viewModel.doctorLiveData.observeForever {
            val doctorList = arrayListOf<String>()
            for (item in it) {
                doctorList.add(item.name.toString())
            }
            //set area down list
            val arrayAdapter =
                ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, doctorList)
            view.daily_visit_doctor.setAdapter(arrayAdapter)
        }
        //on choose doctor get it`s data
        view.daily_visit_doctor.doAfterTextChanged {
            viewModel.getSingleDoctor(it.toString())
        }
        //on choose doctor get it`s data
        viewModel.singleDoctorLiveData.observeForever {
            doctor = it[0]
            //calculate the distance between current location and doctor location
            CoroutineScope(Dispatchers.IO).launch {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.checkDistance(context!!,doctor.latitude!!.toDouble(),doctor.longitude!!.toDouble())
                }
            }
        }
        //create the visit
        view.daily_visit_create_button.setOnClickListener {
            when {
                visitDate.isNullOrEmpty() -> {
                    Toast.makeText(context,Massages.date,Toast.LENGTH_LONG).show()
                }
                view.daily_visit_AM.text.isNullOrEmpty() -> {
                    Toast.makeText(context,Massages.time,Toast.LENGTH_LONG).show()
                }
                else -> {
                    if (view.daily_visit_AM.text.toString() == "AM") {
                        if (view.daily_visit_hospital.text.isNullOrEmpty()) {
                            Toast.makeText(context,Massages.hospital,Toast.LENGTH_LONG).show()
                        } else {
                            val visit = DailyVisitModel(
                                visitDate,
                                view.daily_visit_AM.text.toString(),
                                hospital,
                                null, null,
                                view.daily_visit_comment.text.toString()
                            )
                            viewModel.saveNewVisit(visit)
                            clearView(view)
                        }
                    } else if (view.daily_visit_AM.text.toString() == "PM") {
                        if (view.daily_visit_area.text.isNullOrEmpty() or view.daily_visit_doctor.text.isNullOrEmpty()) {
                            Toast.makeText(context,Massages.doctor,Toast.LENGTH_LONG).show()
                        } else {
                            val visit = DailyVisitModel(
                                visitDate,
                                view.daily_visit_AM.text.toString(),
                                null,
                                view.daily_visit_area.text.toString(),
                                doctor,
                                view.daily_visit_comment.text.toString()
                            )
                            viewModel.saveNewVisit(visit)
                            clearView(view)
                        }
                    } else {
                        Toast.makeText(context,Massages.time,Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        //return distance
        viewModel.distanceLiveData.observeForever {
            if (it <= 200) {
                view.daily_visit_create_button.isEnabled = true
                view.Location_Visit_Button.text = Massages.DistanceOk
                view.daily_visit_create_button.isEnabled = true
            } else {
                view.Location_Visit_Button.text = Massages.DistanceNotOk
                view.daily_visit_create_button.isEnabled = false
            }
        }

        //if gps not enabled show the massage
        viewModel.massageLiveData.observeForever {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
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
        view.daily_visit_create_button.isEnabled = false
    }
}