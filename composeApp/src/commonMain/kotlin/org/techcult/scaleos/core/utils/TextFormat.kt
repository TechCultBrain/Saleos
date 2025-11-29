package org.techcult.scaleos.core.utils

fun String.toCapitalizeFormat(): String =
    this.uppercase().first() + this.substring(1).lowercase().toString()
