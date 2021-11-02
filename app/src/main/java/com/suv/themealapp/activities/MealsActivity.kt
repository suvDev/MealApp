package com.suv.themealapp.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.suv.themealapp.MealApplication
import com.suv.themealapp.R
import com.suv.themealapp.adapters.MealsListAdapter
import com.suv.themealapp.models.meals.Meal
import com.suv.themealapp.models.meals.ResponseMealsModel
import com.suv.themealapp.utils.Constant
import com.suv.themealapp.utils.Status
import com.suv.themealapp.utils.Utils
import com.suv.themealapp.viewmodels.MealsActivityViewModel
import kotlinx.android.synthetic.main.activity_meals.*
import javax.inject.Inject


class MealsActivity : AppCompatActivity(), MealsListAdapter.ClickListener, View.OnClickListener {

    @Inject lateinit var mealsActivityViewModel: MealsActivityViewModel

    private var listOfMeals = ArrayList<Meal>()
    private var adapter: MealsListAdapter ?= null

    override fun onCreate(savedInstanceState: Bundle?) {

        (applicationContext as MealApplication).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meals)

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
                Utils.hideSoftKeyboard(this, edt_search)
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
                    rv_meals.visibility = View.GONE
                    shimmer_view_container.visibility = View.VISIBLE
                    shimmer_view_container.startShimmer()
                }
                Status.SUCCESS -> {
                    try{
                        rv_meals.visibility = View.VISIBLE
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
                            }else{
                                showSnackBar()
                            }
                        }
                    }catch (e: Exception){
                        showSnackBar()
                        e.printStackTrace()
                    }
                }

                Status.ERROR -> {
                    showSnackBar()
                    rv_meals.visibility = View.VISIBLE
                    shimmer_view_container.visibility = View.GONE
                    shimmer_view_container.stopShimmer()
                }
            }
        })
    }

    private fun showSnackBar(){

        //Displaying snack bar if no search results found or Api fails

        val snackBar = Snackbar
            .make(root_layout_meals, Constant.NO_SEARCH_RESULTS, Snackbar.LENGTH_LONG)
            .setAction("Reset Search?") {
                edt_search.setText("")
                edt_search.requestFocus()
                Utils.showSoftKeyboard(this, edt_search)
            }.setActionTextColor(Color.RED)

        snackBar.show()
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