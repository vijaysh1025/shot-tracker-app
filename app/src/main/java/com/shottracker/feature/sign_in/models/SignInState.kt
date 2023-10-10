package com.shottracker.feature.sign_in.models

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)
