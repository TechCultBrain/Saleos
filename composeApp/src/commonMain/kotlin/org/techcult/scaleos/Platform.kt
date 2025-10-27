package org.techcult.scaleos

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform