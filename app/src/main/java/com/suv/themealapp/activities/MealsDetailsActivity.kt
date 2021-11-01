package com.suv.themealapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.suv.themealapp.MealApplication
import com.suv.themealapp.R
import com.suv.themealapp.models.mealDetails.Meal
import com.suv.themealapp.models.mealDetails.ResponseMealDetails
import com.suv.themealapp.utils.Constant
import com.suv.themealapp.utils.Status
import com.suv.themealapp.viewmodels.MealsDetailsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.rv_meal_item.view.*
import javax.inject.Inject

class MealsDetailsActivity : AppCompatActivity() {

    @Inject lateinit var mealsDetailsViewModel: MealsDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        (applicationContext as MealApplication).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meals)

        val mealID = intent?.extras?.getString(Constant.MEAL_ID)
        if(mealID!=null && mealID.isNotEmpty()){
            initObserver()
            mealsDetailsViewModel.getMealDetails(mealID)
        } else{
            Toast.makeText(this, Constant.API_ERROR_MESSAGE, Toast.LENGTH_LONG).show()
            finish()
        }

    }

    private fun initObserver() {
        mealsDetailsViewModel.mealDetails.observe(this, { resp ->
            when (resp?.status) {
                Status.LOADING -> {
                    shimmer_view_container.visibility = View.VISIBLE
                    shimmer_view_container.startShimmer()
                }
                Status.SUCCESS -> {
                    try {
                        shimmer_view_container.visibility = View.GONE
                        shimmer_view_container.stopShimmer()
                        if (resp.data != null) {
                            val response = resp.data as ResponseMealDetails
                            if (response.meals != null && response.meals.isNotEmpty()) {
                               initUI(response.meals[0])
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                Status.ERROR -> {
                    shimmer_view_container.visibility = View.GONE
                    shimmer_view_container.stopShimmer()
                }
            }
        })
    }

    private fun initUI(mealDetails: Meal){
        try{
            Glide.with(item.context)
                .load(mealsList[position].strMealThumb)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(item.imv_category)

        }catch (e: Exception){
            e.printStackTrace()
        }
    }

}