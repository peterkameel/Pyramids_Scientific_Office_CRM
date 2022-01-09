package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.supervisor.medicalRpList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.helper.adapters.DoctorRecyclerAdapter
import com.peter_kameel.pyramidsscientificofficecrm.helper.adapters.HospitalRecyclerAdapter
import com.peter_kameel.pyramidsscientificofficecrm.pojo.LoginModel
import com.peter_kameel.pyramidsscientificofficecrm.util.Massages
import kotlinx.android.synthetic.main.sup_list_of_h_d.view.*
import java.text.SimpleDateFormat
import java.util.*

class MedicalRPList(
    private val item: LoginModel,
    private var type: String
) : Fragment() {
    private val viewModel by viewModels<MedicalRPListViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.sup_list_of_h_d, container, false)
        // RecycleView set LayoutManager
        view.H_D_RecyclerView.setHasFixedSize(true)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        view.H_D_RecyclerView.layoutManager = manager

        when (type) {
            Massages.typeDoctor -> {
                view.medical_name_button.text = "${item.user_name} Doctor`s List"
                viewModel.getListOfDoctors(item.id.toString())
            }
            Massages.typeHospital -> {
                view.medical_name_button.text = "${item.user_name} Hospital`s List"
                viewModel.getListOfHospitals(item.id.toString())
            }
        }
        viewModel.doctorLiveData.observeForever {
            view.H_D_RecyclerView.adapter = DoctorRecyclerAdapter(it)
        }

        viewModel.hospitalLiveData.observeForever {
            view.H_D_RecyclerView.adapter = HospitalRecyclerAdapter(it)
        }
        return view
    }

    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd-MM-yyyy")
        return format.format(date)
    }

}