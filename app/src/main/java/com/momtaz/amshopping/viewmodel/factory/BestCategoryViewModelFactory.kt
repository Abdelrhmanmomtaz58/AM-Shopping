package com.momtaz.amshopping.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.momtaz.amshopping.data.Category
import com.momtaz.amshopping.viewmodel.CategoryViewModel

class BestCategoryViewModelFactory(
  private val firestore: FirebaseFirestore,
    private val category: Category
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryViewModel(firestore,category) as T
    }
}