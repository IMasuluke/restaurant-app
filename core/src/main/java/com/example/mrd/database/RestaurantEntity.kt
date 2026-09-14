package com.example.mrd.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mrd.domain.MenuCategory
import com.example.mrd.domain.Restaurant

@Entity(tableName = "restaurants")
data class RestaurantEntity(
    @PrimaryKey val id: String,
    val name: String,
    val cuisines: List<String>,
    val rating: Double,
    val deliveryFeeCents: Int,
    val etaMinutes: Int,
    val isOpen: Boolean,
    val imageUrl: String?,
    val menu: List<MenuCategory>
)

fun Restaurant.toEntity(): RestaurantEntity {
    return RestaurantEntity(
        id = id,
        name = name,
        cuisines = cuisines,
        rating = rating,
        deliveryFeeCents = deliveryFeeCents,
        etaMinutes = etaMinutes,
        isOpen = isOpen,
        imageUrl = imageUrl,
        menu = menu
    )
}

fun RestaurantEntity.toDomain(): Restaurant {
    return Restaurant(
        id = id,
        name = name,
        cuisines = cuisines,
        rating = rating,
        deliveryFeeCents = deliveryFeeCents,
        etaMinutes = etaMinutes,
        isOpen = isOpen,
        imageUrl = imageUrl,
        menu = menu
    )
}
