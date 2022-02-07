package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.peter_kameel.pyramidsscientificofficecrm.data.WriteExcelFile
import com.peter_kameel.pyramidsscientificofficecrm.databinding.SelectDateBottomSheetBinding
import com.peter_kameel.pyramidsscientificofficecrm.helper.objects.DateConvert
import com.peter_kameel.pyramidsscientificofficecrm.util.Massages
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SelectDateBottomSheet(
    private val type: String,
) : BottomSheetDialogFragment() {
    private var _binding: SelectDateBottomSheetBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var time1: Long? = null
    private var time2: Long? = null

    @Inject
    lateinit var writeExcelFile: WriteExcelFile
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = SelectDateBottomSheetBinding.inflate(inflater, container, false)
        val view = binding.root
        if (type == Massages.visitsType) {
            binding.SelectSecondDate.visibility = GONE
        }
        //show date picker
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        //show Second date picker
        val datePicker2 =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select last day in plan")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        //on chose date
        datePicker.addOnPositiveButtonClickListener {
            time1 = it
            binding.SelectFirstDate.text = DateConvert.convertDateToText(it)
        }
        //on chose date
        datePicker2.addOnPositiveButtonClickListener {
            time2 = it
            binding.SelectSecondDate.text = DateConvert.convertDateToText(it)
        }
        binding.SelectFirstDate.setOnClickListener {
            datePicker.show(parentFragmentManager, "Date1")
        }
        binding.SelectSecondDate.setOnClickListener {
            datePicker2.show(parentFragmentManager, "Date2")
        }
        binding.Create.setOnClickListener {
            if (time1 != null && time2 != null) {
                val day1 = DateConvert.convertDateToTextWithOutDay(time1!!)
                val filename = "($day1)"
                val dates = DateConvert.getDatesList(time1!!, time2!!)
                writeExcelFile.writePlan(dates, filename)
                this.dismiss()
            }else if (time1 != null && time2 == null) {
                val day = DateConvert.convertDateToText(time1!!)
                writeExcelFile.writeVisit(day)
                this.dismiss()
            }else{
                this.dismiss()
            }
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}