package com.suv.themealapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suv.themealapp.network.ApiInterface
import com.suv.themealapp.utils.ApiResponseGeneric
import com.suv.themealapp.utils.Constant
import kotlinx.coroutines.launch
import javax.inject.Inject

class CategoryActivityViewModel @Inject constructor(private val apiInterface: ApiInterface) : ViewModel() {


    var categoryList = MutableLiveData<ApiResponseGeneric<Any>>()

    fun getCategories() {
        categoryList.value = ApiResponseGeneric.loading()
        viewModelScope.launch {
            kotlin.runCatching {
                apiInterface.getCategoryList()
            }.onSuccess { result ->
                if(result.code() == 200){
                    categoryList.value = ApiResponseGeneric.success(result.body())
                } else{
                    categoryList.value = ApiResponseGeneric.error(Throwable(Constant.API_ERROR_MESSAGE))
                }
            }.onFailure {
                categoryList.value = ApiResponseGeneric.error(Throwable(Constant.API_ERROR_MESSAGE))
            }
        }
    }

}