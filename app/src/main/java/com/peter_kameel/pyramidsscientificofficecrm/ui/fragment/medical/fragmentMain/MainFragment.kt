package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.medical.fragmentMain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.helper.adapters.DailyVisitRecyclerAdapter
import com.peter_kameel.pyramidsscientificofficecrm.helper.adapters.DoctorRecyclerAdapter
import com.peter_kameel.pyramidsscientificofficecrm.helper.adapters.HospitalRecyclerAdapter
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DailyVisitModel
import com.peter_kameel.pyramidsscientificofficecrm.util.Massages
import com.peter_kameel.pyramidsscientificofficecrm.util.Shared
import com.peter_kameel.pyramidsscientificofficecrm.util.SharedTag
import kotlinx.android.synthetic.main.fragment_main.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainFragment(
    private var type: String,
    private var mrID: String?,
) : Fragment() {
    private val viewModel by viewModels<MainFragmentViewModel>()
    private var amList = ArrayList<DailyVisitModel>()
    private var pMList = ArrayList<DailyVisitModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        // AM / PM RecycleView set LayoutManager
        view.AMRecyclerView.setHasFixedSize(true)
        view.PMRecyclerView.setHasFixedSize(true)
        val amManager = LinearLayoutManager(context)
        val pmManager = LinearLayoutManager(context)
        amManager.orientation = LinearLayoutManager.VERTICAL
        pmManager.orientation = LinearLayoutManager.VERTICAL
        view.AMRecyclerView.layoutManager = amManager
        view.PMRecyclerView.layoutManager = pmManager
        //get user id
        val uid = Shared.readSharedString(context!!, SharedTag.UID, "false").toString()
        //show date picker
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        //on chose date
        datePicker.addOnPositiveButtonClickListener {
            view.date_text.text = convertLongToTime(it)
            amList.clear()
            pMList.clear()
            view.AMRecyclerView.adapter!!.notifyDataSetChanged()
            view.PMRecyclerView.adapter!!.notifyDataSetChanged()
            if (type == Massages.typeSupVisit) {
                viewModel.getVisitList(convertLongToTime(it), mrID!!)
                view.date_text.text = "Visit`s of Date:  ${convertLongToTime(it)}"
            } else if (type == Massages.typePlan) {
                viewModel.getPlanListHospital(convertLongToTime(it), mrID!!)
                viewModel.getPlanListDoctor(convertLongToTime(it), mrID!!)
                view.date_text.text = "Plan of Date:  ${convertLongToTime(it)}"
            }

        }
        val calendar = Calendar.getInstance().time
        val date = convertLongToTime(calendar.time)

        view.date_text.text = date

        when (type) {
            Massages.typeMrVisit -> {
                viewModel.getVisitList(date, uid)
                view.date_text.isEnabled = false
            }
            Massages.typeSupVisit -> {
                viewModel.getVisitList(date, mrID!!)
                view.date_text.isEnabled = true
                view.date_text.text = "Visit`s of Date:  $date"
            }
            Massages.typePlan -> {
                viewModel.getPlanListHospital(date, mrID!!)
                viewModel.getPlanListDoctor(date, mrID!!)
                view.date_text.isEnabled = true
                view.date_text.text = "Plan of Date:  $date"
            }
        }

        view.date_text.setOnClickListener {
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
            view.AMRecyclerView.adapter = DailyVisitRecyclerAdapter(amList)
            view.PMRecyclerView.adapter = DailyVisitRecyclerAdapter(pMList)
        }

        viewModel.weeklyPlanHospitalLiveData.observeForever {
            view.AMRecyclerView.adapter = HospitalRecyclerAdapter(it)
        }
        viewModel.weeklyPlanDoctorLiveData.observeForever {
            view.PMRecyclerView.adapter = DoctorRecyclerAdapter(it)
        }

        return view
    }

    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd-MM-yyyy")
        return format.format(date)
    }

}