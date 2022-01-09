package com.peter_kameel.pyramidsscientificofficecrm.helper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DailyVisitModel
import kotlinx.android.synthetic.main.item_doctor_name_recycler.view.*

class DailyVisitRecyclerAdapter (private var list: ArrayList<DailyVisitModel>)
    : RecyclerView.Adapter<DailyVisitRecyclerAdapter.ViewHolder>() {

    class ViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val name = item.item_doctor_name_in_recycler!!
        val specialization = item.item_doctor_Specialization_in_recycler!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_doctor_name_recycler, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        if (item.time == "AM"){
            holder.name.text = item.hospital!!.name
        }else if (item.time == "PM"){
            holder.name.text = item.doctor!!.name
            holder.specialization.text = item.doctor!!.specialization
        }
    }

    override fun getItemCount() = list.size
}