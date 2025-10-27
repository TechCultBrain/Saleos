package org.techcult.scaleos.core.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import scaleos.composeapp.generated.resources.Res
import scaleos.composeapp.generated.resources.inter
import scaleos.composeapp.generated.resources.inter_medium
import scaleos.composeapp.generated.resources.inter_semibold


// Default Material 3 typography values

val Inter
    @Composable get() = FontFamily(
        Font(
            resource = Res.font.inter,
            weight = FontWeight.Normal
        ),
        Font(
            resource = Res.font.inter_medium,
            weight = FontWeight.Medium
        ),
        Font(
            resource = Res.font.inter_semibold,
            weight = FontWeight.SemiBold
        )
    )

val baseline = Typography()

val AppTypography
        : Typography
    @Composable get() = Typography(
        displayLarge = baseline.displayLarge.copy(fontFamily = Inter),
        displayMedium = baseline.displayMedium.copy(fontFamily = Inter),
        displaySmall = baseline.displaySmall.copy(fontFamily = Inter),
        headlineLarge = baseline.headlineLarge.copy(fontFamily = Inter),
        headlineMedium = baseline.headlineMedium.copy(fontFamily = Inter),
        headlineSmall = baseline.headlineSmall.copy(fontFamily = Inter),
        titleLarge = baseline.titleLarge.copy(fontFamily = Inter),
        titleMedium = baseline.titleMedium.copy(fontFamily = Inter),
        titleSmall = baseline.titleSmall.copy(fontFamily = Inter),
        bodyLarge = baseline.bodyLarge.copy(fontFamily = Inter),
        bodyMedium = baseline.bodyMedium.copy(fontFamily = Inter),
        bodySmall = baseline.bodySmall.copy(fontFamily = Inter),
        labelLarge = baseline.labelLarge.copy(fontFamily = Inter),
        labelMedium = baseline.labelMedium.copy(fontFamily = Inter),
        labelSmall = baseline.labelSmall.copy(fontFamily = Inter),
    )
