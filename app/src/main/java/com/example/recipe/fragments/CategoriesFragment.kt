package com.example.recipe.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipe.R
import com.example.recipe.activites.CategoryMealsActivity
import com.example.recipe.activites.MainActivity
import com.example.recipe.activites.MealActivity
import com.example.recipe.adapters.CategoriesAdapter
import com.example.recipe.adapters.CategoryMealsAdapter
import com.example.recipe.databinding.FragmentCategoriesBinding
import com.example.recipe.videoModel.HomeViewModel
import com.example.recipe.videoModel.MealViewModel


class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var viewModel: HomeViewModel
    private lateinit var categoryMealsAdapter: CategoryMealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryMealsAdapter = CategoryMealsAdapter()
        prepareRecyclerView()
        observeCategories()
        onCategoryClick()
        onCategoryMealClick()
    }




    private fun onCategoryClick() {

            categoriesAdapter.onItemClick = {category->
                val intent = Intent(activity, CategoryMealsActivity::class.java)
                intent.putExtra(HomeFragment.CATEGORY_NAME,category.strCategory)
                startActivity(intent)


        }
    }

    private fun observeCategories() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer { categories ->
            categoriesAdapter.setCategoryList(categories)
        })
    }

    private fun prepareRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun onCategoryMealClick() {
        categoryMealsAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java).apply {
                putExtra(HomeFragment.MEAL_ID, meal.idMeal)
                putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
                putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
            }
            startActivity(intent)
        }
    }

}