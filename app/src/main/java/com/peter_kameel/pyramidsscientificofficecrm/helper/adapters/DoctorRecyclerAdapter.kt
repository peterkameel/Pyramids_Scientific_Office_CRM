package com.peter_kameel.pyramidsscientificofficecrm.helper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DoctorModel
import kotlinx.android.synthetic.main.item_doctor_name_recycler.view.*

class DoctorRecyclerAdapter(private var list: ArrayList<DoctorModel>)
: RecyclerView.Adapter<DoctorRecyclerAdapter.ViewHolder>() {

    class ViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val name = item.item_doctor_name_in_recycler!!
        val specialization = item.item_doctor_Specialization_in_recycler!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_doctor_name_recycler,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = list[position].name.toString()
        holder.specialization.text = list[position].specialization.toString()
    }

    override fun getItemCount() = list.size

}