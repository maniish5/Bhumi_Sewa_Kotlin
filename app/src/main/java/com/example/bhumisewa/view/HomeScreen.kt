package com.example.bhumisewa.view

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.bhumisewa.HouseDetailActivity
import com.example.bhumisewa.model.House
import com.example.bhumisewa.ui.theme.BhumiColors
import com.example.bhumisewa.ui.theme.BhumiType
import com.example.bhumisewa.utils.MockData
import com.example.bhumisewa.utils.SavedManager
import com.example.bhumisewa.viewmodel.HouseViewModel

@Composable
fun HomeScreen(padding: PaddingValues = PaddingValues(), userName: String = "Guest", houseViewModel: HouseViewModel = viewModel()) {
    val ctx = LocalContext.current
    val houses = houseViewModel.houses.ifEmpty { MockData.houses }
    LaunchedEffect(Unit) { houseViewModel.loadHouses() }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(BhumiColors.Background).padding(padding),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // ── Hero Header ───────────────────────────────────────────────────────
        item {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(BhumiColors.PrimaryDim, BhumiColors.White)))
                    .padding(start = 22.dp, end = 22.dp, top = 52.dp, bottom = 28.dp)
            ) {
                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                        Column {
                            Text("नमस्ते, ${userName.substringBefore(" ")} 🙏", style = BhumiType.bodyMd.copy(color = BhumiColors.TextSecondary))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Find Your\nDream Home", style = BhumiType.displayLg.copy(color = BhumiColors.TextPrimary, lineHeight = 36.sp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("with BhumiSewa", style = BhumiType.bodyMd.copy(color = BhumiColors.Accent))
                        }
                        Box(
                            modifier = Modifier.size(50.dp).background(BhumiColors.Primary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(userName.take(1).uppercase(), style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White))
                        }
                    }
                    Spacer(modifier = Modifier.height(22.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        HeroStatCard("🏘️", "${houses.size}", "Listings", Modifier.weight(1f))
                        HeroStatCard("🏙️", "${houses.map { it.city }.distinct().size}", "Cities", Modifier.weight(1f))
                        HeroStatCard("✅", "${houses.count { it.isAvailable }}", "Available", Modifier.weight(1f))
                    }
                }
            }
        }

        // ── Browse by Type ───────────────────────────────────────────────────
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Browse by Type", style = BhumiType.headingMd.copy(color = BhumiColors.TextPrimary))
            }
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(contentPadding = PaddingValues(horizontal = 22.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                val cats = listOf(Triple("🏢","Apartment",houses.count{it.type=="Apartment"}), Triple("🏡","House",houses.count{it.type=="House"}), Triple("🏨","Studio",houses.count{it.type=="Studio"}), Triple("🏰","Villa",houses.count{it.type=="Villa"}))
                items(cats) { (icon, label, count) -> TypeCard(icon, label, count) }
            }
        }

        // ── Featured ─────────────────────────────────────────────────────────
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Featured Properties", style = BhumiType.headingMd.copy(color = BhumiColors.TextPrimary))
                    Text("Handpicked for you", style = BhumiType.bodyMd.copy(color = BhumiColors.TextMuted))
                }
                Text("See all →", style = BhumiType.label.copy(color = BhumiColors.Primary))
            }
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(contentPadding = PaddingValues(horizontal = 22.dp), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                items(houses.filter { it.isFeatured }) { house ->
                    FeaturedCard(house = house, onClick = {
                        ctx.startActivity(Intent(ctx, HouseDetailActivity::class.java).apply { putExtra("houseId", house.houseId) })
                    })
                }
            }
        }

        // ── All Listings ─────────────────────────────────────────────────────
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("All Listings", style = BhumiType.headingMd.copy(color = BhumiColors.TextPrimary))
                    Text("${houses.size} properties found", style = BhumiType.bodyMd.copy(color = BhumiColors.TextMuted))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(houses) { house ->
            Box(modifier = Modifier.padding(horizontal = 22.dp, vertical = 6.dp)) {
                ListingCard(house = house, onClick = {
                    ctx.startActivity(Intent(ctx, HouseDetailActivity::class.java).apply { putExtra("houseId", house.houseId) })
                })
            }
        }
    }
}

