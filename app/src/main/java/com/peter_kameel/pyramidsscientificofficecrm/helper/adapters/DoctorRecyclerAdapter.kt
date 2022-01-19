package com.peter_kameel.pyramidsscientificofficecrm.helper.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DoctorModel
import com.peter_kameel.pyramidsscientificofficecrm.ui.activity.MainActivity
import com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.BottomSheet
import kotlinx.android.synthetic.main.item_doctor_name_recycler.view.*

class DoctorRecyclerAdapter(private var list: ArrayList<DoctorModel>,private val ctx: Context)
: RecyclerView.Adapter<DoctorRecyclerAdapter.ViewHolder>() {

    class ViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val layoutItem = item.item_layout!!
        val name = item.item_doctor_name_in_recycler!!
        val specialization = item.item_doctor_Specialization_in_recycler!!
        val activity = item.context as MainActivity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_doctor_name_recycler,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = list[position].name.toString()
        holder.specialization.text = list[position].specialization.toString()

        holder.layoutItem.setOnClickListener {
            BottomSheet(ctx,null,list[position]).show(holder.activity.supportFragmentManager,"tag")
        }
    }

    override fun getItemCount() = list.size

}