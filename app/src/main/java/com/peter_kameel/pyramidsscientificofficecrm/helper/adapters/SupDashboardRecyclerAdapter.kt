package com.peter_kameel.pyramidsscientificofficecrm.helper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.pojo.AreaModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.LoginModel
import kotlinx.android.synthetic.main.item_doctor_name_recycler.view.*
import kotlinx.android.synthetic.main.item_name_recycler.view.*
import kotlinx.android.synthetic.main.sup_dashbord_recycler_item.view.*

class SupDashboardRecyclerAdapter(private var list: ArrayList<LoginModel>) :
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
    }

    override fun getItemCount() = list.size


}