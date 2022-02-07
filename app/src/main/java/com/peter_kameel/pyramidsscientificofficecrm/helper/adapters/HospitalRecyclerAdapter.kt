package com.peter_kameel.pyramidsscientificofficecrm.helper.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.peter_kameel.pyramidsscientificofficecrm.databinding.ItemDoctorNameRecyclerBinding
import com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.sheets.BottomSheet
import com.peter_kameel.pyramidsscientificofficecrm.pojo.HospitalModel

class HospitalRecyclerAdapter(
    private var list: ArrayList<HospitalModel>,
    private val ctx: Context,
    private val activity: AppCompatActivity?
    )
    : RecyclerView.Adapter<HospitalRecyclerAdapter.ViewHolder>() {

    class ViewHolder(val itemBinding: ItemDoctorNameRecyclerBinding): RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDoctorNameRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.itemBinding.itemDoctorNameInRecycler.text = item.name

        holder.itemBinding.itemDetails.setOnClickListener {
            if (activity != null){
                BottomSheet(ctx,item,null).show(activity.supportFragmentManager,"tag")
            }
        }
    }

    override fun getItemCount() = list.size
}