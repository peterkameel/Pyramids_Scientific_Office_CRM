package com.peter_kameel.pyramidsscientificofficecrm.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.api.Billing
import com.google.firebase.auth.FirebaseAuth
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.data.WriteExcelFile
import com.peter_kameel.pyramidsscientificofficecrm.helper.interfaces.ClickInsideFragmentListener
import com.peter_kameel.pyramidsscientificofficecrm.pojo.LoginModel
import com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.medical.dailyVisit.DailyVisitFragment
import com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.medical.fragmentMain.MainFragment
import com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.medical.newArea.NewAreaFragment
import com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.medical.newDoctor.NewDoctorFragment
import com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.medical.newHospital.NewHospitalFragment
import com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.medical.weeklyPlan.WeeklyPlanFragment
import com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.sheets.SelectDateBottomSheet
import com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.supervisor.medicalRpList.MedicalRPList
import com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.supervisor.newUser.NewMedicalRP
import com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.supervisor.supDashboard.SupDashboardFragment
import com.peter_kameel.pyramidsscientificofficecrm.util.Massages
import com.peter_kameel.pyramidsscientificofficecrm.util.Shared
import com.peter_kameel.pyramidsscientificofficecrm.util.SharedTag
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    ClickInsideFragmentListener,
    NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    @Inject
    lateinit var shared: Shared
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawer = findViewById(R.id.drawer)
        val navigation = findViewById<NavigationView>(R.id.navigation)
        //setup Navigation Menu
        toggle = ActionBarDrawerToggle(this,drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //On Item selected for navigation
        navigation.setNavigationItemSelectedListener(this)
        if (shared.readSharedString(SharedTag.permission,"") == "mr"){
            navigation.menu.clear()
            navigation.inflateMenu(R.menu.navigation_menu)
            //Show the main Fragment
            replaceFragment(MainFragment(Massages.typeMrVisit,null),SharedTag.MainFragmentTAG)
        }else if (shared.readSharedString(SharedTag.permission,"") == "sup"){
            navigation.menu.clear()
            navigation.inflateMenu(R.menu.navigation_menu_sup)
            //Show the main Fragment
            replaceFragment(SupDashboardFragment(this),SharedTag.superFragmentTAG)
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O && shared.readSharedString(SharedTag.permission,"")=="sup"){
            navigation.menu.findItem(R.id.ExportPlan).isEnabled = false
            navigation.menu.findItem(R.id.VisitFile).isEnabled = false
        }
    }

    //On toggle selected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //On item Selected From Navigation Menu
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
       when(item.itemId) {
           R.id.Dashboard -> {
               replaceFragment(MainFragment(Massages.typeMrVisit,null),SharedTag.MainFragmentTAG)
           }
           R.id.SUP_Dashboard -> {
               replaceFragment(SupDashboardFragment(this),SharedTag.superFragmentTAG)
           }
           R.id.New_Area -> {
               replaceFragment(NewAreaFragment(),SharedTag.FragmentTAG)
           }
           R.id.New_Doctor -> {
               replaceFragment(NewDoctorFragment(),SharedTag.FragmentTAG)
           }
           R.id.New_Hospital -> {
               replaceFragment(NewHospitalFragment(),SharedTag.FragmentTAG)
           }
           R.id.visit -> {
               replaceFragment(DailyVisitFragment(),SharedTag.FragmentTAG)
           }
           R.id.plan -> {
               replaceFragment(WeeklyPlanFragment(),SharedTag.FragmentTAG)
           }
           R.id.ADD_Medical_Rep ->{
               replaceFragment(NewMedicalRP(),SharedTag.FragmentTAG)
           }
           R.id.ExportPlan->{
               checkPermission()
               SelectDateBottomSheet(Massages.PlanType).show(supportFragmentManager,"tag")
           }
           R.id.VisitFile->{
               checkPermission()
               SelectDateBottomSheet(Massages.visitsType).show(supportFragmentManager,"tag")
           }
           R.id.privacy->{
           }
           R.id.logout -> {
               logout()
           }
       }
        drawer.closeDrawers()
        return false
    }
    //replace fragment with others
    private fun replaceFragment(fragment: Fragment,tag: String){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, fragment, tag)
            .commit()
    }

    //fun to logout
    private fun logout() {
        //close this activity
        FirebaseAuth.getInstance().signOut()
        //for not skipping login activity
        shared.saveSharedBoolean(SharedTag.User_Found, false)
        //Move to Login activity
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    //Check Permission write External Storage
    private fun checkPermission(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                ) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                //Request The Permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1002
                )
            }
        }
    }

    override fun onBackPressed() {
        val frag =
            supportFragmentManager.findFragmentByTag(SharedTag.MainFragmentTAG)
        val supFrag =
            supportFragmentManager.findFragmentByTag(SharedTag.superFragmentTAG)
        if (
            frag != null &&
            frag.isVisible &&
            shared.readSharedString(SharedTag.permission,"") == "mr") {
            super.onBackPressed()
        }else if (supFrag != null && supFrag.isVisible){
            super.onBackPressed()
        }else{
            if (shared.readSharedString(SharedTag.permission,"") == "mr"){
                replaceFragment(MainFragment(Massages.typeMrVisit,null),SharedTag.MainFragmentTAG)
            }else{
                replaceFragment(SupDashboardFragment(this),SharedTag.superFragmentTAG)
            }
        }
    }

    override fun getMedical(item: LoginModel, type: String, date: String?) {
        if (type == Massages.typeSupVisit || type == Massages.typePlan){
            replaceFragment(MainFragment(type,item.id),SharedTag.MainFragmentTAG)
        }else {
            replaceFragment(MedicalRPList(item,type),SharedTag.FragmentTAG)
        }
    }


}