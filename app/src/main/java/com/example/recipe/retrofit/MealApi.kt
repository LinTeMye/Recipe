package com.example.recipe.retrofit

import com.example.recipe.pojo.CategoryList
import com.example.recipe.pojo.MealsByCategoryList
import com.example.recipe.pojo.MealList
import com.example.recipe.pojo.MealsByCategory
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    @GET("random.php")
    fun getRandomMeal():Call<MealList>

    @GET("lookup.php?")
    fun getMealDetails(@Query("i")id:String): Call<MealList>

    @GET("filter.php?")
    fun getPopularItems(@Query("c")categoryName:String) : Call<MealsByCategoryList>

    @GET ("Categories.php")
    fun getCategories() : Call<CategoryList>

    @GET ("filter.php")
    fun getMealsbyCategory(@Query("c")categoryName: String) : Call<MealsByCategoryList>


    @GET ("search.php")
    fun searchMeals(@Query("s") searchQuery: String) : Call<MealList>
}