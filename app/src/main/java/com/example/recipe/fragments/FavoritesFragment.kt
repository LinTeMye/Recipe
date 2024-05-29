package com.example.recipe.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.recipe.activites.MainActivity
import com.example.recipe.activites.MealActivity
import com.example.recipe.adapters.MealsAdapter
import com.example.recipe.databinding.FragmentFavoritesBinding

import com.example.recipe.videoModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar


class FavoritesFragment : Fragment() {
        private lateinit var binding:FragmentFavoritesBinding
        private lateinit var viewModel: HomeViewModel
        private lateinit var favoritesAdapter: MealsAdapter

    companion object{
        const val MEAL_ID = "com.example.recipe.fragments.idMeal"
        const val MEAL_NAME = "com.example.recipe.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.recipe.fragments.nameThumb"
        const val CATEGORY_NAME = "com.example.recipe.fragments.categoryName"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel=(activity as MainActivity).ViewModel

        favoritesAdapter = MealsAdapter(object : MealsAdapter.OnItemClickListener {
            override fun onItemClick(mealId: String) {
                val intent = Intent(requireContext(), MealActivity::class.java)
                intent.putExtra(MEAL_ID, mealId)
                startActivity(intent)
            }
        })
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentFavoritesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
            super.onViewCreated(view, savedInstanceState)
            prepareRecyclerView()
            observeFavorites()
             onFavoriteMealClick()

            val itemTouchHelper  = object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
            ){
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ) = true



                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val deletedMeal = favoritesAdapter.differ.currentList.getOrNull(position)

                    if (deletedMeal != null) {
                        viewModel.deleteMeal(deletedMeal)

                        Snackbar.make(requireView(), "Meal Deleted", Snackbar.LENGTH_LONG).setAction(
                            "Undo"
                        ) {
                            viewModel.insertMeal(deletedMeal)
                        }.show()
                    } else {
                        Toast.makeText(requireContext(), "Meal not found", Toast.LENGTH_SHORT).show()
                    }
                }


            }
            ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavorites)

    }

    private fun onFavoriteMealClick() {
        favoritesAdapter.onItemClick = {meal->
            val intent = Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,meal.idMeal)
            intent.putExtra(MEAL_NAME,meal.strMeal)
            intent.putExtra(MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)
        }
    }





    private fun prepareRecyclerView() {

        binding.rvFavorites.apply {
            layoutManager=GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = favoritesAdapter
        }
    }


    private fun observeFavorites() {
        viewModel.observeFavouritesMealsLiveData().observe(requireActivity(), Observer { meals->
            favoritesAdapter.differ.submitList(meals)

        })
    }

}