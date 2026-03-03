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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bhumisewa.model.Booking
import com.example.bhumisewa.model.Review
import com.example.bhumisewa.ui.theme.BhumiColors
import com.example.bhumisewa.ui.theme.BhumiType
import com.example.bhumisewa.utils.MockData
import com.example.bhumisewa.utils.SavedManager
import com.example.bhumisewa.view.BhumiPrimaryButton
import com.example.bhumisewa.view.BhumiTag
import com.example.bhumisewa.view.StarRating
import com.example.bhumisewa.view.StatusBadge
import com.example.bhumisewa.view.BhumiField
import com.example.bhumisewa.viewmodel.BookingViewModel
import com.example.bhumisewa.viewmodel.ReviewViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class HouseDetailActivity : ComponentActivity() {
    private val bookingViewModel: BookingViewModel by viewModels()
    private val reviewViewModel: ReviewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val houseId = intent.getStringExtra("houseId") ?: ""
        setContent {
            HouseDetailScreen(
                houseId = houseId,
                bookingViewModel = bookingViewModel,
                reviewViewModel = reviewViewModel,
                onBack = { finish() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HouseDetailScreen(
    houseId: String,
    bookingViewModel: BookingViewModel,
    reviewViewModel: ReviewViewModel,
    onBack: () -> Unit
) {
    val house = MockData.houses.find { it.houseId == houseId } ?: return
    val ctx = LocalContext.current
    var showBookingSheet by remember { mutableStateOf(false) }
    var showReviewSheet by remember { mutableStateOf(false) }
    val isSaved = SavedManager.isSaved(houseId)

    LaunchedEffect(houseId) { reviewViewModel.loadReviews(houseId) }

    Box(modifier = Modifier.fillMaxSize().background(BhumiColors.Background)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Hero Image
            item {
                Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                    AsyncImage(
                        model = house.imageUrl,
                        contentDescription = house.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(modifier = Modifier.fillMaxSize().background(
                        Brush.verticalGradient(listOf(Color.Black.copy(0.3f), Color.Transparent, Color.Black.copy(0.2f)))
                    ))
                    // Back button
                    Surface(
                        onClick = onBack,
                        modifier = Modifier.padding(top = 52.dp, start = 16.dp).size(40.dp),
                        shape = CircleShape,
                        color = Color.White.copy(0.9f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("←", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BhumiColors.TextPrimary))
                        }
                    }
                    // Save button
                    Surface(
                        onClick = { SavedManager.toggle(house) },
                        modifier = Modifier.align(Alignment.TopEnd).padding(top = 52.dp, end = 16.dp).size(40.dp),
                        shape = CircleShape,
                        color = Color.White.copy(0.9f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(if (isSaved) "❤️" else "🤍", style = TextStyle(fontSize = 16.sp))
                        }
                    }
                    // Available badge
                    Surface(
                        modifier = Modifier.align(Alignment.BottomStart).padding(16.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = if (house.isAvailable) BhumiColors.Green else BhumiColors.Red
                    ) {
                        Text(
                            if (house.isAvailable) "● Available" else "● Not Available",
                            style = BhumiType.label.copy(color = Color.White),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            // Main info
            item {
                Column(modifier = Modifier.fillMaxWidth().background(BhumiColors.White).padding(22.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            BhumiTag(house.type)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(house.title, style = BhumiType.headingLg.copy(color = BhumiColors.TextPrimary))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("📍 ${house.address}, ${house.city}", style = BhumiType.bodyMd.copy(color = BhumiColors.TextSecondary))
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(house.price, style = BhumiType.price.copy(color = BhumiColors.Primary))
                            Text("per month", style = BhumiType.labelSm.copy(color = BhumiColors.TextMuted))
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        StarRating(house.rating)
                        Text("${house.rating}", style = BhumiType.titleMd.copy(color = BhumiColors.TextPrimary))
                        Text("(${house.reviewCount} reviews)", style = BhumiType.bodyMd.copy(color = BhumiColors.TextMuted))
                    }
                }
            }

            // Specs grid
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Column(modifier = Modifier.fillMaxWidth().background(BhumiColors.White).padding(22.dp)) {
                    Text("Property Details", style = BhumiType.headingMd.copy(color = BhumiColors.TextPrimary))
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        SpecCard("🛏️", "Bedrooms", "${house.bedrooms}", Modifier.weight(1f))
                        SpecCard("🚿", "Bathrooms", "${house.bathrooms}", Modifier.weight(1f))
                        SpecCard("📐", "Area", house.area, Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        SpecCard("🏗️", "Floor", house.floor, Modifier.weight(1f))
                        SpecCard("🛋️", "Furnished", house.furnished, Modifier.weight(1f))
                        SpecCard("📅", "Available", house.availableFrom, Modifier.weight(1f))
                    }
                }
            }

            // Description
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Column(modifier = Modifier.fillMaxWidth().background(BhumiColors.White).padding(22.dp)) {
                    Text("Description", style = BhumiType.headingMd.copy(color = BhumiColors.TextPrimary))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(house.description, style = BhumiType.bodyLg.copy(color = BhumiColors.TextSecondary))
                }
            }

            // Amenities
            if (house.amenities.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(modifier = Modifier.fillMaxWidth().background(BhumiColors.White).padding(22.dp)) {
                        Text("Amenities", style = BhumiType.headingMd.copy(color = BhumiColors.TextPrimary))
                        Spacer(modifier = Modifier.height(12.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            house.amenities.forEach { amenity -> AmenityChip(amenity) }
                        }
                    }
                }
            }

            // Owner info
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Column(modifier = Modifier.fillMaxWidth().background(BhumiColors.White).padding(22.dp)) {
                    Text("Owner / Contact", style = BhumiType.headingMd.copy(color = BhumiColors.TextPrimary))
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(48.dp).background(BhumiColors.PrimaryDim, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(house.ownerName.take(1), style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = BhumiColors.Primary))
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(house.ownerName, style = BhumiType.titleLg.copy(color = BhumiColors.TextPrimary))
                            Text(house.ownerPhone, style = BhumiType.bodyMd.copy(color = BhumiColors.TextSecondary))
                            Text(house.ownerEmail, style = BhumiType.bodyMd.copy(color = BhumiColors.Primary))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("💰 Deposit: ${house.depositAmount}", style = BhumiType.bodyMd.copy(color = BhumiColors.TextSecondary))
                }
            }

            // Reviews
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Column(modifier = Modifier.fillMaxWidth().background(BhumiColors.White).padding(22.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Reviews", style = BhumiType.headingMd.copy(color = BhumiColors.TextPrimary))
                        TextButton(onClick = { showReviewSheet = true }) {
                            Text("+ Add Review", style = BhumiType.label.copy(color = BhumiColors.Primary))
                        }
                    }
                    if (reviewViewModel.reviews.isEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No reviews yet. Be the first!", style = BhumiType.bodyMd.copy(color = BhumiColors.TextMuted))
                    }
                }
            }

            items(reviewViewModel.reviews) { review ->
                ReviewCard(review = review, modifier = Modifier.background(BhumiColors.White).padding(horizontal = 22.dp, vertical = 8.dp))
            }
        }

        // Bottom Book button
        if (house.isAvailable) {
            Surface(
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
                color = BhumiColors.White,
                shadowElevation = 8.dp
            ) {
                BhumiPrimaryButton(
                    text = "Book This Property",
                    onClick = { showBookingSheet = true },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 16.dp).navigationBarsPadding()
                )
            }
        }
    }

    if (showBookingSheet) {
        BookingSheet(
            house = house,
            onDismiss = { showBookingSheet = false },
            onSubmit = { booking ->
                bookingViewModel.placeBooking(booking) { success, msg ->
                    Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show()
                    if (success) showBookingSheet = false
                }
            }
        )
    }

    if (showReviewSheet) {
        AddReviewSheet(
            houseId = houseId,
            onDismiss = { showReviewSheet = false },
            onSubmit = { review ->
                reviewViewModel.addReview(review) { success, msg ->
                    Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
                    if (success) showReviewSheet = false
                }
            }
        )
    }
}

@Composable
fun SpecCard(icon: String, label: String, value: String, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, shape = RoundedCornerShape(12.dp), color = BhumiColors.Surface2) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(icon, style = TextStyle(fontSize = 20.sp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, style = BhumiType.titleMd.copy(color = BhumiColors.TextPrimary), maxLines = 1)
            Text(label, style = BhumiType.labelSm.copy(color = BhumiColors.TextMuted))
        }
    }
}

