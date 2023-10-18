package com.shottracker.feature.sign_in.models

import com.shottracker.feature.sign_in.UserData

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)


sealed interface AuthState {
    object SignedOut : AuthState

    class SignedIn(val data: UserData) : AuthState

    class SignInFailure(val error:String?) : AuthState

    class SignOutFailure(val error:String?) : AuthState

    object SigningIn : AuthState

    object SigningOut : AuthState
}