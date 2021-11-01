package com.suv.themealapp

import com.suv.themealapp.activities.MealsActivity
import com.suv.themealapp.activities.MealsDetailsActivity
import com.suv.themealapp.modules.RetrofitClientModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetrofitClientModule::class])
interface MealAppComponent {
    fun inject(mealsActivity: MealsActivity)
    fun inject(mealsDetailsActivity: MealsDetailsActivity)
}