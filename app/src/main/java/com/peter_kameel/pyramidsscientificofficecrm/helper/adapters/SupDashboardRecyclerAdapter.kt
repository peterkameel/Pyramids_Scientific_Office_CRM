package com.peter_kameel.pyramidsscientificofficecrm.helper.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter_kameel.pyramidsscientificofficecrm.databinding.SupDashbordRecyclerItemBinding
import com.peter_kameel.pyramidsscientificofficecrm.helper.interfaces.ClickInsideFragmentListener
import com.peter_kameel.pyramidsscientificofficecrm.pojo.LoginModel
import com.peter_kameel.pyramidsscientificofficecrm.util.Massages

class SupDashboardRecyclerAdapter(
    private val list: ArrayList<LoginModel>,
    private val listener: ClickInsideFragmentListener
) :
    RecyclerView.Adapter<SupDashboardRecyclerAdapter.ViewHolder>() {

    class ViewHolder(val itemBinding: SupDashbordRecyclerItemBinding) : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SupDashbordRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.itemBinding.mrName.text = item.user_name
        holder.itemBinding.getDoctors.setOnClickListener { listener.getMedical(item,Massages.typeDoctor,null) }
        holder.itemBinding.getHospitals.setOnClickListener { listener.getMedical(item,Massages.typeHospital,null) }
        holder.itemBinding.getVisits.setOnClickListener { listener.getMedical(item,Massages.typeSupVisit,null) }
        holder.itemBinding.getPlan.setOnClickListener { listener.getMedical(item,Massages.typePlan,null) }

    }

    override fun getItemCount() = list.size

}