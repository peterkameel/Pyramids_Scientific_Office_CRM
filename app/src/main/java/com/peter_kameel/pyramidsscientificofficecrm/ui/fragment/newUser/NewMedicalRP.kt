package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.newUser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.peter_kameel.pyramidsscientificofficecrm.R
import com.peter_kameel.pyramidsscientificofficecrm.util.Shared
import com.peter_kameel.pyramidsscientificofficecrm.util.SharedTag

class NewMedicalRP : Fragment() {

    private var userID : String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.create_new_user, container, false)
        val uid = Shared.readSharedString(context!!, SharedTag.UID, "false").toString()
        userID = uid
        return view
    }

}