package com.example.recipe.videoModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipe.pojo.MealsByCategory
import com.example.recipe.pojo.MealsByCategoryList
import com.example.recipe.retrofit.Retrofitinstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CategoryMealsViewModel: ViewModel() {
            val mealsLiveData = MutableLiveData<List<MealsByCategory>>()
        fun getMealsByCategory(categoryName:String){
            Retrofitinstance.api.getMealsbyCategory(categoryName).enqueue(object : Callback<MealsByCategoryList>{
                override fun onResponse(
                    call: Call<MealsByCategoryList>,
                    response: Response<MealsByCategoryList>
                ) {
                    response.body()?.let {mealsList->
                        mealsLiveData.postValue(mealsList.meals)

                    }
                }

                override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                    Log.e("CategoryMealsViewModel",t.message.toString())
                }

            })
        }
        fun observeMealsLiveData(): LiveData<List<MealsByCategory>> {
            return mealsLiveData
        }


    }





