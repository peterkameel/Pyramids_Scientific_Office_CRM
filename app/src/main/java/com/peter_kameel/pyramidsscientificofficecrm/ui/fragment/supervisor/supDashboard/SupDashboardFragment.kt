package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.supervisor.supDashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.peter_kameel.pyramidsscientificofficecrm.databinding.SupDashboardFregmentBinding
import com.peter_kameel.pyramidsscientificofficecrm.helper.adapters.SupDashboardRecyclerAdapter
import com.peter_kameel.pyramidsscientificofficecrm.helper.interfaces.ClickInsideFragmentListener
import com.peter_kameel.pyramidsscientificofficecrm.util.Shared
import com.peter_kameel.pyramidsscientificofficecrm.util.SharedTag
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SupDashboardFragment(
    private val listener: ClickInsideFragmentListener
) : Fragment() {
    private var _binding: SupDashboardFregmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val viewModel by viewModels<SupDashboardViewModel>()
    @Inject
    lateinit var shared: Shared
    private val uid: String by lazy {
        shared.readSharedString(SharedTag.UID,"").toString() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SupDashboardFregmentBinding.inflate(inflater, container, false)
        val view = binding.root
        // RecycleView set LayoutManager
        binding.SUPDashboardRecyclerView.setHasFixedSize(true)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        binding.SUPDashboardRecyclerView.layoutManager = manager
        //get list of Medical
        viewModel.getListOfMedical(uid)
        //show the list
        viewModel.medicalLiveData.observeForever {
            binding.SUPDashboardRecyclerView.adapter = SupDashboardRecyclerAdapter(it,listener)
        }
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}