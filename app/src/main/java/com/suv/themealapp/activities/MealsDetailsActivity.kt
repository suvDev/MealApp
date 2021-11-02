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
import kotlinx.android.synthetic.main.activity_meal_details.*
import javax.inject.Inject

class MealsDetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var mealsDetailsViewModel: MealsDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        (applicationContext as MealApplication).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_details)

        val mealID = intent?.extras?.getString(Constant.MEAL_ID)
        if (mealID != null && mealID.isNotEmpty()) {
            initObserver()

            // calling meal details api
            mealsDetailsViewModel.getMealDetails(mealID)
        } else {
            // closing the activity if meal id is null or empty
            Toast.makeText(this, Constant.API_ERROR_MESSAGE, Toast.LENGTH_LONG).show()
            finish()
        }

    }

    private fun initObserver() {
        mealsDetailsViewModel.mealDetails.observe(this, { resp ->
            when (resp?.status) {
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    try {
                        progressBar.visibility = View.GONE
                        if (resp.data != null) {
                            val response = resp.data as ResponseMealDetails
                            if (response.meals != null && response.meals.isNotEmpty()) {
                                initUI(response.meals[0])
                            }
                        }else{
                            Toast.makeText(this, Constant.API_ERROR_MESSAGE, Toast.LENGTH_LONG).show()
                            finish()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, Constant.API_ERROR_MESSAGE, Toast.LENGTH_LONG).show()
                        finish()
                        e.printStackTrace()
                    }
                }

                Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, Constant.API_ERROR_MESSAGE, Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        })
    }

    private fun initUI(mealDetails: Meal) {
        try {

            tv_meal_name.text = mealDetails.strMeal

            var text = ""
            mealDetails.strIngredient1?.let {
                if(it.isNotEmpty()) {
                    text += Constant.BULLET_SYMBOL + it
                }
            }
            mealDetails.strIngredient2?.let {
                if(it.isNotEmpty()) {
                    text += Constant.BULLET_SYMBOL + it
                }
            }
            mealDetails.strIngredient3?.let {
                if(it.isNotEmpty()) {
                    text += Constant.BULLET_SYMBOL + it
                }
            }
            mealDetails.strIngredient4?.let {
                if(it.isNotEmpty()) {
                    text += Constant.BULLET_SYMBOL + it
                }
            }
            mealDetails.strIngredient5?.let {
                if(it.isNotEmpty()) {
                    text += Constant.BULLET_SYMBOL + it
                }
            }
            mealDetails.strIngredient6?.let {
                if(it.isNotEmpty()) {
                    text += Constant.BULLET_SYMBOL + it
                }
            }
            mealDetails.strIngredient7?.let {
                if(it.isNotEmpty()) {
                    text += Constant.BULLET_SYMBOL + it
                }
            }
            mealDetails.strIngredient8?.let {
                if(it.isNotEmpty()) {
                    text += Constant.BULLET_SYMBOL + it
                }
            }
            mealDetails.strIngredient9?.let {
                if(it.isNotEmpty()) {
                    text += Constant.BULLET_SYMBOL + it
                }
            }
            mealDetails.strIngredient10?.let {
                if(it.isNotEmpty()) {
                    text += Constant.BULLET_SYMBOL + it
                }
            }
            mealDetails.strIngredient11?.let {
                if(it.isNotEmpty()) {
                    text += Constant.BULLET_SYMBOL + it
                }
            }
            mealDetails.strIngredient12?.let {
                if(it.isNotEmpty()) {
                    text += Constant.BULLET_SYMBOL + it
                }
            }
            mealDetails.strIngredient13?.let {
                if(it.isNotEmpty()) {
                    text += Constant.BULLET_SYMBOL + it
                }
            }
            mealDetails.strIngredient14?.let {
                if(it.isNotEmpty()) {
                    text += Constant.BULLET_SYMBOL + it
                }
            }
            mealDetails.strIngredient15?.let {
                if(it.isNotEmpty()) {
                    text += Constant.BULLET_SYMBOL + it
                }
            }
            mealDetails.strIngredient16?.let {
                if(it.isNotEmpty()) {
                    text += Constant.BULLET_SYMBOL + it
                }
            }
            mealDetails.strIngredient17?.let {
                if(it.isNotEmpty()) {
                    text += Constant.BULLET_SYMBOL + it
                }
            }
            mealDetails.strIngredient18?.let {
                if(it.isNotEmpty()) {
                    text += Constant.BULLET_SYMBOL + it
                }
            }
            mealDetails.strIngredient19?.let {
                if(it.isNotEmpty()) {
                    text += Constant.BULLET_SYMBOL + it
                }
            }
            mealDetails.strIngredient20?.let {
                if(it.isNotEmpty()) {
                    text += Constant.BULLET_SYMBOL + it
                }
            }


            if (text.isEmpty()) {
                tv_ingredients.visibility = View.GONE
            } else {
                tv_ingredients_list.text = text
            }

            Glide.with(this)
                .load(mealDetails.strMealThumb)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(imv_meal)

            mealDetails.strInstructions.let {
                if (it == null || it.isEmpty()) {
                    tv_instructions.visibility = View.GONE
                } else {
                    tv_instructions_list.text = it
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}