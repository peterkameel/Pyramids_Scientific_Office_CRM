package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.medical.weeklyPlan

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.databinding.WeeklyPlanBinding
import com.peter_kameel.pyramidsscientificofficecrm.helper.adapters.WeeklyPlanAdapterList
import com.peter_kameel.pyramidsscientificofficecrm.helper.objects.DateConvert
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DoctorModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.HospitalModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.WeeklyPlanModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.WeeklyPlanRecyclerModel
import com.peter_kameel.pyramidsscientificofficecrm.util.Massages
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.ArrayList

@AndroidEntryPoint
class WeeklyPlanFragment : Fragment() {
    private val viewModel by viewModels<WeeklyPlanViewModel>()
    private var _binding: WeeklyPlanBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var visitDate: String? = null
    private var hospitalList: ArrayList<HospitalModel> = ArrayList()
    private var doctorList: ArrayList<DoctorModel> = ArrayList()
    private var adapterList: ArrayList<WeeklyPlanRecyclerModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = WeeklyPlanBinding.inflate(inflater, container, false)
        val view = binding.root
        // RecycleView set LayoutManager
        binding.WeeklyPlanRecyclerView.setHasFixedSize(true)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        binding.WeeklyPlanRecyclerView.layoutManager = manager
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
            visitDate = DateConvert.convertDateToText(it)
            binding.WeeklyPlanDateButton.text = visitDate
        }
        //get date of visit
        binding.WeeklyPlanDateButton.setOnClickListener {
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
            binding.WeeklyPlanHospitalText.setAdapter(arrayAdapter)
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
            binding.WeeklyPlanAreaText.setAdapter(arrayAdapter)
        }

        //on text changed get the doctor list
        binding.WeeklyPlanAreaText.doAfterTextChanged {
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
            binding.WeeklyPlanDoctor.setAdapter(arrayAdapter)
        }

        //save hospital
        binding.WeeklyPlanSaveHospital.setOnClickListener {
            if (binding.WeeklyPlanHospitalText.text.isNullOrEmpty()) {
                Toast.makeText(context,Massages.hospital, Toast.LENGTH_LONG).show()
            } else {
                viewModel.getSingleHospital(binding.WeeklyPlanHospitalText.text.toString())
            }
        }
        viewModel.singleHospitalLiveData.observeForever {
            it?.let {
                hospitalList.add(it[0])
                adapterList.add(WeeklyPlanRecyclerModel(it[0].name.toString()))
                binding.WeeklyPlanRecyclerView.adapter = WeeklyPlanAdapterList(adapterList)
                binding.WeeklyPlanHospitalText.text.clear()
            }
        }

        //save doctor
        binding.WeeklyPlanSaveDoctor.setOnClickListener {
            if (binding.WeeklyPlanDoctor.text.isNullOrEmpty()) {
                Toast.makeText(context, Massages.doctor, Toast.LENGTH_LONG).show()
            } else {
                viewModel.getSingleDoctor(binding.WeeklyPlanDoctor.text.toString())
            }
        }
        viewModel.singleDoctorLiveData.observeForever {
            it?.let {
                doctorList.add(it[0])
                adapterList.add(WeeklyPlanRecyclerModel(it[0].name.toString()))
                binding.WeeklyPlanRecyclerView.adapter = WeeklyPlanAdapterList(adapterList)
                binding.WeeklyPlanDoctor.text.clear()
            }
        }

        //save the plan for the date and clear data
        binding.WeeklyPlanSavePlanDate.setOnClickListener {
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
                    val plan = WeeklyPlanModel(visitDate.toString(),hospitalList, doctorList)
                    viewModel.createPlan(visitDate.toString(), plan)
                    Toast.makeText(context, Massages.addPlan, Toast.LENGTH_LONG).show()
                    clearView()
                }
            }
        }
        viewModel.massageLiveData.observeForever {
            if (this.requireView().isVisible){
                Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()
            }        }
        return view
    }
/**
    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val dateString = format.format(date)
        binding.WeeklyPlanDateButton.text = dateString
        return dateString
    }
    **/

    private fun clearView() {
        binding.WeeklyPlanDateButton.text = Massages.getDate
        visitDate = null
        hospitalList.clear()
        doctorList.clear()
        adapterList.clear()
        binding.WeeklyPlanHospitalText.text?.clear()
        binding.WeeklyPlanAreaText.text?.clear()
        binding.WeeklyPlanDoctor.text?.clear()
        binding.WeeklyPlanRecyclerView.adapter = WeeklyPlanAdapterList(adapterList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}