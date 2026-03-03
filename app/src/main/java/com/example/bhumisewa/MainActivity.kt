package com.example.bhumisewa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bhumisewa.ui.theme.BhumiColors
import com.example.bhumisewa.ui.theme.BhumiType
import com.example.bhumisewa.view.*
import com.example.bhumisewa.viewmodel.AuthViewModel
import com.example.bhumisewa.viewmodel.BookingViewModel
import com.example.bhumisewa.viewmodel.HouseViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    private val houseViewModel: HouseViewModel by viewModels()
    private val bookingViewModel: BookingViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BhumiSewaApp(houseViewModel = houseViewModel, bookingViewModel = bookingViewModel, authViewModel = authViewModel)
        }
    }
}

data class NavItem(val icon: String, val label: String)

@Composable
fun BhumiSewaApp(houseViewModel: HouseViewModel, bookingViewModel: BookingViewModel, authViewModel: AuthViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val userName = FirebaseAuth.getInstance().currentUser?.displayName ?: "Guest"
    val navItems = listOf(NavItem("🏠", "Home"), NavItem("🔍", "Search"), NavItem("❤️", "Saved"), NavItem("📋", "Bookings"), NavItem("👤", "Profile"))

    Scaffold(
        containerColor = BhumiColors.Background,
        bottomBar = { BhumiBottomNav(items = navItems, selected = selectedTab, onSelect = { selectedTab = it }) }
    ) { padding ->
        when (selectedTab) {
            0 -> HomeScreen(padding = padding, userName = userName, houseViewModel = houseViewModel)
            1 -> SearchScreen(padding = padding, houseViewModel = houseViewModel)
            2 -> SavedScreen(padding = padding)
            3 -> MyBookingsScreen(padding = padding, bookingViewModel = bookingViewModel)
            4 -> ProfileScreen(padding = padding, authViewModel = authViewModel)
        }
    }
}

@Composable
fun BhumiBottomNav(items: List<NavItem>, selected: Int, onSelect: (Int) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().shadow(16.dp, RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)),
        color = BhumiColors.White,
        shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { i, item ->
                BhumiNavItem(icon = item.icon, label = item.label, selected = selected == i, onClick = { onSelect(i) })
            }
        }
    }
}

@Composable
fun BhumiNavItem(icon: String, label: String, selected: Boolean, onClick: () -> Unit) {
    val bgColor by animateColorAsState(
        targetValue = if (selected) BhumiColors.PrimaryDim else Color.Transparent,
        animationSpec = tween(250), label = ""
    )
    val labelColor by animateColorAsState(
        targetValue = if (selected) BhumiColors.Primary else BhumiColors.TextMuted,
        animationSpec = tween(250), label = ""
    )
    Surface(onClick = onClick, shape = RoundedCornerShape(14.dp), color = bgColor) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(icon, style = TextStyle(fontSize = if (selected) 22.sp else 20.sp))
            Spacer(modifier = Modifier.height(3.dp))
            Text(label, style = BhumiType.labelSm.copy(color = labelColor, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal))
        }
    }
}