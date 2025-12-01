package org.techcult.scaleos.core.utils

import androidx.compose.ui.text.intl.Locale

actual fun formatDouble(value: Double?): String {
    return String.format(Locale.current.platformLocale.country, value)
}

