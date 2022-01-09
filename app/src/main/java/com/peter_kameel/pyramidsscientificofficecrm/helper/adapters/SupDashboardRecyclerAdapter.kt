package com.peter_kameel.pyramidsscientificofficecrm.helper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.helper.interfaces.ClickInsideFragmentListener
import com.peter_kameel.pyramidsscientificofficecrm.pojo.LoginModel
import com.peter_kameel.pyramidsscientificofficecrm.util.Massages
import kotlinx.android.synthetic.main.sup_dashbord_recycler_item.view.*

class SupDashboardRecyclerAdapter(
    private val list: ArrayList<LoginModel>,
    private val listener: ClickInsideFragmentListener
) :
    RecyclerView.Adapter<SupDashboardRecyclerAdapter.ViewHolder>() {

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val name = item.mr_name!!
        val visit = item.getVisits!!
        val plan = item.getPlan!!
        val doctors = item.getDoctors!!
        val hospitals = item.getHospitals!!
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.sup_dashbord_recycler_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.name.text = item.user_name
        holder.doctors.setOnClickListener { listener.getMedical(item,Massages.typeDoctor,null) }
        holder.hospitals.setOnClickListener { listener.getMedical(item,Massages.typeHospital,null) }
        holder.visit.setOnClickListener { listener.getMedical(item,Massages.typeSupVisit,null) }
        holder.plan.setOnClickListener { listener.getMedical(item,Massages.typePlan,null) }

    }

    override fun getItemCount() = list.size


}