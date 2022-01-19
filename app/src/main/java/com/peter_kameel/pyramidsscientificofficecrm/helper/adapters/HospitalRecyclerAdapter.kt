package com.peter_kameel.pyramidsscientificofficecrm.helper.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.BottomSheet
import com.peter_kameel.pyramidsscientificofficecrm.pojo.HospitalModel
import com.peter_kameel.pyramidsscientificofficecrm.ui.activity.MainActivity
import kotlinx.android.synthetic.main.item_doctor_name_recycler.view.*

class HospitalRecyclerAdapter(private var list: ArrayList<HospitalModel>,private val ctx: Context)
    : RecyclerView.Adapter<HospitalRecyclerAdapter.ViewHolder>() {
    //private val sheet : BottomSheet = BottomSheet()
    class ViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val layoutItem = item.item_layout!!
        val name = item.item_doctor_name_in_recycler!!
        val activity = item.context as MainActivity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_doctor_name_recycler, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.name.text = item.name

        holder.layoutItem.setOnClickListener {
            BottomSheet(ctx,item,null).show(holder.activity.supportFragmentManager,"tag")
        }

    }

    override fun getItemCount() = list.size

    private fun intentLocation(lat: String, lon: String){
        // Creates an Intent that will load a map of San Francisco
        val gmmIntentUri = Uri.parse("geo:0,0?q=$lat,$lon")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        ctx.startActivity(mapIntent)
    }
}