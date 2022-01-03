package com.peter_kameel.pyramidsscientificofficecrm.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.dailyVisit.DailyVisitFragment
import com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.fragmentMain.MainFragment
import com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.newArea.NewAreaFragment
import com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.newDoctor.NewDoctorFragment
import com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.newHospital.NewHospitalFragment
import com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.newUser.NewMedicalRP
import com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.weeklyPlan.WeeklyPlanFragment
import com.peter_kameel.pyramidsscientificofficecrm.util.Shared
import com.peter_kameel.pyramidsscientificofficecrm.util.SharedTag
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var toggel: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setup Navigation Menu
        toggel = ActionBarDrawerToggle(this,drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(toggel)
        toggel.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //On Item selected for navigation
        navigation.setNavigationItemSelectedListener(this)
        if (Shared.readSharedString(this,SharedTag.permission,"") == "mr"){
            navigation.menu.clear()
            navigation.inflateMenu(R.menu.navigation_menu)
        }else if (Shared.readSharedString(this,SharedTag.permission,"") == "sup"){
            navigation.menu.clear()
            navigation.inflateMenu(R.menu.navigation_menu_sup)
        }

        Log.e("permission",Shared.readSharedString(this,SharedTag.permission,"").toString())
        Log.e("UID", Shared.readSharedString(this,SharedTag.UID,"").toString())
        Log.e("user_name", Shared.readSharedString(this,SharedTag.user_name,"").toString())
        Log.e("supervisor", Shared.readSharedString(this,SharedTag.supervisor_ID,"").toString())


        //Show the main Fragment
        replaceFragment(MainFragment(),SharedTag.MainFragmentTAG)

    }

    //On toggle selected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggel.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //On item Selected From Navigation Menu
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
       when(item.itemId) {
           R.id.Dashboard -> {
               replaceFragment(MainFragment(),SharedTag.MainFragmentTAG)
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
        //for not skipping login activity
        Shared.saveSharedBoolean(this, SharedTag.User_Found, false)
        //Move to Login activity
        startActivity(Intent(this, LoginActivity::class.java))
        //close this activity
        finish()
    }

    override fun onBackPressed() {
        val frag =
            supportFragmentManager.findFragmentByTag(SharedTag.MainFragmentTAG)
        if (frag != null && frag.isVisible) {
            super.onBackPressed()
        } else {
            replaceFragment(MainFragment(),SharedTag.MainFragmentTAG)
        }
    }
}