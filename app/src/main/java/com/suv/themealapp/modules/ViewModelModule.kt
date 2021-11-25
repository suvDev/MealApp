package com.suv.themealapp.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.suv.themealapp.annotation.ViewModelKey
import com.suv.themealapp.models.meals.Meal
import com.suv.themealapp.viewmodels.MealsActivityViewModel
import com.suv.themealapp.viewmodels.MealsDetailsViewModel
import com.suv.themealapp.viewmodels.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindsViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MealsDetailsViewModel::class)
    abstract fun bindsMealDetailsViewModel(mealsDetailsViewModel: MealsDetailsViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MealsActivityViewModel::class)
    abstract fun bindsMealsActivityViewModel(mealsActivityViewModel: MealsActivityViewModel) : ViewModel
}