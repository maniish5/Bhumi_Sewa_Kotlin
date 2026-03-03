package com.example.bhumisewa.view

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bhumisewa.HouseDetailActivity
import com.example.bhumisewa.ui.theme.BhumiColors
import com.example.bhumisewa.ui.theme.BhumiType
import com.example.bhumisewa.utils.MockData
import com.example.bhumisewa.viewmodel.HouseViewModel

@Composable
fun SearchScreen(
    padding: PaddingValues = PaddingValues(),
    houseViewModel: HouseViewModel = viewModel()
) {
    val ctx = LocalContext.current
    var query by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("All") }
    var selectedCity by remember { mutableStateOf("All") }

    val allHouses = houseViewModel.houses.ifEmpty { MockData.houses }
    val types = listOf("All", "Apartment", "House", "Studio", "Villa")
    val cities = listOf("All") + allHouses.map { it.city }.distinct()

    val filtered = allHouses.filter { house ->
        val matchesQuery = query.isBlank() ||
                house.title.contains(query, ignoreCase = true) ||
                house.address.contains(query, ignoreCase = true) ||
                house.city.contains(query, ignoreCase = true) ||
                house.type.contains(query, ignoreCase = true)
        val matchesType = selectedType == "All" || house.type == selectedType
        val matchesCity = selectedCity == "All" || house.city == selectedCity
        matchesQuery && matchesType && matchesCity
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BhumiColors.Background)
            .padding(padding)
    ) {
        // Header + Search bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BhumiColors.White)
                .padding(start = 22.dp, end = 22.dp, top = 52.dp, bottom = 16.dp)
        ) {
            Text("Search", style = BhumiType.displayLg.copy(color = BhumiColors.TextPrimary))
            Spacer(modifier = Modifier.height(14.dp))
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search by name, city, area...", style = BhumiType.bodyMd.copy(color = BhumiColors.TextHint)) },
                leadingIcon = { Text("🔍", style = androidx.compose.ui.text.TextStyle(fontSize = 16.dp.value.sp)) },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BhumiColors.Primary,
                    unfocusedBorderColor = BhumiColors.Divider,
                    focusedContainerColor = BhumiColors.White,
                    unfocusedContainerColor = BhumiColors.Surface2,
                    focusedTextColor = BhumiColors.TextPrimary,
                    unfocusedTextColor = BhumiColors.TextPrimary
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))
            // Type filter
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(types) { type ->
                    val selected = selectedType == type
                    Surface(
                        onClick = { selectedType = type },
                        shape = RoundedCornerShape(10.dp),
                        color = if (selected) BhumiColors.Primary else BhumiColors.Surface2
                    ) {
                        Text(
                            text = type,
                            style = BhumiType.label.copy(color = if (selected) androidx.compose.ui.graphics.Color.White else BhumiColors.TextSecondary),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            // City filter
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(cities) { city ->
                    val selected = selectedCity == city
                    Surface(
                        onClick = { selectedCity = city },
                        shape = RoundedCornerShape(10.dp),
                        color = if (selected) BhumiColors.PrimaryDim else BhumiColors.Surface2
                    ) {
                        Text(
                            text = city,
                            style = BhumiType.label.copy(color = if (selected) BhumiColors.Primary else BhumiColors.TextSecondary),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (filtered.isEmpty()) {
            BhumiEmptyState("🔍", "No results found", "Try a different search term or filter")
        } else {
            Text(
                "${filtered.size} listing${if (filtered.size != 1) "s" else ""} found",
                style = BhumiType.bodyMd.copy(color = BhumiColors.TextSecondary),
                modifier = Modifier.padding(horizontal = 22.dp, vertical = 10.dp)
            )
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 22.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filtered) { house ->
                    ListingCard(house = house, onClick = {
                        ctx.startActivity(Intent(ctx, HouseDetailActivity::class.java).apply {
                            putExtra("houseId", house.houseId)
                        })
                    })
                }
            }
        }
    }
}