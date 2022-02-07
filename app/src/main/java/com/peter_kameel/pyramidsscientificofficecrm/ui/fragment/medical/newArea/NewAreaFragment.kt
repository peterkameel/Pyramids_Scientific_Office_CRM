package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.medical.newArea

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.peter_kameel.pyramidsscientificofficecrm.databinding.NewAreaFragmentBinding
import com.peter_kameel.pyramidsscientificofficecrm.helper.adapters.AreaRecyclerAdapter
import com.peter_kameel.pyramidsscientificofficecrm.pojo.AreaModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewAreaFragment : Fragment() {

    private val viewModel by viewModels<NewAreaViewModel>()
    private var _binding: NewAreaFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NewAreaFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        // RecycleView set LayoutManager
        binding.AreaRecyclerView.setHasFixedSize(true)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        binding.AreaRecyclerView.layoutManager = manager
        //call area list
        viewModel.getAreaList()
        //on live data set changed
        viewModel.areaLiveData.observeForever {
            binding.AreaRecyclerView.adapter = AreaRecyclerAdapter(it)
        }
        //add a new area to database
        binding.SaveNewArea.setOnClickListener {
            if (binding.NewAreaName.text.isNullOrEmpty()){
                Snackbar.make(view,"Enter Area Name", Snackbar.LENGTH_LONG).show()
            }else{
                viewModel.saveNewArea(AreaModel(binding.NewAreaName.text.toString()))
                binding.NewAreaName.text!!.clear()
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