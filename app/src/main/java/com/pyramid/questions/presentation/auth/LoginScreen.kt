package com.pyramid.questions.presentation.auth

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.pyramid.questions.AppColors
import com.pyramid.questions.R
import com.pyramid.questions.data.local.GamePreferencesManager
import com.pyramid.questions.navigation.Route
import com.pyramid.questions.presentation.test.ComposableBox3D
import java.util.*

@Composable
fun LoginScreen(
    navController: NavHostController,
    onLoginSuccess: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {}
) {
    val TAG = "LoginScreen"
    val context = LocalContext.current
    val preferencesManager = remember { GamePreferencesManager(context) }
    val currentLocale = remember { Locale(preferencesManager.getLanguage()) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val auth = Firebase.auth
    val db = Firebase.firestore

    val arabicFont = FontFamily(
        Font(
            if (currentLocale == Locale("ar")) {
                R.font.arbic_font_bold_2
            } else {
                R.font.eng3
            }
        )
    )

    // Login function
    fun performLogin() {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = context.getString(R.string.please_fill_all_fields)
            return
        }

        isLoading = true
        errorMessage = ""

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    user?.let {
                        // Update user data in Firestore
                        val userData = hashMapOf(
                            "email" to it.email,
                            "lastLogin" to System.currentTimeMillis(),
                            "isOnline" to true
                        )

                        db.collection("users")
                            .document(it.uid)
                            .update(
                                "email", it.email,
                                "lastLogin", System.currentTimeMillis(),
                                "isOnline", true
                            )
                            .addOnSuccessListener {
                                Log.d(TAG, "User login data updated")
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error updating user data", e)
                            }
                    }
                    onLoginSuccess()
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    errorMessage = when (task.exception?.message) {
                        "The email address is badly formatted." -> context.getString(R.string.invalid_email_format)
                        "There is no user record corresponding to this identifier. The user may have been deleted." -> context.getString(R.string.user_not_found)
                        "The password is invalid or the user does not have a password." -> context.getString(R.string.invalid_password)
                        else -> context.getString(R.string.login_failed)
                    }
                }
            }
    }

    CompositionLocalProvider(
        LocalContext provides LocalContext.current.createConfigurationContext(
            Configuration().apply { setLocale(currentLocale) }
        )
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                AppColors.BackgroundStart,
                                AppColors.BackgroundEnd
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Logo/App Icon
                    Image(
                        painter = painterResource(id = R.drawable.img1), // Replace with your app logo
                        contentDescription = stringResource(R.string.app_logo),
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(
                                width = 3.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        AppColors.GoldColor,
                                        AppColors.GoldColor.copy(alpha = 0.6f)
                                    )
                                ),
                                shape = CircleShape
                            ),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Welcome Text
                    Text(
                        text = stringResource(R.string.welcome_back),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = arabicFont,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = stringResource(R.string.login_subtitle),
                        fontSize = 16.sp,
                        fontFamily = arabicFont,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    // Email Input
                    LoginInputField(
                        value = email,
                        onValueChange = { email = it },
                        label = stringResource(R.string.email),
                        keyboardType = KeyboardType.Email,
                        fontFamily = arabicFont
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password Input
                    LoginInputField(
                        value = password,
                        onValueChange = { password = it },
                        label = stringResource(R.string.password),
                        keyboardType = KeyboardType.Password,
                        isPassword = true,
                        isPasswordVisible = isPasswordVisible,
                        onTogglePasswordVisibility = { isPasswordVisible = !isPasswordVisible },
                        fontFamily = arabicFont
                    )

                    if (errorMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = errorMessage,
                            color = AppColors.AlertRed,
                            fontSize = 14.sp,
                            fontFamily = arabicFont,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Login Button
                    LoginButton(
                        text = stringResource(R.string.login),
                        onClick = { performLogin() },
                        isLoading = isLoading,
                        fontFamily = arabicFont
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Forgot Password
                    Text(
                        text = stringResource(R.string.forgot_password),
                        color = AppColors.GoldColor,
                        fontSize = 14.sp,
                        fontFamily = arabicFont,
                        modifier = Modifier.clickable {
                            // Handle forgot password
                            if (email.isNotBlank()) {
                                auth.sendPasswordResetEmail(email)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            errorMessage = context.getString(R.string.password_reset_sent)
                                        }
                                    }
                            } else {
                                errorMessage = context.getString(R.string.enter_email_for_reset)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Register Link
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.dont_have_account),
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp,
                            fontFamily = arabicFont
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.register_now),
                            color = AppColors.GoldColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = arabicFont,
                            modifier = Modifier.clickable {
                                onNavigateToRegister()
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Skip Login (Guest Mode)
                    Text(
                        text = stringResource(R.string.continue_as_guest),
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp,
                        fontFamily = arabicFont,
                        modifier = Modifier.clickable {
                            navController.navigate(Route.HOME) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LoginInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onTogglePasswordVisibility: () -> Unit = {},
    fontFamily: FontFamily
) {
    ComposableBox3D(
        containerGradient = Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF3B6CB1),
                Color(0xFF5D8CCB)
            )
        ),
        borderColor = AppColors.SecondaryColor.copy(alpha = 0.3f),
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        content = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    label = {
                        Text(
                            text = label,
                            fontFamily = fontFamily,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    visualTransformation = if (isPassword && !isPasswordVisible)
                        PasswordVisualTransformation() else VisualTransformation.None,
                    trailingIcon = if (isPassword) {
                        {
                            IconButton(onClick = onTogglePasswordVisibility) {
                                Icon(
                                    painter = painterResource(
                                        id = if (isPasswordVisible) R.drawable.eye_open else R.drawable.eye_closed
                                    ), // You'll need these icons
                                    contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                                    tint = Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    } else null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = AppColors.GoldColor
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontFamily = fontFamily,
                        fontSize = 16.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                )
            }
        }
    )
}

@Composable
fun LoginButton(
    text: String,
    onClick: () -> Unit,
    isLoading: Boolean = false,
    fontFamily: FontFamily
) {
    ComposableBox3D(
        containerGradient = Brush.horizontalGradient(
            colors = listOf(
                AppColors.GoldColor,
                AppColors.GoldColor.copy(alpha = 0.8f)
            )
        ),
        borderColor = AppColors.GoldColor.copy(alpha = 0.5f),
        shadowEffectColor = AppColors.GoldColor.copy(alpha = 0.3f),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(enabled = !isLoading) { onClick() },
        content = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = text,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        navController = rememberNavController(),
        onLoginSuccess = {},
        onNavigateToRegister = {}
    )
}