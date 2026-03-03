package com.example.bhumisewa

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bhumisewa.model.Booking
import com.example.bhumisewa.model.House
import com.example.bhumisewa.ui.theme.BhumiColors
import com.example.bhumisewa.ui.theme.BhumiType
import com.example.bhumisewa.view.BhumiField
import com.example.bhumisewa.view.BhumiPrimaryButton
import com.example.bhumisewa.view.BookingDetailRow
import com.example.bhumisewa.view.StatusBadge
import com.example.bhumisewa.viewmodel.BookingViewModel
import com.example.bhumisewa.viewmodel.HouseViewModel
import com.google.firebase.auth.FirebaseAuth

class AdminDashboardActivity : ComponentActivity() {
    private val houseViewModel: HouseViewModel by viewModels()
    private val bookingViewModel: BookingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AdminDashboard(
                houseViewModel = houseViewModel,
                bookingViewModel = bookingViewModel
            )
        }
    }
}

@Composable
fun AdminDashboard(
    houseViewModel: HouseViewModel,
    bookingViewModel: BookingViewModel
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("📊 Stats", "🏠 Houses", "📋 Bookings", "👤 Account")

    LaunchedEffect(Unit) {
        houseViewModel.loadHouses()
        bookingViewModel.loadAllBookings()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BhumiColors.Background)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BhumiColors.White)
                .padding(start = 22.dp, end = 22.dp, top = 52.dp, bottom = 0.dp)
        ) {
            Text("Admin Panel", style = BhumiType.displayLg.copy(color = BhumiColors.TextPrimary))
            Text("BhumiSewa Management", style = BhumiType.bodyMd.copy(color = BhumiColors.TextSecondary))
            Spacer(modifier = Modifier.height(16.dp))
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = BhumiColors.White,
                contentColor = BhumiColors.Primary,
                edgePadding = 0.dp,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = BhumiColors.Primary
                    )
                }
            ) {
                tabs.forEachIndexed { i, tab ->
                    Tab(
                        selected = selectedTab == i,
                        onClick = { selectedTab = i },
                        text = {
                            Text(
                                tab,
                                style = BhumiType.label.copy(
                                    color = if (selectedTab == i) BhumiColors.Primary else BhumiColors.TextMuted
                                )
                            )
                        }
                    )
                }
            }
        }

        when (selectedTab) {
            0 -> AdminStatsTab(houseViewModel, bookingViewModel)
            1 -> AdminHousesTab(houseViewModel)
            2 -> AdminBookingsTab(bookingViewModel)
            3 -> AdminAccountTab()
        }
    }
}

// ── Stats Tab ─────────────────────────────────────────────────────────────────

@Composable
fun AdminStatsTab(houseViewModel: HouseViewModel, bookingViewModel: BookingViewModel) {
    val houses = houseViewModel.houses
    val bookings = bookingViewModel.allBookings

    LazyColumn(
        contentPadding = PaddingValues(22.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Overview", style = BhumiType.headingMd.copy(color = BhumiColors.TextPrimary))
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AdminStatCard("🏠", "Total Listings", "${houses.size}", BhumiColors.PrimaryDim, BhumiColors.Primary, Modifier.weight(1f))
                AdminStatCard("✅", "Available", "${houses.count { it.isAvailable }}", BhumiColors.GreenDim, BhumiColors.Green, Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AdminStatCard("📋", "Bookings", "${bookings.size}", BhumiColors.BlueDim, BhumiColors.Blue, Modifier.weight(1f))
                AdminStatCard("⏳", "Pending", "${bookings.count { it.status == "Pending" }}", BhumiColors.YellowDim, BhumiColors.Yellow, Modifier.weight(1f))
            }
        }

        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text("Booking Breakdown", style = BhumiType.headingMd.copy(color = BhumiColors.TextPrimary))
            Spacer(modifier = Modifier.height(10.dp))
            Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), color = BhumiColors.White, shadowElevation = 1.dp) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    listOf("Pending", "Confirmed", "Rejected", "Cancelled").forEach { status ->
                        val count = bookings.count { it.status == status }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            StatusBadge(status)
                            Text("$count booking${if (count != 1) "s" else ""}", style = BhumiType.titleMd.copy(color = BhumiColors.TextPrimary))
                        }
                    }
                }
            }
        }

        item {
            Text("Listings by Type", style = BhumiType.headingMd.copy(color = BhumiColors.TextPrimary))
            Spacer(modifier = Modifier.height(10.dp))
            Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), color = BhumiColors.White, shadowElevation = 1.dp) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    listOf("Apartment", "House", "Studio", "Villa").forEach { type ->
                        val count = houses.count { it.type == type }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(type, style = BhumiType.bodyMd.copy(color = BhumiColors.TextSecondary))
                            Text("$count", style = BhumiType.titleMd.copy(color = BhumiColors.Primary))
                        }
                        if (type != "Villa") HorizontalDivider(color = BhumiColors.DividerLight)
                    }
                }
            }
        }
    }
}

