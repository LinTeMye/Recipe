package com.example.recipe.videoModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipe.db.MealDatabase
import com.example.recipe.pojo.Category
import com.example.recipe.pojo.CategoryList
import com.example.recipe.pojo.MealsByCategoryList
import com.example.recipe.pojo.MealsByCategory
import com.example.recipe.pojo.Meal
import com.example.recipe.pojo.MealList
import com.example.recipe.retrofit.Retrofitinstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Query

class HomeViewModel(
    private val mealDatabase: MealDatabase
):ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoryLiveData = MutableLiveData<List<Category>>()
    private var favoritesMealsLiveData = mealDatabase.mealDao().getAllMeals()
    private val searchMealsLiveData = MutableLiveData<List<Meal>>()

    fun getRandomMeal(){
        Retrofitinstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body() !=null){
                    val randomMeal: Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                }else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })
    }
    fun getPopularItems(){
        Retrofitinstance.api.getPopularItems("Seafood").enqueue(object : Callback<MealsByCategoryList>{
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
               if(response.body()!=null){
                   popularItemsLiveData.value=response.body()!!.meals
               }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("HomeFragment",t.message.toString())
            }

        })
    }
    fun getCategories(){
        Retrofitinstance.api.getCategories().enqueue(object : Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                response.body()?.let {categoryList ->
                    categoryLiveData.postValue(categoryList.categories)
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.e("HomeViewModel",t.message.toString())
            }

        })
    }

    fun deleteMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }

    fun insertMeal(meal:Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }

    fun searchMeals(searchQuery: String) = Retrofitinstance.api.searchMeals(searchQuery).enqueue(
        object : Callback<MealList >{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val mealsList = response.body()?.meals
                mealsList?.let {
                    searchMealsLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
               Log.e("Home View Model", t.message.toString())
            }

        }

    )
    fun observeSearchMealsLiveData(): LiveData<List<Meal>> = searchMealsLiveData


    fun observeRandomMealLivedata():LiveData<Meal>{
        return randomMealLiveData
    }
    fun observePopularItemsLiveData():LiveData<List<MealsByCategory>>{
        return popularItemsLiveData
    }

    fun observeCategoriesLiveData():LiveData<List<Category>>{
        return categoryLiveData
    }
    fun observeFavouritesMealsLiveData(): LiveData<List<Meal>>{
        return favoritesMealsLiveData
    }
}