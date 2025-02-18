package com.example.wanderlist.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.wanderlist.R

val Montserrat = FontFamily(
    // Thin (100)
    Font(R.font.montserrat_thin, FontWeight.Thin, FontStyle.Normal),
    Font(R.font.montserrat_thinitalic, FontWeight.Thin, FontStyle.Italic),

    // ExtraLight (200)
    Font(R.font.montserrat_extralight, FontWeight.ExtraLight, FontStyle.Normal),
    Font(R.font.montserrat_extralightitalic, FontWeight.ExtraLight, FontStyle.Italic),

    // Light (300)
    Font(R.font.montserrat_light, FontWeight.Light, FontStyle.Normal),
    Font(R.font.montserrat_lightitalic, FontWeight.Light, FontStyle.Italic),

    // Regular (400)
    Font(R.font.montserrat_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.montserrat_italic, FontWeight.Normal, FontStyle.Italic),

    // Medium (500)
    Font(R.font.montserrat_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.montserrat_mediumitalic, FontWeight.Medium, FontStyle.Italic),

    // SemiBold (600)
    Font(R.font.montserrat_semibold, FontWeight.SemiBold, FontStyle.Normal),
    Font(R.font.montserrat_semibolditalic, FontWeight.SemiBold, FontStyle.Italic),

    // Bold (700)
    Font(R.font.montserrat_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.montserrat_bolditalic, FontWeight.Bold, FontStyle.Italic),

    // ExtraBold (800)
    Font(R.font.montserrat_extrabold, FontWeight.ExtraBold, FontStyle.Normal),
    Font(R.font.montserrat_extrabolditalic, FontWeight.ExtraBold, FontStyle.Italic),

    // Black (900)
    Font(R.font.montserrat_black, FontWeight.Black, FontStyle.Normal),
    Font(R.font.montserrat_blackitalic, FontWeight.Black, FontStyle.Italic)
)

val Alef = FontFamily(
    Font(R.font.alef_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.alef_regular, FontWeight.Normal, FontStyle.Normal)
)

val Acme = FontFamily(
    Font(R.font.acme_regular, FontWeight.Normal, FontStyle.Normal)
)

val WorkSans = FontFamily(
    Font(R.font.worksans_thin, FontWeight.Thin, FontStyle.Normal),
    Font(R.font.worksans_thinitalic, FontWeight.Thin, FontStyle.Italic),
    Font(R.font.worksans_extralight, FontWeight.ExtraLight, FontStyle.Normal),
    Font(R.font.worksans_extralightitalic, FontWeight.ExtraLight, FontStyle.Italic),
    Font(R.font.worksans_light, FontWeight.Light, FontStyle.Normal),
    Font(R.font.worksans_lightitalic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.worksans_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.worksans_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.worksans_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.worksans_mediumitalic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.worksans_semibold, FontWeight.SemiBold, FontStyle.Normal),
    Font(R.font.worksans_semibolditalic, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.worksans_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.worksans_bolditalic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.worksans_extrabold, FontWeight.ExtraBold, FontStyle.Normal),
    Font(R.font.worksans_extrabolditalic, FontWeight.ExtraBold, FontStyle.Italic),
    Font(R.font.worksans_black, FontWeight.Black, FontStyle.Normal),
    Font(R.font.worksans_blackitalic, FontWeight.Black, FontStyle.Italic)
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)