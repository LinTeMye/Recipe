package com.example.recipe.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipe.activites.MainActivity
import com.example.recipe.activites.MealActivity
import com.example.recipe.adapters.MealsAdapter
import com.example.recipe.databinding.FragmentSearchBinding
import com.example.recipe.videoModel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {
        private lateinit var binding: FragmentSearchBinding
        private lateinit var viewModel: HomeViewModel
        private  lateinit var searchRecyclerviewAdapter: MealsAdapter
    private var searchJob: Job? = null

    companion object{
        const val MEAL_ID = "com.example.recipe.fragments.idMeal"
        const val MEAL_NAME = "com.example.recipe.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.recipe.fragments.nameThumb"
        const val CATEGORY_NAME = "com.example.recipe.fragments.categoryName"

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            viewModel = (activity as MainActivity).ViewModel
        searchRecyclerviewAdapter = MealsAdapter(object : MealsAdapter.OnItemClickListener {
            override fun onItemClick(mealId: String) {
                val intent = Intent(requireContext(), MealActivity::class.java)
                intent.putExtra(FavoritesFragment.MEAL_ID, mealId)
                startActivity(intent)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerView()
        setupSearchTextListener()
        observeSearchedMealsLiveData()
        onSearchItemClick()



    }

    private fun onSearchItemClick() {
        searchRecyclerviewAdapter.onItemClick = {meal->
            val intent = Intent(activity,MealActivity::class.java)
            intent.putExtra(FavoritesFragment.MEAL_ID,meal.idMeal)
            intent.putExtra(FavoritesFragment.MEAL_NAME,meal.strMeal)
            intent.putExtra(FavoritesFragment.MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)
        }
    }



    private fun setupSearchTextListener() {
        binding.edSearchBox.addTextChangedListener { searchQuery ->
            searchJob?.cancel()
            searchJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(500)
                viewModel.searchMeals(searchQuery.toString())
            }
        }
    }


    private fun observeSearchedMealsLiveData() {
        viewModel.observeSearchMealsLiveData().observe(viewLifecycleOwner, Observer { mealsList ->
            searchRecyclerviewAdapter.differ.submitList(mealsList)
        })
    }


    private fun prepareRecyclerView() {
        searchRecyclerviewAdapter = MealsAdapter(object : MealsAdapter.OnItemClickListener {
            override fun onItemClick(mealId: String) {
                // Handle item click, start MealActivity with mealId
                val intent = Intent(requireContext(), MealActivity::class.java)
                intent.putExtra("MEAL_ID", mealId)
                startActivity(intent)
            }
            })

        binding.rvSearchedMeals.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = searchRecyclerviewAdapter
        }
    }
    interface OnSearchItemClickListener {
        fun onSearchItemClick(mealId: String)
    }
}