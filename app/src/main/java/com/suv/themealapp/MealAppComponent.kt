package com.suv.themealapp

import com.suv.themealapp.activities.CategoryActivity
import com.suv.themealapp.modules.RetrofitClientModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetrofitClientModule::class])
interface MealAppComponent {
    fun inject(categoryActivity: CategoryActivity)
}