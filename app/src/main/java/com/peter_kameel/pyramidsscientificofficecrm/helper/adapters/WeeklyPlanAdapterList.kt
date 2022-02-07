package com.peter_kameel.pyramidsscientificofficecrm.helper.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter_kameel.pyramidsscientificofficecrm.databinding.ItemNameRecyclerBinding
import com.peter_kameel.pyramidsscientificofficecrm.pojo.WeeklyPlanRecyclerModel

class WeeklyPlanAdapterList(private var list: ArrayList<WeeklyPlanRecyclerModel>) :
    RecyclerView.Adapter<WeeklyPlanAdapterList.ViewHolder>() {

    class ViewHolder(val itemBinding: ItemNameRecyclerBinding) : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemNameRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.itemBinding.itemNameInRecycler.text = item.name
    }
}

