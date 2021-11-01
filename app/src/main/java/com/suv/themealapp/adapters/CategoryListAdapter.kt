package com.suv.themealapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.suv.themealapp.R
import com.suv.themealapp.models.category.Category
import kotlinx.android.synthetic.main.rv_category_item.view.*

class CategoryListAdapter(
    private val listener: ClickListener,
    private val categoryList: List<Category>
) : RecyclerView.Adapter<CategoryListAdapter.CategoryListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
        return CategoryListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.rv_category_item, parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
        val item = holder.itemView
        item.tv_category_name.text = categoryList[position].strCategory

        Glide.with(item.context)
            .load(categoryList[position].strCategoryThumb)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .into(item.imv_category)
    }

    override fun getItemCount(): Int {
        return categoryList.size
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