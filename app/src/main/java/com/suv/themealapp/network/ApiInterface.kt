package com.suv.themealapp.network

import com.suv.themealapp.models.mealDetails.ResponseMealDetails
import com.suv.themealapp.models.meals.ResponseMealsModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("/api/json/v1/1/filter.php?c=Pasta")
    suspend fun getMealsList(): Response<ResponseMealsModel>

    @GET("/api/json/v1/1/search.php")
    suspend fun searchForMeals(@Query("s") keyword: String): Response<ResponseMealsModel>

    @GET("/api/json/v1/1/lookup.php")
    suspend fun getMealDetails(@Query("i") keyword: String): Response<ResponseMealDetails>

}