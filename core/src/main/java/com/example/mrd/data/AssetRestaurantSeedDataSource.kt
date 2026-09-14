package com.example.mrd.data

import android.content.Context
import com.example.mrd.domain.MenuCategory
import com.example.mrd.domain.MenuItem
import com.example.mrd.domain.MenuOption
import com.example.mrd.domain.Restaurant
import org.json.JSONArray
import org.json.JSONObject

class AssetRestaurantSeedDataSource(
    private val context: Context,
    private val fileName: String = "restaurants.json"
) : RestaurantSeedDataSource {
    override suspend fun restaurants(): List<Restaurant> {
        val json = context.assets.open(fileName)
            .bufferedReader()
            .use { reader -> reader.readText() }

        return JSONObject(json)
            .getJSONArray("restaurants")
            .mapObjects { restaurantJson -> restaurantJson.toRestaurant() }
    }

    private fun JSONObject.toRestaurant(): Restaurant {
        return Restaurant(
            id = getString("id"),
            name = getString("name"),
            cuisines = getJSONArray("cuisines").mapStrings(),
            rating = getDouble("rating"),
            deliveryFeeCents = getInt("delivery_fee_cents"),
            etaMinutes = getInt("eta_minutes"),
            isOpen = getBoolean("is_open"),
            imageUrl = nullableString("image_url"),
            menu = getJSONArray("menu").mapObjects { categoryJson -> categoryJson.toMenuCategory() }
        )
    }

    private fun JSONObject.toMenuCategory(): MenuCategory {
        return MenuCategory(
            id = getString("id"),
            name = getString("name"),
            items = getJSONArray("items").mapObjects { itemJson -> itemJson.toMenuItem() }
        )
    }

    private fun JSONObject.toMenuItem(): MenuItem {
        return MenuItem(
            id = getString("id"),
            name = getString("name"),
            priceCents = getInt("price_cents"),
            available = getBoolean("available"),
            description = getString("description"),
            imageUrl = nullableString("image_url"),
            options = getJSONArray("options").mapObjects { optionJson -> optionJson.toMenuOption() }
        )
    }

    private fun JSONObject.toMenuOption(): MenuOption {
        return MenuOption(
            id = getString("id"),
            name = getString("name"),
            priceCents = getInt("price_cents")
        )
    }

    private fun JSONObject.nullableString(name: String): String? {
        if (!has(name) || isNull(name)) return null
        return optString(name).ifBlank { null }
    }

    private fun JSONArray.mapStrings(): List<String> {
        return List(length()) { index -> getString(index) }
    }

    private fun <T> JSONArray.mapObjects(transform: (JSONObject) -> T): List<T> {
        return List(length()) { index -> transform(getJSONObject(index)) }
    }
}
