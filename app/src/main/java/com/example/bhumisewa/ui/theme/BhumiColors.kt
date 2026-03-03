package com.example.bhumisewa.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object BhumiColors {
    // Backgrounds — warm cream/paper tones
    val Background   = Color(0xFFF5F0E8)
    val Surface      = Color(0xFFFBF8F2)
    val Surface2     = Color(0xFFEFE9DC)
    val Surface3     = Color(0xFFE5DDD0)
    val White        = Color(0xFFFFFDF9)

    // Primary — deep terracotta/clay
    val Primary      = Color(0xFF8B3A2A)
    val PrimaryLight = Color(0xFFAD5040)
    val PrimaryDark  = Color(0xFF6B2A1C)
    val PrimaryDim   = Color(0xFFF4E0DA)
    val PrimaryBorder= Color(0xFFD9A090)

    // Accent — saffron/turmeric gold
    val Accent       = Color(0xFFD4870A)
    val AccentDim    = Color(0xFFFFF3DC)
    val AccentDark   = Color(0xFFAA6A00)

    // Highlight — forest sage
    val Sage         = Color(0xFF4A6741)
    val SageDim      = Color(0xFFDEEADC)

    // Text
    val TextPrimary   = Color(0xFF1C1008)
    val TextSecondary = Color(0xFF5C4A38)
    val TextMuted     = Color(0xFF9A8070)
    val TextHint      = Color(0xFFBBAA98)

    // Status
    val Green         = Color(0xFF2E6B35)
    val GreenDim      = Color(0xFFDCEEDE)
    val Red           = Color(0xFF8B1A1A)
    val RedDim        = Color(0xFFF5DCDC)
    val Yellow        = Color(0xFFB07800)
    val YellowDim     = Color(0xFFFFF0C8)
    val Blue          = Color(0xFF1E4D7A)
    val BlueDim       = Color(0xFFD8E8F5)

    // Divider
    val Divider       = Color(0xFFDDD5C8)
    val DividerLight  = Color(0xFFEDE8E0)

    // Star
    val Star          = Color(0xFFD4870A)
}

object BhumiType {
    val displayXL  = TextStyle(fontSize = 38.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = (-1.2).sp, lineHeight = 44.sp)
    val displayLg  = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = (-0.8).sp, lineHeight = 36.sp)
    val headingLg  = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.3).sp)
    val headingMd  = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.2).sp)
    val titleLg    = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    val titleMd    = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    val bodyLg     = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Normal, lineHeight = 24.sp)
    val bodyMd     = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Normal, lineHeight = 20.sp)
    val label      = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.3.sp)
    val labelSm    = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.5.sp)
    val price      = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = (-0.5).sp)
}