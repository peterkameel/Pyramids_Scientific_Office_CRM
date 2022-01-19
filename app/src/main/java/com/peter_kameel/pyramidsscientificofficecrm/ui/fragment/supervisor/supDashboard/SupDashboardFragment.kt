package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.supervisor.supDashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.helper.adapters.SupDashboardRecyclerAdapter
import com.peter_kameel.pyramidsscientificofficecrm.helper.interfaces.ClickInsideFragmentListener
import kotlinx.android.synthetic.main.sup_dashboard_fregment.view.*

class SupDashboardFragment(
    private val listener: ClickInsideFragmentListener
) : Fragment() {

    val viewModel by viewModels<SupDashboardViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.sup_dashboard_fregment, container, false)
        // RecycleView set LayoutManager
        view.SUP_Dashboard_RecyclerView.setHasFixedSize(true)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        view.SUP_Dashboard_RecyclerView.layoutManager = manager
        //get list of Medical
        viewModel.getListOfMedical()
        //show the list
        viewModel.medicalLiveData.observeForever {
            view.SUP_Dashboard_RecyclerView.adapter = SupDashboardRecyclerAdapter(it,listener)
        }
        return view
    }
}