package com.peter_kameel.pyramidsscientificofficecrm.util

import android.content.Context

object Shared {

    private const val FileName = "Setting"

    // Save String from Shared
    fun saveSharedString(ctx: Context, TAG: String?, Value: String?) {
        val sharedPref = ctx.getSharedPreferences(FileName, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(TAG, Value)
        editor.commit()
    }

    // Read String from Shared
    fun readSharedString(ctx: Context, TAG: String?, defaultValue: String?): String? {
        val sharedPref = ctx.getSharedPreferences(FileName, Context.MODE_PRIVATE)
        return sharedPref.getString(TAG, defaultValue)
    }

    // Save Boolean from Shared
    fun saveSharedBoolean(ctx: Context, TAG: String?, Value: Boolean?) {
        val sharedPref = ctx.getSharedPreferences(FileName, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(TAG, Value!!)
        editor.apply()
    }

    // Read Boolean from Shared
    fun readSharedBoolean(ctx: Context, TAG: String?, defaultValue: Boolean?): Boolean {
        val sharedPref = ctx.getSharedPreferences(FileName, Context.MODE_PRIVATE)
        return sharedPref.getBoolean(TAG, defaultValue!!)
    } // also can add another fun to save/read int - and other types of data
}