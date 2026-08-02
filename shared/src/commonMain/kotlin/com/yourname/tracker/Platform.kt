package com.yourname.tracker

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform