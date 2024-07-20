package com.momtaz.amshopping.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.momtaz.amshopping.data.order.Order
import com.momtaz.amshopping.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _oder = MutableStateFlow<Resource<Order>>(Resource.Unspecified())
    val oder = _oder.asStateFlow()

    fun placeOrder(oder: Order) {
        viewModelScope.launch {
            _oder.emit(Resource.Loading())
        }
        firestore.runBatch {
            //TODO: ADD the oder into user orders collection
            //TODO: add the oder into orders collection
            //TODO: Delete the product from user cart collection
             oder.userid = auth.uid.toString()
            firestore.collection("user")
                .document(auth.uid!!)
                .collection("orders")
                .document()
                .set(oder)

            firestore.collection("orders")
                .document().set(oder)
            firestore.collection("user").document(auth.uid!!).collection("cart")
                .get()
                .addOnSuccessListener {
                    it.documents.forEach{
                        it.reference.delete()
                    }
                }
        }.addOnSuccessListener {
            viewModelScope.launch {
                _oder.emit(Resource.Success(oder))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _oder.emit(Resource.Error(it.message.toString()))
            }
        }
    }
}