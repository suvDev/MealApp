package com.suv.themealapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.suv.themealapp.MealApplication
import com.suv.themealapp.R
import com.suv.themealapp.adapters.CategoryListAdapter
import com.suv.themealapp.models.category.Category
import com.suv.themealapp.models.category.ResponseCategoryModel
import com.suv.themealapp.utils.Status
import com.suv.themealapp.viewmodels.CategoryActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class CategoryActivity : AppCompatActivity(), CategoryListAdapter.ClickListener {

    @Inject lateinit var categoryActivityViewModel: CategoryActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        (applicationContext as MealApplication).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // init category api observer
        initObserver()

        // get list of categories on loading
        categoryActivityViewModel.getCategories()
    }

    private fun initObserver(){
        categoryActivityViewModel.categoryList.observe(this,{ resp ->
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
                            val response = resp.data as ResponseCategoryModel
                            initAdapter(response.categories)
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

    private fun initAdapter(listOfCategories: List<Category>){
        val adapter = CategoryListAdapter(this, listOfCategories)
        rv_categories.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_categories.adapter = adapter
    }

    override fun onCategoryClick(position: Int) {

    }
}