@Composable
fun AdminStatCard(icon: String, label: String, value: String, bg: Color, textColor: Color, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, shape = RoundedCornerShape(16.dp), color = bg, shadowElevation = 0.dp) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(icon, style = TextStyle(fontSize = 24.sp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, style = BhumiType.displayLg.copy(color = textColor, fontSize = 28.sp))
            Text(label, style = BhumiType.bodyMd.copy(color = textColor.copy(0.7f)))
        }
    }
}

// ── Houses Tab ────────────────────────────────────────────────────────────────

@Composable
fun AdminHousesTab(houseViewModel: HouseViewModel) {
    val ctx = LocalContext.current
    val houses = houseViewModel.houses
    var showAddDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("${houses.size} Listings", style = BhumiType.titleLg.copy(color = BhumiColors.TextPrimary))
            Button(
                onClick = { showAddDialog = true },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BhumiColors.Primary)
            ) { Text("+ Add Listing", style = BhumiType.label.copy(color = Color.White)) }
        }

        if (houses.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No listings yet", style = BhumiType.bodyMd.copy(color = BhumiColors.TextMuted))
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 22.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(houses) { house ->
                    AdminHouseCard(
                        house = house,
                        onDelete = {
                            houseViewModel.deleteHouse(house.houseId) { _, msg ->
                                Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
                            }
                        },
                        onToggleAvailability = {
                            houseViewModel.updateHouse(house.copy(isAvailable = !house.isAvailable)) { _, msg ->
                                Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddHouseDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { house ->
                houseViewModel.addHouse(house) { success, msg ->
                    Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
                    if (success) showAddDialog = false
                }
            }
        )
    }
}

@Composable
fun AdminHouseCard(house: House, onDelete: () -> Unit, onToggleAvailability: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), color = BhumiColors.White, shadowElevation = 1.dp) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(house.title, style = BhumiType.titleLg.copy(color = BhumiColors.TextPrimary))
                    Text("${house.city} • ${house.type} • ${house.bedrooms}BD", style = BhumiType.bodyMd.copy(color = BhumiColors.TextSecondary))
                    Text(house.price, style = BhumiType.titleMd.copy(color = BhumiColors.Primary))
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (house.isAvailable) BhumiColors.GreenDim else BhumiColors.RedDim
                ) {
                    Text(
                        if (house.isAvailable) "Available" else "Rented",
                        style = BhumiType.labelSm.copy(color = if (house.isAvailable) BhumiColors.Green else BhumiColors.Red),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onToggleAvailability,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        if (house.isAvailable) "Mark Rented" else "Mark Available",
                        style = BhumiType.label.copy(color = BhumiColors.TextSecondary)
                    )
                }
                OutlinedButton(
                    onClick = onDelete,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BhumiColors.Red),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Delete", style = BhumiType.label.copy(color = BhumiColors.Red))
                }
            }
        }
    }
}

@Composable
fun AddHouseDialog(onDismiss: () -> Unit, onAdd: (House) -> Unit) {
    var title by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Apartment") }
    var price by remember { mutableStateOf("") }
    var bedrooms by remember { mutableStateOf("1") }
    var area by remember { mutableStateOf("") }
    var ownerName by remember { mutableStateOf("") }
    var ownerPhone by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = BhumiColors.White,
        shape = RoundedCornerShape(20.dp),
        title = { Text("Add New Listing", style = BhumiType.headingMd.copy(color = BhumiColors.TextPrimary)) },
        text = {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item { BhumiField("Title", title, { title = it }, "e.g. Modern 2BHK Apartment") }
                item { BhumiField("Address", address, { address = it }, "Street / Area") }
                item { BhumiField("City", city, { city = it }, "e.g. Kathmandu") }
                item {
                    Text("Type", style = BhumiType.label.copy(color = BhumiColors.TextSecondary))
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Apartment", "House", "Studio", "Villa").forEach { t ->
                            FilterChip(
                                selected = type == t,
                                onClick = { type = t },
                                label = { Text(t, style = BhumiType.labelSm) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BhumiColors.PrimaryDim,
                                    selectedLabelColor = BhumiColors.Primary
                                )
                            )
                        }
                    }
                }
                item { BhumiField("Price (e.g. Rs. 25,000/mo)", price, { price = it }) }
                item { BhumiField("Bedrooms", bedrooms, { bedrooms = it }, "1") }
                item { BhumiField("Area (e.g. 850 sq ft)", area, { area = it }) }
                item { BhumiField("Owner Name", ownerName, { ownerName = it }) }
                item { BhumiField("Owner Phone", ownerPhone, { ownerPhone = it }) }
                item { BhumiField("Image URL", imageUrl, { imageUrl = it }, "https://...") }
            }
        },
        confirmButton = {
            BhumiPrimaryButton(
                text = "Add Listing",
                onClick = {
                    if (title.isNotBlank() && address.isNotBlank() && price.isNotBlank()) {
                        onAdd(House(
                            title = title,
                            address = address,
                            city = city,
                            type = type,
                            price = price,
                            bedrooms = bedrooms.toIntOrNull() ?: 1,
                            area = area,
                            ownerName = ownerName,
                            ownerPhone = ownerPhone,
                            imageUrl = imageUrl,
                            isAvailable = true
                        ))
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", style = BhumiType.titleMd.copy(color = BhumiColors.TextSecondary))
            }
        }
    )
}

