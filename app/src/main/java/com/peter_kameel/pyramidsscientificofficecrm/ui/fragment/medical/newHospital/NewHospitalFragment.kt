package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.medical.newHospital

import android.location.Location
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
import com.google.android.material.snackbar.Snackbar
import com.peter_kameel.pyramidsscientificofficecrm.databinding.NewHospitalFragmentBinding
import com.peter_kameel.pyramidsscientificofficecrm.helper.adapters.HospitalRecyclerAdapter
import com.peter_kameel.pyramidsscientificofficecrm.helper.objects.CheckNetwork
import com.peter_kameel.pyramidsscientificofficecrm.pojo.HospitalModel
import com.peter_kameel.pyramidsscientificofficecrm.util.Massages
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewHospitalFragment : Fragment() {
    private val viewModel by viewModels<NewHospitalViewModel>()
    private var _binding: NewHospitalFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var doctorLocation: Location
    @Inject
    lateinit var checkNetwork: CheckNetwork
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NewHospitalFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        // RecycleView set LayoutManager
        binding.HospitalRecyclerView.setHasFixedSize(true)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        binding.HospitalRecyclerView.layoutManager = manager

        //call area list
        viewModel.getHospitalList()
        //on live data set changed
        viewModel.hospitalLiveData.observeForever {
            binding.HospitalRecyclerView.adapter = HospitalRecyclerAdapter(it,requireContext(),activity as? AppCompatActivity)
        }
        //get the location
        binding.LocationHospitalButton.setOnClickListener {
            if (checkNetwork.isNetworkAvailable()){
                viewModel.getLocation(requireContext())
            }else {
                Toast.makeText(requireContext(), Massages.connection,Toast.LENGTH_LONG).show()
            }
        }
        //observe on location when coming
        viewModel.locationLiveData.observeForever {
            val lat = it.latitude
            val lon = it.longitude
            binding.LocationHospitalButton.text = "$lat , $lon"
            doctorLocation = it
            binding.CreateHospital.isEnabled = true
        }
        //save the doctor
        binding.CreateHospital.setOnClickListener {
            if (binding.NewHospitalNameTextFiled.text.isNullOrEmpty()) {
                Snackbar.make(binding.root, "Enter Doctor Name!", Snackbar.LENGTH_LONG).show()
            } else {
                val hospital = HospitalModel(
                    binding.NewHospitalNameTextFiled.text.toString(),
                    doctorLocation.latitude.toString(),
                    doctorLocation.longitude.toString()
                )
                viewModel.saveNewHospital(hospital)
                binding.NewHospitalNameTextFiled.text!!.clear()
            }
        }

        viewModel.massageLiveData.observeForever {
            if (this.requireView().isVisible){
                Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()
            }
        }
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}