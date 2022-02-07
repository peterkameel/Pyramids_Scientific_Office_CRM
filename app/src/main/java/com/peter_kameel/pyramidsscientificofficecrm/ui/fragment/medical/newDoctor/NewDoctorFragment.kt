package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.medical.newDoctor

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.databinding.NewDoctorFragmentBinding
import com.peter_kameel.pyramidsscientificofficecrm.helper.adapters.DoctorRecyclerAdapter
import com.peter_kameel.pyramidsscientificofficecrm.helper.objects.CheckNetwork
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DoctorModel
import com.peter_kameel.pyramidsscientificofficecrm.util.Massages
import com.peter_kameel.pyramidsscientificofficecrm.util.Shared
import com.peter_kameel.pyramidsscientificofficecrm.util.SharedTag
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewDoctorFragment : Fragment() {
    private var _binding: NewDoctorFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel by viewModels<NewDoctorViewModel>() // initialize viewModel
    private lateinit var doctorLocation: Location
    @Inject
    lateinit var shared: Shared
    @Inject
    lateinit var checkNetwork: CheckNetwork
    private val uid by lazy { shared.readSharedString(SharedTag.UID, "null").toString() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NewDoctorFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        // RecycleView set LayoutManager
        binding.DoctorRecyclerView.setHasFixedSize(true)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        binding.DoctorRecyclerView.layoutManager = manager
        //get and store list of area
        viewModel.areaLiveData.observeForever {
            val areaList = arrayListOf<String>()
            for (item in it) {
                areaList.add(item.name.toString())
            }
            //set area down list
            val adapter =
                ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, areaList)
            binding.NewDoctorAreaTextFiled.setAdapter(adapter)
        }
        //call Doctor list
        viewModel.getDoctorList()
        //on live data set changed
        viewModel.doctorLiveData.observeForever {
            binding.DoctorRecyclerView.adapter = DoctorRecyclerAdapter(it,requireContext(),
                activity as? AppCompatActivity
            )
        }
        //get the location
        binding.LocationVisitButton.setOnClickListener {
            if (checkNetwork.isNetworkAvailable()){
                viewModel.getLocation(requireContext())
            }else {
               Toast.makeText(requireContext(),Massages.connection,Toast.LENGTH_LONG).show()
            }
        }
        viewModel.locationLiveData.observeForever {
            val lat = it.latitude
            val lon = it.longitude
            binding.LocationVisitButton.text = "$lat , $lon"
            doctorLocation = it
            binding.CreateNewDoctorButton.isEnabled = true
        }
        //save the doctor
        binding.CreateNewDoctorButton.setOnClickListener {
            when {
                binding.NewDoctorNameTextFiled.text.isNullOrEmpty() -> {
                    Snackbar.make(view, Massages.enterDoctorName, Snackbar.LENGTH_LONG).show()
                }
                binding.NewDoctorSpecializationTextFiled.text.isNullOrEmpty() -> {
                    Snackbar.make(view, Massages.enterDoctorSp, Snackbar.LENGTH_LONG).show()
                }
                binding.NewDoctorAreaTextFiled.text.isNullOrEmpty() -> {
                    Snackbar.make(view, Massages.enterDoctorArea, Snackbar.LENGTH_LONG).show()
                }
                else -> {
                    val doctor = DoctorModel(
                        binding.NewDoctorNameTextFiled.text.toString(),
                        binding.NewDoctorSpecializationTextFiled.text.toString(),
                        binding.NewDoctorAreaTextFiled.text.toString(),
                        doctorLocation.latitude.toString(),
                        doctorLocation.longitude.toString()
                    )
                    viewModel.saveNewDoctor(doctor)
                    binding.NewDoctorNameTextFiled.text!!.clear()
                    binding.NewDoctorSpecializationTextFiled.text!!.clear()
                    binding.NewDoctorAreaTextFiled.text!!.clear()
                }
            }
        }

        //if gps not enabled show the massage
        viewModel.massageLiveData.observeForever {
            if (this.requireView().isVisible){
                Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()
            }
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        uid.let { viewModel.getAreaList() }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}