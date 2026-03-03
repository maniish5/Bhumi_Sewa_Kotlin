package com.example.bhumisewa.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bhumisewa.LoginActivity
import com.example.bhumisewa.ui.theme.BhumiColors
import com.example.bhumisewa.ui.theme.BhumiType
import com.example.bhumisewa.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(
    padding: PaddingValues = PaddingValues(),
    authViewModel: AuthViewModel = viewModel()
) {
    val ctx = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() }
    val user = auth.currentUser
    var displayName by remember { mutableStateOf(user?.displayName ?: "Guest") }
    val email = user?.email ?: "Not signed in"
    var showEditDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BhumiColors.Background)
            .padding(padding)
    ) {
        // Profile header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BhumiColors.White)
                .padding(start = 22.dp, end = 22.dp, top = 52.dp, bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(BhumiColors.PrimaryDim, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    displayName.take(1).uppercase(),
                    style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = BhumiColors.Primary)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(displayName, style = BhumiType.headingMd.copy(color = BhumiColors.TextPrimary))
            Text(email, style = BhumiType.bodyMd.copy(color = BhumiColors.TextSecondary))
            Spacer(modifier = Modifier.height(14.dp))
            Surface(
                onClick = { showEditDialog = true },
                shape = RoundedCornerShape(10.dp),
                color = BhumiColors.PrimaryDim
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("✏️", style = TextStyle(fontSize = 12.sp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Edit Profile", style = BhumiType.label.copy(color = BhumiColors.Primary))
                }
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 22.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text("Account", style = BhumiType.labelSm.copy(color = BhumiColors.TextMuted, letterSpacing = 1.sp))
                Spacer(modifier = Modifier.height(6.dp))
            }
            item { ProfileMenuItem("🏠", "My Listings", "Properties you've listed") {} }
            item { ProfileMenuItem("📋", "My Bookings", "Track your booking requests") {} }
            item { ProfileMenuItem("❤️", "Saved Homes", "Your saved properties") {} }
            item { ProfileMenuItem("🔔", "Notifications", "Alerts & updates") {} }

            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text("Support", style = BhumiType.labelSm.copy(color = BhumiColors.TextMuted, letterSpacing = 1.sp))
                Spacer(modifier = Modifier.height(6.dp))
            }
            item { ProfileMenuItem("❓", "Help & FAQ", "How BhumiSewa works") {} }
            item { ProfileMenuItem("📄", "Terms & Privacy", "Legal information") {} }
            item { ProfileMenuItem("⭐", "Rate the App", "Share your feedback") {} }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                Surface(
                    onClick = {
                        auth.signOut()
                        ctx.startActivity(android.content.Intent(ctx, LoginActivity::class.java).apply {
                            flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or
                                    android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
                        })
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = BhumiColors.RedDim
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(40.dp).background(BhumiColors.Red.copy(0.15f), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) { Text("🚪", style = TextStyle(fontSize = 16.sp)) }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Log Out", style = BhumiType.titleLg.copy(color = BhumiColors.Red, fontWeight = FontWeight.Bold))
                            Text("Sign out of your account", style = BhumiType.bodyMd.copy(color = BhumiColors.Red.copy(0.6f)))
                        }
                        Text("›", style = TextStyle(fontSize = 20.sp, color = BhumiColors.Red))
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }

    if (showEditDialog) {
        var newName by remember { mutableStateOf(displayName) }
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            containerColor = BhumiColors.White,
            shape = RoundedCornerShape(20.dp),
            title = { Text("Edit Profile", style = BhumiType.headingMd.copy(color = BhumiColors.TextPrimary)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Display Name", style = BhumiType.label.copy(color = BhumiColors.TextSecondary))
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BhumiColors.Primary,
                            unfocusedBorderColor = BhumiColors.Divider,
                            focusedTextColor = BhumiColors.TextPrimary,
                            unfocusedTextColor = BhumiColors.TextPrimary
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newName.isNotBlank()) {
                            user?.updateProfile(
                                com.google.firebase.auth.UserProfileChangeRequest.Builder()
                                    .setDisplayName(newName.trim()).build()
                            )
                            displayName = newName.trim()
                        }
                        showEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BhumiColors.Primary),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Save", style = BhumiType.titleMd.copy(color = Color.White)) }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel", style = BhumiType.titleMd.copy(color = BhumiColors.TextSecondary))
                }
            }
        )
    }
}

@Composable
fun ProfileMenuItem(icon: String, title: String, subtitle: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = BhumiColors.White,
        shadowElevation = 0.5.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(40.dp).background(BhumiColors.Surface2, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) { Text(icon, style = TextStyle(fontSize = 18.sp)) }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = BhumiType.titleMd.copy(color = BhumiColors.TextPrimary))
                Text(subtitle, style = BhumiType.bodyMd.copy(color = BhumiColors.TextSecondary, fontSize = 11.sp))
            }
            Text("›", style = TextStyle(fontSize = 20.sp, color = BhumiColors.TextMuted))
        }
    }
}