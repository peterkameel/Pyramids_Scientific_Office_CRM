package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DoctorModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.HospitalModel
import com.peter_kameel.pyramidsscientificofficecrm.util.Massages
import kotlinx.android.synthetic.main.bottom_sheet.view.*

class BottomSheet(
    private val ctx: Context,
    private val hospital: HospitalModel?,
    private val doctor: DoctorModel?
) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.bottom_sheet, container, false)
        if (hospital != null){
            view.sheetTextName.text = Massages.itemName + hospital.name
            view.sheetTextSp.text = Massages.itemSp
            view.sheetTextArea.text = Massages.itemArea
        }else if (doctor != null){
            view.sheetTextName.text = Massages.itemName + doctor.name
            view.sheetTextSp.text = Massages.itemSp + doctor.specialization
            view.sheetTextArea.text = Massages.itemArea + doctor.area
        }
        view.sheetButtonLocation.setOnClickListener {
            if (hospital!=null){
                intentLocation(hospital.latitude.toString(),hospital.longitude.toString())
            }else if (doctor != null){
                intentLocation(doctor.latitude.toString(),doctor.longitude.toString())
            }
        }
        return view
    }

    private fun intentLocation(lat: String, lon: String){
        // Creates an Intent that will load a map of San Francisco
        val gmmIntentUri = Uri.parse("geo:0,0?q=$lat,$lon")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        ctx.startActivity(mapIntent)
    }
}