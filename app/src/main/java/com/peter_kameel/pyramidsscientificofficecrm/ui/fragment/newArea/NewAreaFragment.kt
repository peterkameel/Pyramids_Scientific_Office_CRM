package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.newArea

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.helper.adapters.AreaRecyclerAdapter
import com.peter_kameel.pyramidsscientificofficecrm.util.Shared
import com.peter_kameel.pyramidsscientificofficecrm.util.SharedTag
import kotlinx.android.synthetic.main.new_area_fragment.view.*

class NewAreaFragment : Fragment() {

    private val viewModel by viewModels<NewAreaViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.new_area_fragment, container, false)
        // RecycleView set LayoutManager
        view.AreaRecyclerView.setHasFixedSize(true)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        view.AreaRecyclerView.layoutManager = manager
        //get user id
        val uid = Shared.readSharedString(context!!, SharedTag.UID,"false").toString()
        //call area list
        viewModel.getAreaList(uid)
        //on live data set changed
        viewModel.areaLiveData.observeForever {
            view.AreaRecyclerView.adapter = AreaRecyclerAdapter(it)
        }
        //add a new area to database
        view.Save_New_Area.setOnClickListener {
            if (view.New_Area_Name_.text.isNullOrEmpty()){
                Snackbar.make(view,"Enter Area Name", Snackbar.LENGTH_LONG).show()
            }else{
                viewModel.saveNewArea(view.New_Area_Name_.text.toString(),uid)
                view.New_Area_Name_.text!!.clear()
            }
        }

        return view
    }

}