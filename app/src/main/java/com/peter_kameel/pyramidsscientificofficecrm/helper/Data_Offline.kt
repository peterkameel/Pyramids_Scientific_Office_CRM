package com.peter_kameel.pyramidsscientificofficecrm.helper

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class DataOffline: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}