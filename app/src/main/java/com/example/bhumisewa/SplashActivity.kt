package com.example.bhumisewa

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bhumisewa.ui.theme.BhumiColors
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {
    private val ADMIN_EMAIL = "admin@bhumisewa.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BhumiSewaSplash(
                onNavigate = { isLoggedIn, isAdmin ->
                    when {
                        isLoggedIn && isAdmin -> startActivity(Intent(this, AdminDashboardActivity::class.java))
                        isLoggedIn            -> startActivity(Intent(this, MainActivity::class.java))
                        else                  -> startActivity(Intent(this, LoginActivity::class.java))
                    }
                    finish()
                },
                adminEmail = ADMIN_EMAIL
            )
        }
    }
}

@Composable
fun BhumiSewaSplash(onNavigate: (Boolean, Boolean) -> Unit, adminEmail: String) {
    val alpha by animateFloatAsState(targetValue = 1f, animationSpec = tween(1000), label = "")
    val rotate by animateFloatAsState(targetValue = 0f, animationSpec = tween(1200, easing = EaseOutBack), label = "")

    LaunchedEffect(Unit) {
        delay(2200)
        val user = FirebaseAuth.getInstance().currentUser
        onNavigate(user != null, user?.email == adminEmail)
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(Brush.verticalGradient(listOf(BhumiColors.Background, BhumiColors.Surface2))),
        contentAlignment = Alignment.Center
    ) {
        // Decorative mandala-like circles
        Box(modifier = Modifier.size(480.dp).align(Alignment.TopCenter).offset(y = (-120).dp)
            .background(BhumiColors.PrimaryDim.copy(0.4f), CircleShape))
        Box(modifier = Modifier.size(240.dp).align(Alignment.BottomStart).offset((-60).dp, 60.dp)
            .background(BhumiColors.AccentDim.copy(0.5f), CircleShape))
        Box(modifier = Modifier.size(120.dp).align(Alignment.BottomEnd).offset(40.dp, (-80).dp)
            .background(BhumiColors.SageDim.copy(0.6f), CircleShape))

        Column(
            modifier = Modifier.alpha(alpha),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo — stylized house with terracotta bg
            Box(
                modifier = Modifier.size(96.dp)
                    .background(
                        Brush.radialGradient(listOf(BhumiColors.PrimaryLight, BhumiColors.PrimaryDark)),
                        RoundedCornerShape(28.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("🏡", style = TextStyle(fontSize = 44.sp))
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                "BhumiSewa",
                style = TextStyle(
                    fontSize = 44.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = BhumiColors.Primary,
                    letterSpacing = (-2.0).sp
                )
            )
            Text(
                "भूमिसेवा",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = BhumiColors.Accent,
                    letterSpacing = 2.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Your trusted property partner",
                style = TextStyle(fontSize = 13.sp, color = BhumiColors.TextMuted, letterSpacing = 0.5.sp)
            )

            Spacer(modifier = Modifier.height(64.dp))

            // Dot loader
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                repeat(3) { i ->
                    val dotAlpha by animateFloatAsState(
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(500, delayMillis = i * 180),
                            repeatMode = RepeatMode.Reverse
                        ), label = ""
                    )
                    Box(
                        modifier = Modifier.size(9.dp).alpha(dotAlpha)
                            .background(
                                if (i == 1) BhumiColors.Accent else BhumiColors.Primary,
                                CircleShape
                            )
                    )
                }
            }
        }
    }
}