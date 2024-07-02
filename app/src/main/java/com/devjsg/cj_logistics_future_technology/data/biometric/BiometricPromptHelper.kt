package com.devjsg.cj_logistics_future_technology.data.biometric

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class BiometricPromptHelper @Inject constructor(
    @ActivityContext private val context: Context,
    private val activity: FragmentActivity
) {

    private val executor = ContextCompat.getMainExecutor(context)
    private lateinit var onSuccess: () -> Unit
    private lateinit var onError: (String) -> Unit

    private val biometricPrompt = BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            if (::onError.isInitialized) {
                onError(errString.toString())
            }
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            if (::onSuccess.isInitialized) {
                onSuccess()
            }
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            if (::onError.isInitialized) {
                onError("Authentication failed")
            }
        }
    })

    private val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric login")
        .setSubtitle("Log in using your biometric credential")
        .setNegativeButtonText("Use account password")
        .build()

    fun authenticate(onSuccess: () -> Unit, onError: (String) -> Unit) {
        this.onSuccess = onSuccess
        this.onError = onError

        val biometricManager = BiometricManager.from(context)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                biometricPrompt.authenticate(promptInfo)
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                onError("No biometric features available on this device.")
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                onError("Biometric features are currently unavailable.")
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                onError("The user hasn't associated any biometric credentials with their account.")
            }
        }
    }
}