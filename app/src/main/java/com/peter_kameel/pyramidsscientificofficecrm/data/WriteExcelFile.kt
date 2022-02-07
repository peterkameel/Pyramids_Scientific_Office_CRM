package com.peter_kameel.pyramidsscientificofficecrm.data

import com.peter_kameel.pyramidsscientificofficecrm.pojo.DailyVisitModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.LoginModel
import com.peter_kameel.pyramidsscientificofficecrm.pojo.WeeklyPlanModel
import com.peter_kameel.pyramidsscientificofficecrm.util.Shared
import com.peter_kameel.pyramidsscientificofficecrm.util.SharedTag
import dagger.hilt.android.qualifiers.ApplicationContext
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WriteExcelFile @Inject constructor(
    private val firebaseDBRepo: FirebaseDBRepo,
    private val shared: Shared,
) {
    private val visitsFolder = File("/sdcard/Documents/Pyramids/Visits")
    private val plansFolder = File("/sdcard/Documents/Pyramids/Plans")
    lateinit var fileOutputStream: FileOutputStream
    private val workBook = XSSFWorkbook()
    var counter = 0


    //Create Cell Style
    private fun returnCellStyle(): XSSFCellStyle {
        val style = workBook.createCellStyle()
        style.alignment = HorizontalAlignment.CENTER
        return style
    }

    private fun returnCellStyleWithBackground(): XSSFCellStyle {
        val style = workBook.createCellStyle()
        val font = workBook.createFont()
        font.bold = true
        style.alignment = HorizontalAlignment.CENTER
        style.fillForegroundColor = IndexedColors.GREY_40_PERCENT.index
        style.fillPattern = FillPatternType.SOLID_FOREGROUND
        style.setFont(font)
        return style
    }


    //write plan excel file functions
    fun writePlan(dates: ArrayList<String>, fileName: String) {
        firebaseDBRepo.getListOfMedicalBySupID(
            shared.readSharedString(SharedTag.UID, "").toString(),
            onSuccess = {
                if (!it.isNullOrEmpty()) {
                    for (item in it) {
                        getWeeklyPlanList(
                            dates,
                            fileName,
                            item.id.toString(),
                            item.user_name.toString()
                        )
                    }
                }
            },
            onError = {}
        )
    }

    private fun getWeeklyPlanList(
        dates: ArrayList<String>,
        fileName: String,
        mrId: String,
        name: String
    ) {
        firebaseDBRepo.getWeeklyPlanByDateList(
            dates,
            mrId,
            onSuccess = {
                writePlansFile(name, it, fileName)
            },
            onError = {}
        )
    }

    private fun writePlansFile(
        name: String,
        list: ArrayList<WeeklyPlanModel>,
        fileName: String
    ) {
        val format2 = SimpleDateFormat("EE dd-MM-yyyy", Locale.getDefault())
        list.sortBy { format2.parse(it.date) }
        val file = File(plansFolder, "/$fileName.xlsx")
        var sheet: XSSFSheet
        if (!plansFolder.exists()) {
            plansFolder.mkdirs()
        }
        if (workBook.getSheet(name) == null) {
            sheet = workBook.createSheet(name)
            sheet.addMergedRegion(CellRangeAddress.valueOf("A1:E1"))
            val cell = sheet.createRow(0).createCell(0)
            cell.setCellValue("Dr: $name")
            cell.cellStyle = returnCellStyleWithBackground()
            for (i in 0..7) {
                sheet.setColumnWidth(i, 6000)
            }
        } else {
            sheet = workBook.getSheet(name)
        }
        var int = 0
        for (i in 1..20) {
            sheet.createRow(i)
        }
        for (item in list) {
            val cell0 = sheet.getRow(1).createCell(int)
            cell0.setCellValue(item.date)
            cell0.cellStyle = returnCellStyle()
            val cell1 = sheet.getRow(2).createCell(int)
            cell1.setCellValue("AM Visits")
            cell1.cellStyle = returnCellStyleWithBackground()
            if (!item.AM.isNullOrEmpty()) {
                var ix = 3
                for (model in item.AM!!) {
                    val newCell = sheet.getRow(ix).createCell(int)
                    newCell.setCellValue(model.name)
                    newCell.cellStyle = returnCellStyle()
                    ix += 1
                }
            }
            val cell2 = sheet.getRow(7).createCell(int)
            cell2.setCellValue("PM Visits")
            cell2.cellStyle = returnCellStyleWithBackground()
            if (!item.PM.isNullOrEmpty()) {
                var ix = 8
                for (model in item.PM!!) {
                    val newCell = sheet.getRow(ix).createCell(int)
                    newCell.setCellValue(model.name)
                    newCell.cellStyle = returnCellStyle()
                    ix += 1
                }
            }
            int += 1
        }
        try {
            fileOutputStream = FileOutputStream(file)
            workBook.write(fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: Exception) {
        }
    }


    //write visits excel file functions
    fun writeVisit(date: String) {
        firebaseDBRepo.getListOfMedicalBySupID(
            shared.readSharedString(SharedTag.UID, "").toString(),
            onSuccess = {
                if (!it.isNullOrEmpty()) {
                    for (item in it) {
                        getVisitsByDate(item, date)
                    }
                }
            },
            onError = {}
        )
    }

    private fun getVisitsByDate(item: LoginModel, date: String) {
        firebaseDBRepo.getVisitListBYDateAndID(
            date,
            item.id.toString(),
            onSuccess = {
                writeVisitsFile(item.user_name.toString(), it, date)
            },
            onError = {}
        )
    }

    private fun writeVisitsFile(
        name: String,
        list: java.util.ArrayList<DailyVisitModel>,
        date: String
    ) {
        val file = File(visitsFolder, "/($date).xlsx")
        var sheet: XSSFSheet
        if (!visitsFolder.exists()) {
            visitsFolder.mkdirs()
        }
        if (workBook.getSheet(name) == null) {
            sheet = workBook.createSheet(name)
            sheet.addMergedRegion(CellRangeAddress.valueOf("A1:D1"))
            sheet.addMergedRegion(CellRangeAddress.valueOf("A2:D2"))
            for (count in 0..3) {
                sheet.setColumnWidth(count, 5000)
            }
            val cell0 = sheet.createRow(0).createCell(0)
            cell0.setCellValue("Dr: $name")
            cell0.cellStyle = this.returnCellStyleWithBackground()
            val cell1 = sheet.createRow(1).createCell(0)
            cell1.setCellValue("Visit`s of Date:  $date")
            cell1.cellStyle = this.returnCellStyle()
            val row1 = sheet.createRow(2)
            row1.createCell(0).setCellValue("Name")
            row1.createCell(1).setCellValue("Specialization")
            row1.createCell(2).setCellValue("Area")
            row1.createCell(3).setCellValue("Comment")
            for (count in 0..3) {
                row1.getCell(count).cellStyle = returnCellStyleWithBackground()
            }
            counter = 3
        } else {
            sheet = workBook.getSheet(name)
        }
        for (item in list) {
            if (item.time == "AM" && item.hospital != null) {
                val hssfRow = sheet.createRow(counter)
                hssfRow.createCell(0).setCellValue(item.hospital?.name)
                hssfRow.createCell(1).setCellValue("")
                hssfRow.createCell(2).setCellValue("")
                hssfRow.createCell(3).setCellValue(item.comment)
                for (count in 0..3) {
                    hssfRow.getCell(count).cellStyle = returnCellStyle()
                }
                counter += 1
            }
        }
        for (item in list) {
            if (item.time == "PM" && item.doctor != null) {
                val hssfRow = sheet.createRow(counter)
                hssfRow.createCell(0).setCellValue(item.doctor?.name)
                hssfRow.createCell(1).setCellValue(item.doctor?.specialization)
                hssfRow.createCell(2).setCellValue(item.doctor?.area)
                hssfRow.createCell(3).setCellValue(item.comment)
                for (count in 0..3) {
                    hssfRow.getCell(count).cellStyle = returnCellStyle()
                }
                counter += 1
            }
        }
        try {
            fileOutputStream = FileOutputStream(file)
            workBook.write(fileOutputStream)
            fileOutputStream.close()
            counter += 1
        } catch (e: Exception) {
        }
    }

}