// ── Bookings Tab ──────────────────────────────────────────────────────────────

@Composable
fun AdminBookingsTab(bookingViewModel: BookingViewModel) {
    val ctx = LocalContext.current
    val bookings = bookingViewModel.allBookings
    var filter by remember { mutableStateOf("All") }

    val filtered = if (filter == "All") bookings
    else bookings.filter { it.status == filter }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("All", "Pending", "Confirmed", "Rejected").forEach { f ->
                val selected = filter == f
                Surface(
                    onClick = { filter = f },
                    shape = RoundedCornerShape(10.dp),
                    color = if (selected) BhumiColors.Primary else BhumiColors.Surface2
                ) {
                    Text(
                        f,
                        style = BhumiType.label.copy(color = if (selected) Color.White else BhumiColors.TextSecondary),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                    )
                }
            }
        }

        if (filtered.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No $filter bookings", style = BhumiType.bodyMd.copy(color = BhumiColors.TextMuted))
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 22.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filtered) { booking ->
                    AdminBookingCard(
                        booking = booking,
                        onConfirm = {
                            bookingViewModel.updateStatus(booking.bookingId, "Confirmed") { _, msg ->
                                Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
                            }
                        },
                        onReject = {
                            bookingViewModel.updateStatus(booking.bookingId, "Rejected") { _, msg ->
                                Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AdminBookingCard(booking: Booking, onConfirm: () -> Unit, onReject: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), color = BhumiColors.White, shadowElevation = 1.dp) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(booking.houseTitle, style = BhumiType.titleLg.copy(color = BhumiColors.TextPrimary), modifier = Modifier.weight(1f))
                StatusBadge(booking.status)
            }
            BookingDetailRow("Tenant", booking.tenantName)
            BookingDetailRow("Phone", booking.tenantPhone)
            BookingDetailRow("Move-in", booking.moveInDate)
            BookingDetailRow("Duration", booking.duration)
            BookingDetailRow("Price", booking.housePrice)
            if (booking.status == "Pending") {
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BhumiColors.Green),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) { Text("Confirm", style = BhumiType.label.copy(color = Color.White)) }
                    OutlinedButton(
                        onClick = onReject,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BhumiColors.Red)
                    ) { Text("Reject", style = BhumiType.label.copy(color = BhumiColors.Red)) }
                }
            }
        }
    }
}

// ── Account Tab ───────────────────────────────────────────────────────────────

@Composable
fun AdminAccountTab() {
    val ctx = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    Column(
        modifier = Modifier.fillMaxSize().padding(22.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), color = BhumiColors.White, shadowElevation = 1.dp) {
            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.size(72.dp).background(BhumiColors.PrimaryDim, androidx.compose.foundation.shape.CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("A", style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, color = BhumiColors.Primary))
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text("Admin", style = BhumiType.headingMd.copy(color = BhumiColors.TextPrimary))
                Text(user?.email ?: "admin@bhumisewa.com", style = BhumiType.bodyMd.copy(color = BhumiColors.TextSecondary))
                Spacer(modifier = Modifier.height(8.dp))
                Surface(shape = RoundedCornerShape(8.dp), color = BhumiColors.PrimaryDim) {
                    Text("Administrator", style = BhumiType.label.copy(color = BhumiColors.Primary), modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp))
                }
            }
        }

        Button(
            onClick = {
                auth.signOut()
                ctx.startActivity(android.content.Intent(ctx, LoginActivity::class.java).apply {
                    flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
            },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BhumiColors.RedDim),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            Text("Log Out", style = BhumiType.titleMd.copy(color = BhumiColors.Red, fontWeight = FontWeight.Bold))
        }
    }
}