package com.peter_kameel.pyramidsscientificofficecrm.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Shared @Inject constructor(
    @ApplicationContext appContext: Context
) {

     private val fileName = "Setting"
     private val ctx = appContext

    // Save String from Shared
    fun saveSharedString(TAG: String?, Value: String?) {
        val sharedPref = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(TAG, Value)
        editor.commit()
    }

    // Read String from Shared
    fun readSharedString(TAG: String?, defaultValue: String?): String? {
        val sharedPref = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        return sharedPref.getString(TAG, defaultValue)
    }

    // Save Boolean from Shared
    fun saveSharedBoolean(TAG: String?, Value: Boolean?) {
        val sharedPref = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(TAG, Value!!)
        editor.apply()
    }

    // Read Boolean from Shared
    fun readSharedBoolean(TAG: String?, defaultValue: Boolean?): Boolean {
        val sharedPref = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        return sharedPref.getBoolean(TAG, defaultValue!!)
    } // also can add another fun to save/read int - and other types of data
}