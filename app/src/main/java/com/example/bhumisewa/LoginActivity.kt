package com.example.bhumisewa

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bhumisewa.ui.theme.BhumiColors
import com.example.bhumisewa.ui.theme.BhumiType
import com.example.bhumisewa.view.BhumiPrimaryButton
import com.example.bhumisewa.viewmodel.AuthViewModel

class LoginActivity : ComponentActivity() {
    private val ADMIN_EMAIL = "admin@bhumisewa.com"
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginScreen(
                onLogin = { email, password ->
                    authViewModel.login(email, password) { success, msg ->
                        if (success) {
                            val dest = if (email == ADMIN_EMAIL) AdminDashboardActivity::class.java else MainActivity::class.java
                            startActivity(Intent(this, dest).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            })
                        } else Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                    }
                },
                onGoRegister = { startActivity(Intent(this, RegisterActivity::class.java)) }
            )
        }
    }
}

@Composable
fun LoginScreen(onLogin: (String, String) -> Unit, onGoRegister: () -> Unit) {
    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading  by remember { mutableStateOf(false) }
    var showPass by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Warm gradient background
        Box(
            modifier = Modifier.fillMaxSize()
                .background(Brush.verticalGradient(listOf(BhumiColors.PrimaryDim, BhumiColors.Background, BhumiColors.Surface2)))
        )

        // Top decorative arc
        Box(
            modifier = Modifier.fillMaxWidth().height(220.dp).align(Alignment.TopCenter)
                .background(
                    Brush.radialGradient(
                        listOf(BhumiColors.Primary.copy(0.15f), Color.Transparent),
                        radius = 600f
                    )
                )
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 28.dp).systemBarsPadding(),
            verticalArrangement = Arrangement.Center
        ) {
            // Logo block
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Box(
                    modifier = Modifier.size(56.dp).background(BhumiColors.Primary, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) { Text("🏡", style = TextStyle(fontSize = 24.sp)) }
                Column {
                    Text("BhumiSewa", style = BhumiType.headingLg.copy(color = BhumiColors.Primary))
                    Text("भूमिसेवा", style = BhumiType.bodyMd.copy(color = BhumiColors.Accent))
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text("Welcome back", style = BhumiType.bodyMd.copy(color = BhumiColors.TextSecondary))
            Text("Sign In", style = BhumiType.displayLg.copy(color = BhumiColors.TextPrimary))
            Spacer(modifier = Modifier.height(30.dp))

            // Email
            Text("Email Address", style = BhumiType.label.copy(color = BhumiColors.TextSecondary))
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = email, onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("you@email.com", style = BhumiType.bodyMd.copy(color = BhumiColors.TextHint)) },
                singleLine = true, shape = RoundedCornerShape(14.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = bhumiFieldColors()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Password
            Text("Password", style = BhumiType.label.copy(color = BhumiColors.TextSecondary))
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = password, onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Your password", style = BhumiType.bodyMd.copy(color = BhumiColors.TextHint)) },
                singleLine = true, shape = RoundedCornerShape(14.dp),
                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    TextButton(onClick = { showPass = !showPass }, contentPadding = PaddingValues(8.dp)) {
                        Text(if (showPass) "Hide" else "Show", style = BhumiType.label.copy(color = BhumiColors.Primary))
                    }
                },
                colors = bhumiFieldColors()
            )
            Spacer(modifier = Modifier.height(28.dp))

            BhumiPrimaryButton("SIGN IN", onClick = { loading = true; onLogin(email.trim(), password) }, modifier = Modifier.fillMaxWidth(), isLoading = loading)
            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Text("Don't have an account? ", style = BhumiType.bodyMd.copy(color = BhumiColors.TextSecondary))
                TextButton(onClick = onGoRegister, contentPadding = PaddingValues(0.dp)) {
                    Text("Register", style = BhumiType.titleMd.copy(color = BhumiColors.Primary, fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}

@Composable
fun bhumiFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor      = BhumiColors.Primary,
    unfocusedBorderColor    = BhumiColors.Divider,
    focusedContainerColor   = BhumiColors.White,
    unfocusedContainerColor = BhumiColors.Surface2,
    focusedTextColor        = BhumiColors.TextPrimary,
    unfocusedTextColor      = BhumiColors.TextPrimary,
    cursorColor             = BhumiColors.Primary
)