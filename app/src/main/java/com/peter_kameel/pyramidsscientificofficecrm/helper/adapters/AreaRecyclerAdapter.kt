package com.peter_kameel.pyramidsscientificofficecrm.helper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.pojo.AreaModel
import kotlinx.android.synthetic.main.item_name_recycler.view.*

class AreaRecyclerAdapter (private var list: ArrayList<AreaModel>)
    : RecyclerView.Adapter<AreaRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_name_recycler,parent,false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val item = list[position]
        holder.Name.text = item.name
    }
    override fun getItemCount(): Int = list.size

    class ViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val Name = item.item_name_in_recycler
    }
}