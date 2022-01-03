package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.fragmentMain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.helper.adapters.DailyVisitRecyclerAdapter
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DailyVisitModel
import com.peter_kameel.pyramidsscientificofficecrm.util.Shared
import com.peter_kameel.pyramidsscientificofficecrm.util.SharedTag
import kotlinx.android.synthetic.main.fragment_main.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainFragment: Fragment() {
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
        val uid = Shared.readSharedString(context!!, SharedTag.UID,"false").toString()

        val calendar = Calendar.getInstance().time
        val date = convertLongToTime(calendar.time)
        view.date_text.text = date

        viewModel.getVisitList(date,uid)

        viewModel.visitLiveData.observeForever {
           for (item in it){
               if (item.time.toString() == "AM"){
                   amList.add(item)
               }else if (item.time.toString() == "PM"){
                   pMList.add(item)
               }
           }
            view.AMRecyclerView.adapter = DailyVisitRecyclerAdapter(amList)
            view.PMRecyclerView.adapter = DailyVisitRecyclerAdapter(pMList)
        }

        return view
    }

    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd-MM-yyyy")
        return format.format(date)
    }

}