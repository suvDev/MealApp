package com.suv.themealapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.suv.themealapp.MealApplication
import com.suv.themealapp.R
import com.suv.themealapp.adapters.MealsListAdapter
import com.suv.themealapp.models.meals.Meal
import com.suv.themealapp.models.meals.ResponseMealsModel
import com.suv.themealapp.utils.Constant
import com.suv.themealapp.utils.Status
import com.suv.themealapp.viewmodels.MealsActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MealsActivity : AppCompatActivity(), MealsListAdapter.ClickListener, View.OnClickListener {

    @Inject lateinit var mealsActivityViewModel: MealsActivityViewModel

    private var listOfMeals = ArrayList<Meal>()
    private var adapter: MealsListAdapter ?= null

    override fun onCreate(savedInstanceState: Bundle?) {

        (applicationContext as MealApplication).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //init clickListener and text watcher
        initListenersAndWatchers()

        // init category api observer
        initObserver()

        // get list of meals
        mealsActivityViewModel.getMeals()
    }

    private fun initListenersAndWatchers(){
        imv_search.setOnClickListener(this)
        edt_search.addTextChangedListener {
            if(it!=null && it.length > 2){
                mealsActivityViewModel.searchMeals(it.toString())
            }
        }
    }

    private fun initObserver(){
        mealsActivityViewModel.listOfMeals.observe(this,{ resp ->
            when(resp?.status){
                Status.LOADING -> {
                    shimmer_view_container.visibility = View.VISIBLE
                    shimmer_view_container.startShimmer()
                }
                Status.SUCCESS -> {
                    try{
                        shimmer_view_container.visibility = View.GONE
                        shimmer_view_container.stopShimmer()
                        if(resp.data!=null){
                            val response = resp.data as ResponseMealsModel
                            if(response.meals!=null && response.meals.isNotEmpty()) {
                                listOfMeals.clear()
                                listOfMeals.addAll(response.meals)
                                initAdapter()
                            }
                        }
                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }

                Status.ERROR -> {
                    shimmer_view_container.visibility = View.GONE
                    shimmer_view_container.stopShimmer()
                }
            }
        })

        mealsActivityViewModel.searchedListOfMeals.observe(this,{ resp ->
            when(resp?.status){
                Status.LOADING -> {
                    shimmer_view_container.visibility = View.VISIBLE
                    shimmer_view_container.startShimmer()
                }
                Status.SUCCESS -> {
                    try{
                        shimmer_view_container.visibility = View.GONE
                        shimmer_view_container.stopShimmer()
                        if(resp.data!=null){
                            val response = resp.data as ResponseMealsModel
                            if(response.meals!=null && response.meals.isNotEmpty()) {
                                listOfMeals.clear()
                                listOfMeals.addAll(response.meals)
                                if(adapter==null){
                                    initAdapter()
                                }else {
                                    adapter?.notifyDataSetChanged()
                                }
                            }
                        }
                    }catch (e: Exception){
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

    private fun initAdapter(){
        adapter = MealsListAdapter(this, listOfMeals)
        rv_meals.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_meals.adapter = adapter
    }

    override fun onCategoryClick(position: Int) {
            if(position < listOfMeals.size) {
                val intent = Intent(this, MealsDetailsActivity::class.java)
                intent.putExtra(Constant.MEAL_ID, listOfMeals[position].idMeal)
                startActivity(intent)
            }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.imv_search -> {
                if(edt_search.text!=null && edt_search.text.toString().isNotEmpty()){
                    mealsActivityViewModel.searchMeals(edt_search.text.toString())
                }
            }
        }
    }
}