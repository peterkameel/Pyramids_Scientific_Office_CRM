package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.fragmentMain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.helper.adapters.DailyVisitRecyclerAdapter
import com.peter_kameel.pyramidsscientificofficecrm.util.Shared
import com.peter_kameel.pyramidsscientificofficecrm.util.SharedTag
import kotlinx.android.synthetic.main.fragment_main.view.*
import java.text.SimpleDateFormat
import java.util.*

class MainFragment: Fragment() {
    private val viewModel by viewModels<MainFragmentViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        // RecycleView set LayoutManager
        //view.Daily_Visit_RecyclerView.setHasFixedSize(true)
        val manager = LinearLayoutManager(context)
        //manager.orientation = LinearLayoutManager.VERTICAL
       // view.Daily_Visit_RecyclerView.layoutManager = manager
        //get user id
        val uid = Shared.readSharedString(context!!, SharedTag.UID,"false").toString()

        val calendar = Calendar.getInstance().time
        val date = convertLongToTime(calendar.time)
        view.date_text.text = date

        viewModel.getVisitList(date,uid)

        viewModel.visitLiveData.observeForever {
           // view.Daily_Visit_RecyclerView.adapter = DailyVisitRecyclerAdapter(it)
        }

        return view
    }

    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd-MM-yyyy")
        return format.format(date)
    }

}