@Composable
fun HeroStatCard(icon: String, value: String, label: String, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, shape = RoundedCornerShape(14.dp), color = BhumiColors.White, shadowElevation = 1.dp) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(icon, style = TextStyle(fontSize = 22.sp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, style = BhumiType.titleLg.copy(color = BhumiColors.Primary))
            Text(label, style = BhumiType.labelSm.copy(color = BhumiColors.TextMuted))
        }
    }
}

@Composable
fun TypeCard(icon: String, label: String, count: Int) {
    Surface(shape = RoundedCornerShape(16.dp), color = BhumiColors.White, shadowElevation = 1.dp) {
        Column(modifier = Modifier.padding(horizontal = 22.dp, vertical = 14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(52.dp).background(BhumiColors.PrimaryDim, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                Text(icon, style = TextStyle(fontSize = 24.sp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, style = BhumiType.titleMd.copy(color = BhumiColors.TextPrimary))
            Text("$count listed", style = BhumiType.labelSm.copy(color = BhumiColors.TextMuted))
        }
    }
}

@Composable
fun FeaturedCard(house: House, onClick: () -> Unit) {
    Surface(onClick = onClick, modifier = Modifier.width(270.dp), shape = RoundedCornerShape(20.dp), color = BhumiColors.White, shadowElevation = 3.dp) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(165.dp)) {
                AsyncImage(model = house.imageUrl, contentDescription = house.title, contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)))
                // Gradient overlay
                Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent, BhumiColors.Primary.copy(0.7f)), startY = 60f)))
                // Featured badge
                Surface(modifier = Modifier.align(Alignment.TopStart).padding(10.dp), shape = RoundedCornerShape(8.dp), color = BhumiColors.Accent) {
                    Text("✦ Featured", style = BhumiType.labelSm.copy(color = Color.White), modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
                // Heart
                val isSaved = SavedManager.isSaved(house.houseId)
                Surface(modifier = Modifier.align(Alignment.TopEnd).padding(10.dp).size(32.dp), shape = CircleShape, color = Color.White.copy(0.92f)) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.clickable { SavedManager.toggle(house) }) {
                        Text(if (isSaved) "❤️" else "🤍", style = TextStyle(fontSize = 14.sp))
                    }
                }
                // Price
                Column(modifier = Modifier.align(Alignment.BottomStart).padding(12.dp)) {
                    Text(house.price, style = BhumiType.titleLg.copy(color = Color.White))
                }
            }
            Column(modifier = Modifier.padding(14.dp)) {
                Text(house.title, style = BhumiType.titleLg.copy(color = BhumiColors.TextPrimary), maxLines = 1)
                Spacer(modifier = Modifier.height(4.dp))
                Text("📍 ${house.address}, ${house.city}", style = BhumiType.bodyMd.copy(color = BhumiColors.TextSecondary), maxLines = 1)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    BhumiTag("${house.bedrooms} BD")
                    BhumiTag(house.type, accent = true)
                }
            }
        }
    }
}

@Composable
fun ListingCard(house: House, onClick: () -> Unit) {
    Surface(onClick = onClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), color = BhumiColors.White, shadowElevation = 1.dp) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(100.dp, 86.dp)) {
                AsyncImage(model = house.imageUrl, contentDescription = house.title, contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp)))
                if (house.isFeatured) {
                    Surface(modifier = Modifier.align(Alignment.TopStart).padding(4.dp), shape = RoundedCornerShape(4.dp), color = BhumiColors.Accent) {
                        Text("★", style = TextStyle(fontSize = 9.sp, color = Color.White), modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(house.type, style = BhumiType.labelSm.copy(color = BhumiColors.Accent, letterSpacing = 0.8.sp))
                Text(house.title, style = BhumiType.titleMd.copy(color = BhumiColors.TextPrimary), maxLines = 1)
                Text("📍 ${house.city}", style = BhumiType.bodyMd.copy(color = BhumiColors.TextSecondary))
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(house.price, style = BhumiType.titleMd.copy(color = BhumiColors.Primary))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text("★", style = TextStyle(fontSize = 12.sp, color = BhumiColors.Star))
                        Text("${house.rating}", style = BhumiType.labelSm.copy(color = BhumiColors.TextSecondary))
                    }
                }
            }
        }
    }
}