package com.example.mrd.utils

import kotlin.math.roundToInt

fun Int.asPrice(): String {
    if (this == 0) return "Free"
    val rand = this / 100.0
    return "R${rand.roundToInt()}"
}

fun Double.asRating(): String = String.format("%.1f", this)
