package com.example.assignment2.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment2.data.repository.AuthRepository
import com.example.assignment2.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableLiveData<Resource<String>>()
    val loginState: LiveData<Resource<String>> = _loginState

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _loginState.value = Resource.Error("Username and password cannot be empty.")
            return
        }

        _loginState.value = Resource.Loading
        viewModelScope.launch {
            _loginState.value = authRepository.login(username, password)
        }
    }
}
