package com.suv.themealapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.suv.themealapp.network.ApiInterface
import com.suv.themealapp.utils.ApiResponseGeneric
import com.suv.themealapp.utils.Constant
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MealsDetailsViewModel @Inject constructor(private val apiInterface: ApiInterface): ViewModel(){

    var mealDetails = MutableLiveData<ApiResponseGeneric<Any>>()

    fun getMealDetails(mealId: String) {
        mealDetails.value = ApiResponseGeneric.loading()
        viewModelScope.launch {
            kotlin.runCatching {
                apiInterface.getMealDetails(mealId)
            }.onSuccess { result ->
                if(result.code() == 200){
                    mealDetails.value = ApiResponseGeneric.success(result.body())
                } else{
                    mealDetails.value = ApiResponseGeneric.error(Throwable(Constant.API_ERROR_MESSAGE))
                }
            }.onFailure {
                mealDetails.value = ApiResponseGeneric.error(Throwable(Constant.API_ERROR_MESSAGE))
            }
        }
    }
}