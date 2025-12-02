package org.techcult.scaleos.core.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

actual fun formatCurrency(amount: String): String {
    val locale = Locale.Builder()
        .setLanguage("en")
        .setRegion("IN")
        .build()
    val numberFormat = NumberFormat.getNumberInstance(locale) as DecimalFormat
    numberFormat.applyPattern("#,##0.00") // Example pattern for two decimal places and thousands separator

    // Parse the clean text and format it
    return try {
        val number = amount.toBigDecimal().movePointLeft(2) // Assuming input is in cents/smallest unit
        numberFormat.format(number)
    } catch (e: NumberFormatException) {
        amount // If parsing fails, return original text
    }
}