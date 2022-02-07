package com.peter_kameel.pyramidsscientificofficecrm.helper.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter_kameel.pyramidsscientificofficecrm.databinding.ItemNameRecyclerBinding
import com.peter_kameel.pyramidsscientificofficecrm.pojo.AreaModel

class AreaRecyclerAdapter (private var list: ArrayList<AreaModel>)
    : RecyclerView.Adapter<AreaRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemNameRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val area = list[position]
        holder.bind(area)
    }
    override fun getItemCount(): Int = list.size

    class ViewHolder(private val itemBinding: ItemNameRecyclerBinding): RecyclerView.ViewHolder(itemBinding.root){
        fun bind(area: AreaModel){
            itemBinding.itemNameInRecycler.text = area.name
        }
    }
}