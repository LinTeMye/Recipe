package com.example.recipe.activites

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.recipe.R
import com.example.recipe.db.MealDatabase
import com.example.recipe.videoModel.HomeViewModel
import com.example.recipe.videoModel.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    val ViewModel: HomeViewModel by lazy {
        val mealDatabase = MealDatabase.getInstance(this)
        val homeViewModelProviderFactory=HomeViewModelFactory(mealDatabase)
        ViewModelProvider(this,homeViewModelProviderFactory)[HomeViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.btm_nav)
        val navController = Navigation.findNavController(this, R.id.host_fragment)

        NavigationUI.setupWithNavController(bottomNavigation,navController)
        }
    }
