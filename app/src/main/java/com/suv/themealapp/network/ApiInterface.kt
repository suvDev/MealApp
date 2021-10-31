package com.suv.themealapp.network

import com.suv.themealapp.models.category.ResponseCategoryModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("/categories.php")
    fun getUserData(): Response<ResponseCategoryModel>

}