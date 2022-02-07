package com.peter_kameel.pyramidsscientificofficecrm.helper.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.peter_kameel.pyramidsscientificofficecrm.databinding.ItemDoctorNameRecyclerBinding
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DailyVisitModel
import com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.sheets.BottomSheet

class DailyVisitRecyclerAdapter (
    private val ctx: Context,
    private var list: ArrayList<DailyVisitModel>,
    private val activity: AppCompatActivity?)
    : RecyclerView.Adapter<DailyVisitRecyclerAdapter.ViewHolder>() {

    class ViewHolder(val itemBinding: ItemDoctorNameRecyclerBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(visit: DailyVisitModel){
            if (visit.time == "AM"){
                itemBinding.itemDoctorNameInRecycler.text = visit.hospital!!.name
            }else if (visit.time == "PM"){
                itemBinding.itemDoctorNameInRecycler.text = visit.doctor!!.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDoctorNameRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
        holder.itemBinding.itemDetails.setOnClickListener {
            if (activity != null){
                if (item.hospital != null) {
                    BottomSheet(ctx, item.hospital, null).show(
                        activity.supportFragmentManager,
                        "tag"
                    )
                }else if (item.doctor != null){
                    BottomSheet(ctx, null, item.doctor).show(
                        activity.supportFragmentManager,
                        "tag"
                    )
                }
            }
        }
    }

    override fun getItemCount() = list.size
}