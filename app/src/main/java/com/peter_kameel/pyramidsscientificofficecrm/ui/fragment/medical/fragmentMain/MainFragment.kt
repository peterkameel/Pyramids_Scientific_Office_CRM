package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.medical.fragmentMain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.peter_kameel.pyramidsscientificofficecrm.databinding.FragmentMainBinding
import com.peter_kameel.pyramidsscientificofficecrm.helper.adapters.DailyVisitRecyclerAdapter
import com.peter_kameel.pyramidsscientificofficecrm.helper.adapters.DoctorRecyclerAdapter
import com.peter_kameel.pyramidsscientificofficecrm.helper.adapters.HospitalRecyclerAdapter
import com.peter_kameel.pyramidsscientificofficecrm.helper.objects.DateConvert
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DailyVisitModel
import com.peter_kameel.pyramidsscientificofficecrm.util.Massages
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MainFragment(
    private var type: String,
    private var mrID: String?,
) : Fragment() {
    private var _binding: FragmentMainBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val viewModel by viewModels<MainFragmentViewModel>()
    private var amList = ArrayList<DailyVisitModel>()
    private var pMList = ArrayList<DailyVisitModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        // AM / PM RecycleView set LayoutManager
        binding.AMRecyclerView.setHasFixedSize(true)
        binding.PMRecyclerView.setHasFixedSize(true)
        val amManager = LinearLayoutManager(context)
        val pmManager = LinearLayoutManager(context)
        amManager.orientation = LinearLayoutManager.VERTICAL
        pmManager.orientation = LinearLayoutManager.VERTICAL
        binding.AMRecyclerView.layoutManager = amManager
        binding.PMRecyclerView.layoutManager = pmManager
        //show date picker
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        //on chose date
        datePicker.addOnPositiveButtonClickListener {
            val date = DateConvert.convertDateToText(it)
            binding.dateText.text = date
            amList.clear()
            pMList.clear()
            binding.AMRecyclerView.adapter!!.notifyDataSetChanged()
            binding.PMRecyclerView.adapter!!.notifyDataSetChanged()
            if (type == Massages.typeSupVisit) {
                viewModel.getVisitListBYDateAndID(DateConvert.convertDateToText(it),mrID.toString())
                binding.dateText.text = Massages.visitsDate + date
            } else if (type == Massages.typePlan) {
                viewModel.getWeeklyPlanByDateAndID(date,mrID.toString())
                binding.dateText.text = Massages.planDate + date
            }

        }
        val calendar = Calendar.getInstance().time
        val date = DateConvert.convertDateToText(calendar.time)

        binding.dateText.text = date

        when (type) {
            Massages.typeMrVisit -> {
                viewModel.getVisitListBYDate(date)
                binding.dateText.isEnabled = false
            }
            Massages.typeSupVisit -> {
                viewModel.getVisitListBYDateAndID(date, mrID.toString())
                binding.dateText.isEnabled = true
                binding.dateText.text = Massages.visitsDate + date
            }
            Massages.typePlan -> {
                viewModel.getWeeklyPlanByDateAndID(date,mrID.toString())
                binding.dateText.isEnabled = true
                binding.dateText.text = Massages.planDate + date
            }
        }

        binding.dateText.setOnClickListener {
            datePicker.show(parentFragmentManager, "tag")
        }

        viewModel.visitLiveData.observeForever {
            for (item in it) {
                if (item.time.toString() == "AM") {
                    amList.add(item)
                } else if (item.time.toString() == "PM") {
                    pMList.add(item)
                }
            }
            binding.AMRecyclerView.adapter = DailyVisitRecyclerAdapter(requireContext(),amList, activity as? AppCompatActivity)
            binding.PMRecyclerView.adapter = DailyVisitRecyclerAdapter(requireContext(),pMList, activity as? AppCompatActivity)
        }

        viewModel.weeklyPlanHospitalLiveData.observeForever {
            binding.AMRecyclerView.adapter = HospitalRecyclerAdapter(it,requireContext(), activity as? AppCompatActivity)
        }
        viewModel.weeklyPlanDoctorLiveData.observeForever {
            binding.PMRecyclerView.adapter = DoctorRecyclerAdapter(it,requireContext(), activity as? AppCompatActivity)
        }

        viewModel.massageLiveData.observeForever {
            if (this.requireView().isVisible){
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}