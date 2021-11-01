package com.suv.themealapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suv.themealapp.network.ApiInterface
import com.suv.themealapp.utils.ApiResponseGeneric
import com.suv.themealapp.utils.Constant
import kotlinx.coroutines.launch
import javax.inject.Inject

class MealsActivityViewModel @Inject constructor(private val apiInterface: ApiInterface) : ViewModel() {

    var listOfMeals = MutableLiveData<ApiResponseGeneric<Any>>()
    var searchedListOfMeals = MutableLiveData<ApiResponseGeneric<Any>>()

    fun getMeals() {
        listOfMeals.value = ApiResponseGeneric.loading()
        viewModelScope.launch {
            kotlin.runCatching {
                apiInterface.getMealsList()
            }.onSuccess { result ->
                if(result.code() == 200){
                    listOfMeals.value = ApiResponseGeneric.success(result.body())
                } else{
                    listOfMeals.value = ApiResponseGeneric.error(Throwable(Constant.API_ERROR_MESSAGE))
                }
            }.onFailure {
                listOfMeals.value = ApiResponseGeneric.error(Throwable(Constant.API_ERROR_MESSAGE))
            }
        }
    }

    fun searchMeals(keyword: String) {
        searchedListOfMeals.value = ApiResponseGeneric.loading()
        viewModelScope.launch {
            kotlin.runCatching {
                apiInterface.searchForMeals(keyword)
            }.onSuccess { result ->
                if(result.code() == 200){
                    searchedListOfMeals.value = ApiResponseGeneric.success(result.body())
                } else{
                    searchedListOfMeals.value = ApiResponseGeneric.error(Throwable(Constant.API_ERROR_MESSAGE))
                }
            }.onFailure {
                searchedListOfMeals.value = ApiResponseGeneric.error(Throwable(Constant.API_ERROR_MESSAGE))
            }
        }
    }


}