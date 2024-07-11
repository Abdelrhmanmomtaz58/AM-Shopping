package com.momtaz.amshopping.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import com.momtaz.amshopping.data.Category
import com.momtaz.amshopping.data.Product
import com.momtaz.amshopping.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel constructor(
    private val firestore: FirebaseFirestore,
    private val category: Category
) : ViewModel(){

    private val _offerProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val offerProduct =_offerProducts.asStateFlow()

    private val _bestProduct = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProduct =_bestProduct.asStateFlow()

    init {
        fetchOfferProducts()
        fetchBestProductS()
    }

    fun fetchOfferProducts(){
        viewModelScope.launch {
            _offerProducts.emit(Resource.Loading())
        }
        firestore.collection("Products").whereEqualTo("category",category.category)
            .whereNotEqualTo("offerPercentage",null).get()
            .addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _offerProducts.emit(Resource.Success(products))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _offerProducts.emit(Resource.Error(it.message.toString()))
                }
            }

    }
    fun fetchBestProductS(){
        viewModelScope.launch {
            _bestProduct.emit(Resource.Loading())
        }
        firestore.collection("Products").whereEqualTo("category",category.category)
            //.whereEqualTo("offerPercentage",null)
            .get()
            .addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestProduct.emit(Resource.Success(products))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestProduct.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}