package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.supervisor.dailyVisitForSup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.peter_kameel.pyramidsscientificofficecrm.R

class DailyVisitForSupFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        return view
    }
}