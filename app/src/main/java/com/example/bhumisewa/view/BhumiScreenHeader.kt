package com.example.bhumisewa.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bhumisewa.ui.theme.BhumiColors
import com.example.bhumisewa.ui.theme.BhumiType

@Composable
fun BhumiScreenHeader(title: String, subtitle: String? = null) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BhumiColors.White)
            .padding(start = 22.dp, end = 22.dp, top = 52.dp, bottom = 20.dp)
    ) {
        if (subtitle != null) {
            Text(
                text = subtitle.uppercase(),
                style = BhumiType.labelSm.copy(color = BhumiColors.Accent, letterSpacing = 1.8.sp)
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
        Text(title, style = BhumiType.displayLg.copy(color = BhumiColors.TextPrimary))
        Spacer(modifier = Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Box(modifier = Modifier.width(32.dp).height(3.dp).background(BhumiColors.Primary, RoundedCornerShape(2.dp)))
            Box(modifier = Modifier.width(12.dp).height(3.dp).background(BhumiColors.Accent, RoundedCornerShape(2.dp)))
        }
    }
}

@Composable
fun BhumiEmptyState(icon: String, title: String, subtitle: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(icon, style = TextStyle(fontSize = 52.sp))
            Text(title, style = BhumiType.headingMd.copy(color = BhumiColors.TextPrimary))
            Text(subtitle, style = BhumiType.bodyMd.copy(color = BhumiColors.TextSecondary), textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun BhumiField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    icon: String = "",
    singleLine: Boolean = true
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            if (icon.isNotEmpty()) Text(icon, style = TextStyle(fontSize = 14.sp))
            Text(label, style = BhumiType.label.copy(color = BhumiColors.TextSecondary))
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, style = BhumiType.bodyMd.copy(color = BhumiColors.TextHint)) },
            singleLine = singleLine,
            maxLines = if (singleLine) 1 else 4,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BhumiColors.Primary,
                unfocusedBorderColor = BhumiColors.Divider,
                focusedContainerColor = BhumiColors.White,
                unfocusedContainerColor = BhumiColors.Surface2,
                focusedTextColor = BhumiColors.TextPrimary,
                unfocusedTextColor = BhumiColors.TextPrimary,
                cursorColor = BhumiColors.Primary
            )
        )
    }
}

@Composable
fun BhumiTag(text: String, accent: Boolean = false) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = if (accent) BhumiColors.AccentDim else BhumiColors.PrimaryDim
    ) {
        Text(
            text = text,
            style = BhumiType.labelSm.copy(color = if (accent) BhumiColors.AccentDark else BhumiColors.Primary),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun BhumiPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(54.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = BhumiColors.Primary),
        enabled = enabled && !isLoading,
        elevation = ButtonDefaults.buttonElevation(0.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
        } else {
            Text(text, style = BhumiType.label.copy(color = Color.White, letterSpacing = 1.sp))
        }
    }
}

@Composable
fun StarRating(rating: Float, size: Int = 14) {
    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        repeat(5) { i ->
            Text(
                text = if (i < rating.toInt()) "★" else if (i < rating) "½" else "☆",
                style = TextStyle(fontSize = size.sp, color = BhumiColors.Star)
            )
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val (bg, textColor) = when (status) {
        "Confirmed" -> BhumiColors.GreenDim  to BhumiColors.Green
        "Rejected"  -> BhumiColors.RedDim    to BhumiColors.Red
        "Cancelled" -> BhumiColors.RedDim    to BhumiColors.Red
        else        -> BhumiColors.YellowDim to BhumiColors.Yellow
    }
    Surface(shape = RoundedCornerShape(8.dp), color = bg) {
        Text(
            text = status,
            style = BhumiType.labelSm.copy(color = textColor),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}