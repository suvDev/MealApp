package com.suv.themealapp

import android.app.Application
import com.suv.themealapp.modules.RetrofitClientModule

class MealApplication: Application() {

    lateinit var appComponent: MealAppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerMealAppComponent.builder().retrofitClientModule(RetrofitClientModule(this)).build()
    }
}