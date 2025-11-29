package org.techcult.scaleos.core.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

val customFormat = LocalDateTime.Format {
    monthName(MonthNames.ENGLISH_ABBREVIATED); char(' '); day(); chars(", "); year()
}

fun LocalDateTime.toFormattedString(): String=
    this.format(customFormat)
