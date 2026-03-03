package com.example.bhumisewa.view

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bhumisewa.HouseDetailActivity
import com.example.bhumisewa.model.SavedItem
import com.example.bhumisewa.ui.theme.BhumiColors
import com.example.bhumisewa.ui.theme.BhumiType
import com.example.bhumisewa.utils.MockData
import com.example.bhumisewa.utils.SavedManager

@Composable
fun SavedScreen(padding: PaddingValues = PaddingValues()) {
    val ctx = LocalContext.current
    val items = SavedManager.items

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BhumiColors.Background)
            .padding(padding)
    ) {
        BhumiScreenHeader(title = "Saved Homes", subtitle = "${items.size} saved")

        if (items.isEmpty()) {
            BhumiEmptyState("❤️", "Nothing saved yet", "Tap the heart on any listing to save it here")
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 22.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items) { item ->
                    SavedCard(
                        item = item,
                        onClick = {
                            ctx.startActivity(Intent(ctx, HouseDetailActivity::class.java).apply {
                                putExtra("houseId", item.houseId)
                            })
                        },
                        onRemove = {
                            val house = MockData.houses.find { it.houseId == item.houseId }
                            if (house != null) SavedManager.toggle(house)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SavedCard(item: SavedItem, onClick: () -> Unit, onRemove: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = BhumiColors.White,
        shadowElevation = 1.dp
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(90.dp, 78.dp).clip(RoundedCornerShape(12.dp))) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.type, style = BhumiType.labelSm.copy(color = BhumiColors.Primary))
                Text(item.title, style = BhumiType.titleMd.copy(color = BhumiColors.TextPrimary), maxLines = 1)
                Text("📍 ${item.city}", style = BhumiType.bodyMd.copy(color = BhumiColors.TextSecondary))
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(item.price, style = BhumiType.titleMd.copy(color = BhumiColors.Primary))
                    Text("${item.bedrooms} BD", style = BhumiType.labelSm.copy(color = BhumiColors.TextMuted))
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = onRemove, modifier = Modifier.size(36.dp)) {
                Text("🗑️", style = TextStyle(fontSize = 16.sp))
            }
        }
    }
}