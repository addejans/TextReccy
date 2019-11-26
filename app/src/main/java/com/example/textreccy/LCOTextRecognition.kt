package com.example.textreccy

import android.app.Application

import com.google.firebase.FirebaseApp

class LCOTextRecognition : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }

    companion object {
        val RESULT_TEXT = "RESULT_TEXT"
    }
}
