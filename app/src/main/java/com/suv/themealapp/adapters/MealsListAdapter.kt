package com.suv.themealapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.suv.themealapp.R
import com.suv.themealapp.models.meals.Meal
import kotlinx.android.synthetic.main.rv_meal_item.view.*
import java.lang.NullPointerException

class MealsListAdapter(
    private val listener: ClickListener,
    private val mealsList: List<Meal>
) : RecyclerView.Adapter<MealsListAdapter.CategoryListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
        return CategoryListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.rv_meal_item, parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
        try {
            val item = holder.itemView
            item.tv_category_name.text = mealsList[position].strMeal

            Glide.with(item.context)
                .load(mealsList[position].strMealThumb)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(item.imv_category)

        } catch (nullPointerException: NullPointerException){
            nullPointerException.printStackTrace()
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }


    class CategoryListViewHolder(itemView: View, private val listener: ClickListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.onCategoryClick(adapterPosition)
        }

    }

    interface ClickListener {
        fun onCategoryClick(position: Int)
    }
}