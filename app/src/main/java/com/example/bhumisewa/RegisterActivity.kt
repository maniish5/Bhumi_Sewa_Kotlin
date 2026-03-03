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

class RegisterActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegisterScreen(
                onRegister = { name, email, password ->
                    authViewModel.register(name, email, password) { success, msg ->
                        if (success) startActivity(Intent(this, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }) else Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                    }
                },
                onBack = { finish() }
            )
        }
    }
}

@Composable
fun RegisterScreen(onRegister: (String, String, String) -> Unit, onBack: () -> Unit) {
    var fullName by remember { mutableStateOf("") }
    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm  by remember { mutableStateOf("") }
    var showPass by remember { mutableStateOf(false) }
    var loading  by remember { mutableStateOf(false) }
    val passwordMatch = password == confirm || confirm.isEmpty()

    Box(modifier = Modifier.fillMaxSize()
        .background(Brush.verticalGradient(listOf(BhumiColors.Background, BhumiColors.Surface2)))) {

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 28.dp).systemBarsPadding(),
            verticalArrangement = Arrangement.Center
        ) {
            TextButton(onClick = onBack, contentPadding = PaddingValues(0.dp)) {
                Text("← Back", style = BhumiType.titleMd.copy(color = BhumiColors.TextSecondary))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Box(modifier = Modifier.size(56.dp).background(BhumiColors.Primary, RoundedCornerShape(16.dp)), contentAlignment = Alignment.Center) {
                    Text("🏡", style = TextStyle(fontSize = 24.sp))
                }
                Column {
                    Text("BhumiSewa", style = BhumiType.headingLg.copy(color = BhumiColors.Primary))
                    Text("भूमिसेवा", style = BhumiType.bodyMd.copy(color = BhumiColors.Accent))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("Create Account", style = BhumiType.displayLg.copy(color = BhumiColors.TextPrimary))
            Text("Join thousands finding homes with BhumiSewa", style = BhumiType.bodyMd.copy(color = BhumiColors.TextSecondary))
            Spacer(modifier = Modifier.height(28.dp))

            Text("Full Name", style = BhumiType.label.copy(color = BhumiColors.TextSecondary))
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(value = fullName, onValueChange = { fullName = it }, modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Your full name", style = BhumiType.bodyMd.copy(color = BhumiColors.TextHint)) },
                singleLine = true, shape = RoundedCornerShape(14.dp), colors = bhumiFieldColors())
            Spacer(modifier = Modifier.height(14.dp))

            Text("Email Address", style = BhumiType.label.copy(color = BhumiColors.TextSecondary))
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(value = email, onValueChange = { email = it }, modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("you@email.com", style = BhumiType.bodyMd.copy(color = BhumiColors.TextHint)) },
                singleLine = true, shape = RoundedCornerShape(14.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), colors = bhumiFieldColors())
            Spacer(modifier = Modifier.height(14.dp))

            Text("Password", style = BhumiType.label.copy(color = BhumiColors.TextSecondary))
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(value = password, onValueChange = { password = it }, modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Min. 6 characters", style = BhumiType.bodyMd.copy(color = BhumiColors.TextHint)) },
                singleLine = true, shape = RoundedCornerShape(14.dp),
                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = { TextButton(onClick = { showPass = !showPass }, contentPadding = PaddingValues(8.dp)) {
                    Text(if (showPass) "Hide" else "Show", style = BhumiType.label.copy(color = BhumiColors.Primary)) } },
                colors = bhumiFieldColors())
            Spacer(modifier = Modifier.height(14.dp))

            Text("Confirm Password", style = BhumiType.label.copy(color = BhumiColors.TextSecondary))
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(value = confirm, onValueChange = { confirm = it }, modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Repeat password", style = BhumiType.bodyMd.copy(color = BhumiColors.TextHint)) },
                singleLine = true, shape = RoundedCornerShape(14.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = !passwordMatch,
                supportingText = { if (!passwordMatch) Text("Passwords do not match", style = BhumiType.bodyMd.copy(color = BhumiColors.Red)) },
                colors = bhumiFieldColors())
            Spacer(modifier = Modifier.height(24.dp))

            BhumiPrimaryButton("CREATE ACCOUNT",
                onClick = {
                    if (fullName.isBlank() || email.isBlank() || password.length < 6 || !passwordMatch) return@BhumiPrimaryButton
                    loading = true; onRegister(fullName.trim(), email.trim(), password)
                },
                modifier = Modifier.fillMaxWidth(), isLoading = loading)
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Text("Already have an account? ", style = BhumiType.bodyMd.copy(color = BhumiColors.TextSecondary))
                TextButton(onClick = onBack, contentPadding = PaddingValues(0.dp)) {
                    Text("Sign In", style = BhumiType.titleMd.copy(color = BhumiColors.Primary, fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}