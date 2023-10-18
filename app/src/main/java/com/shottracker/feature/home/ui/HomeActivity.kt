package com.shottracker.feature.home.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.compose.runtime.getValue
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.FirebaseApp
import com.shottracker.feature.home.ui.screens.ChartScreen
import com.shottracker.feature.home.ui.screens.GamesScreen
import com.shottracker.feature.home.viewmodel.DailyScheduleViewModel
import com.shottracker.feature.home.viewmodel.PlayByPlayViewModel
import com.shottracker.feature.settings.ui.SettingsScreen
import com.shottracker.feature.sign_in.GoogleAuthUiClient
import com.shottracker.feature.sign_in.models.AuthState
import com.shottracker.feature.sign_in.models.SignInState
import com.shottracker.feature.sign_in.viewmodel.SignInViewModel
import com.shottracker.ui.theme.ShottrackerappTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


/**
 * This Activity is the Entry point to this app.
 */
@ExperimentalAnimationApi
@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    private val dailyScheduleViewModel by viewModels<DailyScheduleViewModel>()
    private val playByPlayViewModel by viewModels<PlayByPlayViewModel>()
    private val signInViewModel by viewModels<SignInViewModel> ()

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        googleAuthUiClient.getSignedInUser()?.let {
            signInViewModel.setAuthState(AuthState.SignedIn(it))
        }

        setContent {
            ShottrackerappTheme {
                // A surface container using the 'background' color from the theme
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        Log.d("AUTHSTATE", "$result")
                        if(result.resultCode == RESULT_OK) {
                            lifecycleScope.launch {
                                val authState = googleAuthUiClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                signInViewModel.setAuthState(authState)
                            }
                        }
                    }
                )

                val authState by signInViewModel.state.collectAsStateWithLifecycle()

                Application(
                    dailyScheduleViewModel = dailyScheduleViewModel,
                    playViewModel = playByPlayViewModel,
                    authState = authState,
                    onSignInClick = {
                        signInViewModel.setAuthState(AuthState.SigningIn)
                        lifecycleScope.launch {
                            val signInIntentSender = googleAuthUiClient.signIn()
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    signInIntentSender ?: return@launch
                                ).build()
                            )
                        }
                    },
                    onSignOutClick = {
                        lifecycleScope.launch {
                            val state = googleAuthUiClient.signOut()
                            signInViewModel.setAuthState(state)
                            Toast.makeText(
                                applicationContext,
                                "Signed out",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Application(
    dailyScheduleViewModel: DailyScheduleViewModel,
    playViewModel: PlayByPlayViewModel,
    authState: AuthState,
    onSignInClick: () -> Unit = {},
    onSignOutClick: () -> Unit = {}
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            GamesScreen(viewModel = dailyScheduleViewModel, {
                navController.navigate("settings")
            }) {
                it?.let {
                    navController.navigate("chart")
                    playViewModel.fetchPlayByPlay(it)
                }
            }
        }
        composable("chart") {
            ChartScreen(viewModel = playViewModel) {
                navController.navigate("main")
            }
        }
        composable("settings") {
            SettingsScreen (
                authState = authState,
                onSignInClick = onSignInClick,
                onSignOutClick = onSignOutClick
            ) {
                navController.navigate("main")
            }
        }
    }
}



