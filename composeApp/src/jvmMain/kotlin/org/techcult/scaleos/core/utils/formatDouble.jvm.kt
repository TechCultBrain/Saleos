package org.techcult.scaleos.core.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

actual fun formatDouble(value: Double?): String {

    val format=DecimalFormat.getInstance(Locale.getDefault())
    format.maximumFractionDigits=2
    format.minimumFractionDigits=2
    format.roundingMode


    return format.format(value?:0)


}