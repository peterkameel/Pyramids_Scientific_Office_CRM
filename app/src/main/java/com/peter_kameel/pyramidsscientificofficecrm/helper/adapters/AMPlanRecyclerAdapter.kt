package com.peter_kameel.pyramidsscientificofficecrm.helper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.pojo.HospitalModel
import kotlinx.android.synthetic.main.item_doctor_name_recycler.view.*

class AMPlanRecyclerAdapter(private val list: ArrayList<HospitalModel>)
    :RecyclerView.Adapter<AMPlanRecyclerAdapter.ViewHolder>() {

    class ViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val name = item.item_doctor_name_in_recycler
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder( LayoutInflater.from(parent.context).inflate(R.layout.item_doctor_name_recycler, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.name.text = item.name
    }

    override fun getItemCount(): Int = list.size

}