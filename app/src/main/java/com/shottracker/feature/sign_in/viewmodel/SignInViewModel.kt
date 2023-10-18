package com.shottracker.feature.sign_in.viewmodel

import androidx.compose.ui.res.stringArrayResource
import androidx.lifecycle.ViewModel
import com.shottracker.feature.sign_in.SignInResult
import com.shottracker.feature.sign_in.models.AuthState
import com.shottracker.feature.sign_in.models.SignInState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class SignInViewModel: ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.SignedOut)
    val state = _state.asStateFlow()

    fun setAuthState(state: AuthState) {
        _state.update { state }
    }

}