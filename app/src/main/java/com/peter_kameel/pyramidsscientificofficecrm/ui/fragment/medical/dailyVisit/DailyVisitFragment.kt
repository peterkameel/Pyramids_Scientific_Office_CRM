package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.medical.dailyVisit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.databinding.DailyVisitBinding
import com.peter_kameel.pyramidsscientificofficecrm.helper.objects.CheckNetwork
import com.peter_kameel.pyramidsscientificofficecrm.helper.objects.DateConvert
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DailyVisitModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DoctorModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.HospitalModel
import com.peter_kameel.pyramidsscientificofficecrm.util.Massages
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DailyVisitFragment : Fragment() {
    private var _binding: DailyVisitBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel by viewModels<DailyVisitViewModel>()
    private var visitDate: String? = null
    private var doctor = DoctorModel()
    private var hospital = HospitalModel()
    @Inject
    lateinit var checkNetwork: CheckNetwork
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DailyVisitBinding.inflate(inflater, container, false)
        val view = binding.root
        //show date picker
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        //on chose date
        datePicker.addOnPositiveButtonClickListener {
            visitDate = DateConvert.convertDateToText(it)
            binding.WeeklyPlanDateButton.text = visitDate
        }
        //get date of visit
        binding.WeeklyPlanDateButton.setOnClickListener {
            datePicker.show(parentFragmentManager, "tag")
        }
        val am = arrayOf("AM", "PM")
        val adapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, am)
        binding.dailyVisitAM.setAdapter(adapter)
        //on choose AM or PM
        binding.dailyVisitAM.doAfterTextChanged {
            if (it.toString() == "AM") {
                viewModel.getHospitalList()
                binding.DailyVisitHospitalTextInputLayout.isEnabled = true
                binding.DailyVisitAreaTextInputLayout.isEnabled = false
                binding.DailyVisitDoctorTextInputLayout.isEnabled = false
            } else if (it.toString() == "PM") {
                viewModel.getAreaList()
                binding.DailyVisitHospitalTextInputLayout.isEnabled = false
                binding.DailyVisitAreaTextInputLayout.isEnabled = true
                binding.DailyVisitDoctorTextInputLayout.isEnabled = true
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
            binding.dailyVisitHospital.setAdapter(arrayAdapter)
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
            binding.dailyVisitArea.setAdapter(arrayAdapter)
        }
        //on choose hospital get it`s data
        binding.dailyVisitHospital.doAfterTextChanged {
            viewModel.getSingleHospital(it.toString())
        }
        //on choose hospital get it`s data
        viewModel.singleHospitalLiveData.observeForever {
            hospital = it
            //calculate the distance between current location and hospital location
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.checkDistance(
                    requireContext(),
                    hospital.latitude!!.toDouble(),
                    hospital.longitude!!.toDouble()
                )
            }
        }
        //on choose area will appear call a list of doctors
        binding.dailyVisitArea.doAfterTextChanged {
            viewModel.getDoctorListByArea(it.toString())
        }
        //show the list of doctors
        viewModel.doctorLiveData.observeForever {
            val doctorList = arrayListOf<String>()
            for (item in it) {
                doctorList.add(item.name.toString())
            }
            //set doctor down list
            val arrayAdapter =
                ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, doctorList)
            binding.dailyVisitDoctor.setAdapter(arrayAdapter)
        }
        //on choose doctor get it`s data
        binding.dailyVisitDoctor.doAfterTextChanged {
            viewModel.getSingleDoctor(it.toString())
        }
        //on choose doctor get it`s data
        viewModel.singleDoctorLiveData.observeForever {
            doctor = it
            //calculate the distance between current location and doctor location
            CoroutineScope(Dispatchers.IO).launch {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.checkDistance(
                        requireContext(),
                        doctor.latitude!!.toDouble(),
                        doctor.longitude!!.toDouble()
                    )
                }
            }
        }
        //create the visit
        binding.dailyVisitCreateButton.setOnClickListener {
            when {
                visitDate.isNullOrEmpty() -> {
                    Toast.makeText(context, Massages.date, Toast.LENGTH_LONG).show()
                }
                binding.dailyVisitAM.text.isNullOrEmpty() -> {
                    Toast.makeText(context, Massages.time, Toast.LENGTH_LONG).show()
                }
                else -> {
                    if (binding.dailyVisitAM.text.toString() == "AM") {
                        if (binding.dailyVisitHospital.text.isNullOrEmpty()) {
                            Toast.makeText(context, Massages.hospital, Toast.LENGTH_LONG).show()
                        } else {
                            val visit = DailyVisitModel(
                                visitDate,
                                binding.dailyVisitAM.text.toString(),
                                hospital,
                                null, null,
                                binding.dailyVisitComment.text.toString()
                            )
                            viewModel.saveNewVisit(visit)
                            clearView()
                        }
                    } else if (binding.dailyVisitAM.text.toString() == "PM") {
                        if (binding.dailyVisitArea.text.isNullOrEmpty() or binding.dailyVisitDoctor.text.isNullOrEmpty()) {
                            Toast.makeText(context, Massages.doctor, Toast.LENGTH_LONG).show()
                        } else {
                            val visit = DailyVisitModel(
                                visitDate,
                                binding.dailyVisitAM.text.toString(),
                                null,
                                binding.dailyVisitArea.text.toString(),
                                doctor,
                                binding.dailyVisitComment.text.toString()
                            )
                            viewModel.saveNewVisit(visit)
                            clearView()
                        }
                    } else {
                        Toast.makeText(context,Massages.time,Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        //return distance
        viewModel.distanceLiveData.observeForever {
            if (checkNetwork.isNetworkAvailable()){
                if (it <= 200) {
                    binding.dailyVisitCreateButton.isEnabled = true
                    binding.LocationVisitButton.text = Massages.DistanceOk
                    binding.dailyVisitCreateButton.isEnabled = true
                } else {
                    binding.LocationVisitButton.text = Massages.DistanceNotOk
                    binding.dailyVisitCreateButton.isEnabled = false
                }
            }else {
                if (it <= 500) {
                    binding.dailyVisitCreateButton.isEnabled = true
                    binding.LocationVisitButton.text = Massages.DistanceOk
                    binding.dailyVisitCreateButton.isEnabled = true
                } else {
                    binding.LocationVisitButton.text = Massages.DistanceNotOk
                    binding.dailyVisitCreateButton.isEnabled = false
                }
            }
        }

        //if gps not enabled show the massage
        viewModel.massageLiveData.observeForever {
            if (this.requireView().isVisible) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }

        return view
    }

    private fun clearView() {
        binding.dailyVisitCreateButton.isEnabled = false
        if (!binding.dailyVisitComment.text.isNullOrEmpty()) {
            binding.dailyVisitComment.text!!.clear()
        }
        if (!binding.dailyVisitHospital.text.isNullOrEmpty()) {
            binding.dailyVisitHospital.text!!.clear()
        } else if (!binding.dailyVisitDoctor.text.isNullOrEmpty()) {
            binding.dailyVisitDoctor.text!!.clear()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}