package com.suv.themealapp

import com.suv.themealapp.activities.CategoryActivity
import dagger.Component

@Component
interface MealAppComponent {

    fun inject(categoryActivity: CategoryActivity)
}