package com.momtaz.amshopping.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.momtaz.amshopping.R
import com.momtaz.amshopping.databinding.ActivityShoppingBinding

class ShoppingActivity : AppCompatActivity() {
   private lateinit var binding :ActivityShoppingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityShoppingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.shoppingHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)


    }
}