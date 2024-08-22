package com.example.newsapp.biometric

import android.content.Context
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.main.MainViewModel
import com.example.newsapp.R
import com.example.newsapp.navigation.NavGraph

private var isSuccess = mutableStateOf(false)

@Composable
fun CheckIfBiometricIsAvailable(mainViewModel: MainViewModel) {
    val context = LocalContext.current
    val biometricManager = remember { BiometricManager.from(context) }
    val isBiometricAvailable = remember {
        biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
    }
    when (isBiometricAvailable) {
        BiometricManager.BIOMETRIC_SUCCESS -> {
            CheckAuthentication(context, mainViewModel)
        }

        else -> {
            // Biometric features are not available.
            OpenHomeScreen()
        }
    }
}

@Composable
private fun CheckAuthentication(context: Context, mainViewModel: MainViewModel) {
    if (isSuccess.value) {
        OpenHomeScreen()
    } else {
        OpenBiometricPrompt(context, mainViewModel)
    }
}

@Composable
private fun OpenBiometricPrompt(context: Context, mainViewModel: MainViewModel) {
    val executor = remember { ContextCompat.getMainExecutor(context) }
    val biometricPrompt = BiometricPrompt(
        context as FragmentActivity,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                // handle authentication error here
                when (errorCode) {
                    BiometricPrompt.ERROR_NEGATIVE_BUTTON -> {
                        // User clicked the "Exit from the app" button
                        mainViewModel.closeApp()
                    }

                    BiometricPrompt.ERROR_USER_CANCELED -> {
                        // User canceled the authentication process
                        mainViewModel.closeApp()
                    }

                    else -> {
                        // handle other authentication errors here
                        mainViewModel.closeApp()
                        Toast.makeText(
                            context,
                            "Authentication error: $errString",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                // handle authentication success here
                isSuccess.value = true
            }
        }
    )
    val title = stringResource(R.string.biometricTitle)
    val subtitle = stringResource(R.string.biometricSubtitle)
    val negativeButtonText = stringResource(R.string.biometricNegativeButtonText)

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setAllowedAuthenticators(BIOMETRIC_STRONG)
        .setTitle(title)
        .setSubtitle(subtitle)
        .setNegativeButtonText(negativeButtonText)
        .build()

    biometricPrompt.authenticate(promptInfo)
}

@Composable
private fun OpenHomeScreen() {
    val navController = rememberNavController()
    NavGraph(navController = navController)
}