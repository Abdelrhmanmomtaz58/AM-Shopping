package com.momtaz.amshopping.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.momtaz.amshopping.data.User
import com.momtaz.amshopping.util.RegisterFieldsState
import com.momtaz.amshopping.util.RegisterValidation
import com.momtaz.amshopping.util.Resource
import com.momtaz.amshopping.util.validateEmail
import com.momtaz.amshopping.util.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _register = MutableStateFlow<Resource<FirebaseUser>>(Resource.Unspecified())
    val register: Flow<Resource<FirebaseUser>> = _register
    private val _validation = Channel<RegisterFieldsState>()
     val validation = _validation.receiveAsFlow()

    fun createAccountWithEmailAndPassword(user: User, password: String) {
        if (checkValidation(user, password)) {
            runBlocking {
                _register.emit(Resource.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener { authResult ->
                    authResult.user?.let {
                        _register.value = Resource.Success(it)
                    }
                }
                .addOnFailureListener { exception ->
                    _register.value = Resource.Error(exception.message.toString())
                }
        }else{
            val registerFieldsState =RegisterFieldsState(
                validateEmail(user.email), validatePassword(password)
            )
            runBlocking {
                _validation.send(registerFieldsState)
            }
        }
    }

    private fun checkValidation(user: User, password: String): Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        return emailValidation is RegisterValidation.Success &&
                passwordValidation is RegisterValidation.Success
    }
}
