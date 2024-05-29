package com.example.recipe.activites

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipe.R
import com.example.recipe.adapters.CategoryMealsAdapter
import com.example.recipe.databinding.ActivityCategoryMealsBinding
import com.example.recipe.fragments.HomeFragment
import com.example.recipe.videoModel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {
    lateinit var binding: ActivityCategoryMealsBinding
    lateinit var categoryMealsViewModel: CategoryMealsViewModel
    lateinit var categoryMealsAdapter: CategoryMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)



        categoryMealsViewModel = ViewModelProvider(this)[CategoryMealsViewModel::class.java]


        categoryMealsViewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)
        prepareRecyclerView()
        categoryMealsViewModel.observeMealsLiveData().observe(this, Observer { mealsList ->
            binding.tvCategoryCount.text = mealsList.size.toString()
            categoryMealsAdapter.setMealsList(mealsList)

        })
    }

    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealsAdapter

            categoryMealsAdapter.setOnItemClickListener { meal ->
                val intent = Intent(this@CategoryMealsActivity, MealActivity::class.java).apply {
                    putExtra(HomeFragment.MEAL_ID, meal.idMeal)
                    putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
                    putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
                }
                startActivity(intent)
            }
        }
    }

}

