package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.sheets

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.peter_kameel.pyramidsscientificofficecrm.databinding.BottomSheetBinding
import com.peter_kameel.pyramidsscientificofficecrm.pojo.DoctorModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.HospitalModel
import com.peter_kameel.pyramidsscientificofficecrm.util.Massages

class BottomSheet(
    private val ctx: Context,
    private val hospital: HospitalModel?,
    private val doctor: DoctorModel?
) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = BottomSheetBinding.inflate(inflater, container, false)
        val view = binding.root
        if (hospital != null){
            binding.sheetTextName.text = Massages.itemName + hospital.name
            binding.sheetTextSp.text = Massages.itemSp
            binding.sheetTextArea.text = Massages.itemArea
        }else if (doctor != null){
            binding.sheetTextName.text = Massages.itemName + doctor.name
            binding.sheetTextSp.text = Massages.itemSp + doctor.specialization
            binding.sheetTextArea.text = Massages.itemArea + doctor.area
        }
        binding.sheetButtonLocation.setOnClickListener {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}