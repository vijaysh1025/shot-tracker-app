package com.shottracker.feature.settings.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionResult
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.shot_tracker_app.R
import com.shottracker.feature.sign_in.UserData
import com.shottracker.feature.sign_in.models.AuthState
import com.shottracker.feature.sign_in.models.SignInState
import com.shottracker.ui.theme.SettingsBg


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(authState: AuthState, onSignInClick: () -> Unit, onSignOutClick: () -> Unit, onBack: () -> Unit = {}) {
    val composition: LottieCompositionResult =
        rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_b))
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = SettingsBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Settings")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = SettingsBg,
                )
            )
        }
    ) {
        Surface(modifier = Modifier.padding(it), color = Color.Transparent) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (authState is AuthState.SignedIn) {
                    Profile(authState.data)
                }
                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 10.dp)
                ) {
                    Spacer(
                        modifier = Modifier
                            .height(5.dp)
                            .fillMaxWidth()
                    )
                    if (authState !is AuthState.SignedIn) {
                        SettingsItem2("Sign In", onSignInClick)
                        Spacer(
                            modifier = Modifier
                                .height(10.dp)
                                .fillMaxWidth()
                        )
                    } else {
                        SettingsItem2("Followed Teams")
                        Spacer(
                            modifier = Modifier
                                .height(5.dp)
                                .fillMaxWidth()
                        )
                        SettingsItem2("Sign Out", onSignOutClick)
                        Spacer(
                            modifier = Modifier
                                .height(10.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }

        }
    }
}

@Preview
@Composable
fun Profile(userData: UserData? = null) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (userData?.profilePictureUrl != null) {
            AsyncImage(
                model = userData.profilePictureUrl,
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        if (userData?.username != null) {
            Text(
                text = userData.username,
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
fun SettingsItem(
    icon: Painter = rememberVectorPainter(Icons.Default.Person),
    text: String = "Manage Account",
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 15.dp, vertical = 10.dp)
    ) {
        Icon(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterVertically),
            painter = icon,
            contentDescription = "Person"
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterVertically),
            text = text,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterVertically),
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Person"
        )
    }
}

@Preview
@Composable
fun SettingsItem2(text: String = "Test", onClick: () -> Unit = {}) {
    OutlinedButton(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth(),
        border = BorderStroke(1.dp, Color.Black),
        onClick = onClick
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black
        )
    }
}