@Composable
fun AmenityChip(text: String) {
    val icon = when (text) {
        "WiFi" -> "📶"; "Parking" -> "🅿️"; "Security" -> "🔒"; "Pool" -> "🏊"
        "Garden" -> "🌿"; "Gym" -> "💪"; "Rooftop" -> "🏙️"; "Lift" -> "🛗"
        "CCTV" -> "📷"; "Solar Water" -> "☀️"; "Power Backup" -> "⚡"
        "Balcony" -> "🌅"; "Smart Home" -> "🏠"; "Home Theatre" -> "🎬"
        else -> "✓"
    }
    Surface(shape = RoundedCornerShape(10.dp), color = BhumiColors.PrimaryDim) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(icon, style = TextStyle(fontSize = 12.sp))
            Text(text, style = BhumiType.label.copy(color = BhumiColors.Primary))
        }
    }
}

@Composable
fun ReviewCard(review: Review, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalDivider(color = BhumiColors.Divider)
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(36.dp).background(BhumiColors.PrimaryDim, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(review.userName.take(1).uppercase(), style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BhumiColors.Primary))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(review.userName, style = BhumiType.titleMd.copy(color = BhumiColors.TextPrimary))
                StarRating(review.rating, size = 12)
            }
        }
        if (review.comment.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(review.comment, style = BhumiType.bodyMd.copy(color = BhumiColors.TextSecondary))
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingSheet(
    house: com.example.bhumisewa.model.House,
    onDismiss: () -> Unit,
    onSubmit: (Booking) -> Unit
) {
    var tenantName by remember { mutableStateOf("") }
    var tenantPhone by remember { mutableStateOf("") }
    var tenantEmail by remember { mutableStateOf("") }
    var moveInDate by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = BhumiColors.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 40.dp)
        ) {
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Book Property", style = BhumiType.headingMd.copy(color = BhumiColors.TextPrimary))
                        Text(house.title, style = BhumiType.bodyMd.copy(color = BhumiColors.TextSecondary))
                    }
                    IconButton(onClick = onDismiss) { Text("✕", style = TextStyle(fontSize = 18.sp, color = BhumiColors.TextMuted)) }
                }
                HorizontalDivider(color = BhumiColors.Divider)
                Spacer(modifier = Modifier.height(4.dp))
            }
            item { BhumiField("Your Name", tenantName, { tenantName = it }, "Full name", "👤") }
            item { BhumiField("Phone Number", tenantPhone, { tenantPhone = it }, "+977-XXXXXXXXXX", "📞") }
            item { BhumiField("Email", tenantEmail, { tenantEmail = it }, "your@email.com", "✉️") }
            item { BhumiField("Move-in Date", moveInDate, { moveInDate = it }, "e.g. 1 March 2026", "📅") }
            item { BhumiField("Duration", duration, { duration = it }, "e.g. 6 months, 1 year", "⏱️") }
            item { BhumiField("Message (optional)", message, { message = it }, "Any special requirements...", "💬", singleLine = false) }
            item {
                BhumiPrimaryButton(
                    text = "SEND BOOKING REQUEST",
                    onClick = {
                        if (tenantName.isNotBlank() && tenantPhone.isNotBlank() && moveInDate.isNotBlank()) {
                            onSubmit(Booking(
                                houseId = house.houseId,
                                houseTitle = house.title,
                                houseAddress = house.address,
                                houseImageUrl = house.imageUrl,
                                housePrice = house.price,
                                tenantName = tenantName,
                                tenantPhone = tenantPhone,
                                tenantEmail = tenantEmail,
                                moveInDate = moveInDate,
                                duration = duration,
                                message = message
                            ))
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReviewSheet(houseId: String, onDismiss: () -> Unit, onSubmit: (Review) -> Unit) {
    var rating by remember { mutableStateOf(5f) }
    var comment by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = BhumiColors.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Write a Review", style = BhumiType.headingMd.copy(color = BhumiColors.TextPrimary))
                IconButton(onClick = onDismiss) { Text("✕", style = TextStyle(fontSize = 18.sp, color = BhumiColors.TextMuted)) }
            }
            HorizontalDivider(color = BhumiColors.Divider)
            Text("Rating", style = BhumiType.label.copy(color = BhumiColors.TextSecondary))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                (1..5).forEach { i ->
                    TextButton(onClick = { rating = i.toFloat() }, contentPadding = PaddingValues(4.dp)) {
                        Text(if (i <= rating) "★" else "☆", style = TextStyle(fontSize = 28.sp, color = if (i <= rating) BhumiColors.Star else BhumiColors.TextMuted))
                    }
                }
            }
            BhumiField("Comment", comment, { comment = it }, "Share your experience...", "💬", singleLine = false)
            BhumiPrimaryButton(
                text = "SUBMIT REVIEW",
                onClick = { onSubmit(Review(houseId = houseId, rating = rating, comment = comment)) },
                modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(bottom = 16.dp)
            )
        }
    }
}