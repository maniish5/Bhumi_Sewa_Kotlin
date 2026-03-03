package com.example.bhumisewa.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.bhumisewa.model.Booking
import com.example.bhumisewa.ui.theme.BhumiColors
import com.example.bhumisewa.ui.theme.BhumiType
import com.example.bhumisewa.viewmodel.BookingViewModel

@Composable
fun MyBookingsScreen(
    padding: PaddingValues = PaddingValues(),
    bookingViewModel: BookingViewModel = viewModel()
) {
    val ctx = LocalContext.current
    val bookings = bookingViewModel.userBookings

    LaunchedEffect(Unit) { bookingViewModel.loadUserBookings() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BhumiColors.Background)
            .padding(padding)
    ) {
        BhumiScreenHeader(title = "My Bookings", subtitle = "${bookings.size} requests")

        if (bookingViewModel.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = BhumiColors.Primary)
            }
        } else if (bookings.isEmpty()) {
            BhumiEmptyState("📋", "No bookings yet", "Book a property to see your requests here")
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 22.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(bookings) { booking ->
                    BookingCard(
                        booking = booking,
                        onCancel = {
                            bookingViewModel.updateStatus(booking.bookingId, "Cancelled") { _, msg ->
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
fun BookingCard(booking: Booking, onCancel: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = BhumiColors.White,
        shadowElevation = 1.dp
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(120.dp)) {
                if (booking.houseImageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = booking.houseImageUrl,
                        contentDescription = booking.houseTitle,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize().background(BhumiColors.PrimaryDim),
                        contentAlignment = Alignment.Center
                    ) { Text("🏠", style = TextStyle(fontSize = 36.sp)) }
                }
                Box(
                    modifier = Modifier.fillMaxSize().background(
                        Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(0.5f)), startY = 40f)
                    )
                )
                StatusBadge(booking.status)
                    .let { /* placed below */ }
                Row(
                    modifier = Modifier.fillMaxWidth().align(Alignment.TopEnd).padding(10.dp),
                    horizontalArrangement = Arrangement.End
                ) { StatusBadge(booking.status) }
                Text(
                    booking.houseTitle,
                    style = BhumiType.headingMd.copy(color = Color.White),
                    modifier = Modifier.align(Alignment.BottomStart).padding(12.dp)
                )
            }
            Column(modifier = Modifier.padding(14.dp)) {
                BookingDetailRow("Address", booking.houseAddress)
                BookingDetailRow("Move-in Date", booking.moveInDate)
                BookingDetailRow("Duration", booking.duration)
                BookingDetailRow("Price", booking.housePrice)
                if (booking.message.isNotBlank()) {
                    BookingDetailRow("Message", booking.message)
                }
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = BhumiColors.Divider)
                Spacer(modifier = Modifier.height(10.dp))
                if (booking.status == "Pending") {
                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = BhumiColors.Red),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BhumiColors.Red)
                    ) {
                        Text("Cancel Request", style = BhumiType.label.copy(color = BhumiColors.Red))
                    }
                }
            }
        }
    }
}

@Composable
fun BookingDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = BhumiType.bodyMd.copy(color = BhumiColors.TextSecondary))
        Text(value, style = BhumiType.titleMd.copy(color = BhumiColors.TextPrimary))
    }
}