package com.suv.themealapp

import android.app.Application

class MealApplication: Application() {

    lateinit var appComponent: MealAppComponent

    override fun onCreate() {
        super.onCreate()
    }
}