package com.peter_kameel.pyramidsscientificofficecrm.helper.interfaces

import com.peter_kameel.pyramidsscientificofficecrm.pojo.LoginModel

interface ClickInsideFragmentListener {
    fun getMedical(item: LoginModel,type: String, date: String?)
}