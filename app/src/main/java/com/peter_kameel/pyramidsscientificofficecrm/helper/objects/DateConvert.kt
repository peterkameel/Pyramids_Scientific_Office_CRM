package com.peter_kameel.pyramidsscientificofficecrm.helper.objects

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object DateConvert {
    fun convertDateToTextWithOutDay(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return format.format(date)
    }
    fun convertDateToText(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("EE dd-MM-yyyy", Locale.getDefault())
        return format.format(date)
    }
    fun getDatesList(time1: Long,time2: Long): ArrayList<String>{
        val dates = ArrayList<String>()
        val format2 = SimpleDateFormat("EE dd-MM-yyyy", Locale.getDefault())
        var date1: Date? = null
        var date2: Date? = null
        try {
            date1 = format2.parse(this.convertDateToText(time1))
            date2 = format2.parse(this.convertDateToText(time2))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calender1 = Calendar.getInstance()
        calender1.time = date1
        val calender2 = Calendar.getInstance()
        calender2.time = date2
        while (!calender1.after(calender2)) {
            dates.add(this.convertDateToText(calender1.time.time))
            calender1.add(Calendar.DATE, 1)
        }
        return dates
    }
}