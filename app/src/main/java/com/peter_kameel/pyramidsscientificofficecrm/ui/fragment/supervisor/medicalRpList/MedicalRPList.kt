package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.supervisor.medicalRpList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.peter_kameel.pyramidsscientificofficecrm.databinding.SupListOfHDBinding
import com.peter_kameel.pyramidsscientificofficecrm.helper.adapters.DoctorRecyclerAdapter
import com.peter_kameel.pyramidsscientificofficecrm.helper.adapters.HospitalRecyclerAdapter
import com.peter_kameel.pyramidsscientificofficecrm.pojo.LoginModel
import com.peter_kameel.pyramidsscientificofficecrm.util.Massages
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MedicalRPList(
    private val item: LoginModel,
    private var type: String
) : Fragment() {
    private val viewModel by viewModels<MedicalRPListViewModel>()
    private var _binding: SupListOfHDBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SupListOfHDBinding.inflate(inflater, container, false)
        val view = binding.root
        // RecycleView set LayoutManager
        binding.HDRecyclerView.setHasFixedSize(true)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        binding.HDRecyclerView.layoutManager = manager

        when (type) {
            Massages.typeDoctor -> {
                binding.medicalNameButton.text = "${item.user_name} Doctor`s List"
                viewModel.getListOfDoctors(item.id.toString())
            }
            Massages.typeHospital -> {
                binding.medicalNameButton.text = "${item.user_name} Hospital`s List"
                viewModel.getListOfHospitals(item.id.toString())
            }
        }
        viewModel.doctorLiveData.observeForever {
            binding.HDRecyclerView.adapter = DoctorRecyclerAdapter(it,requireContext(), activity as? AppCompatActivity)
        }

        viewModel.hospitalLiveData.observeForever {
            binding.HDRecyclerView.adapter = HospitalRecyclerAdapter(it,requireContext(), activity as? AppCompatActivity)
        }

        viewModel.massageLiveData.observeForever {

        }
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}