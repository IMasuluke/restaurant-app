package com.example.mrd.database

import androidx.room.TypeConverter
import com.example.mrd.domain.MenuCategory
import com.example.mrd.domain.MenuItem
import com.example.mrd.domain.MenuOption

class RoomTypeConverters {
    @TypeConverter
    fun cuisinesToString(cuisines: List<String>): String {
        return cuisines.joinToString(separator = LIST_SEPARATOR) { it.escape() }
    }

    @TypeConverter
    fun stringToCuisines(value: String): List<String> {
        if (value.isBlank()) return emptyList()
        return value.split(LIST_SEPARATOR).map { it.unescape() }
    }

    @TypeConverter
    fun menuToString(menu: List<MenuCategory>): String {
        return menu.joinToString(separator = CATEGORY_SEPARATOR) { category ->
            listOf(
                category.id.escape(),
                category.name.escape(),
                menuItemsToString(category.items).escape()
            ).joinToString(FIELD_SEPARATOR)
        }
    }

    @TypeConverter
    fun stringToMenu(value: String): List<MenuCategory> {
        if (value.isBlank()) return emptyList()
        return value.split(CATEGORY_SEPARATOR).mapNotNull { rawCategory ->
            val fields = rawCategory.split(FIELD_SEPARATOR)
            if (fields.size != 3) return@mapNotNull null
            MenuCategory(
                id = fields[0].unescape(),
                name = fields[1].unescape(),
                items = stringToMenuItems(fields[2].unescape())
            )
        }
    }

    private fun menuItemsToString(items: List<MenuItem>): String {
        return items.joinToString(separator = ITEM_SEPARATOR) { item ->
            listOf(
                item.id.escape(),
                item.name.escape(),
                item.priceCents.toString(),
                item.available.toString(),
                item.description.escape(),
                item.imageUrl.orEmpty().escape(),
                menuOptionsToString(item.options).escape()
            ).joinToString(FIELD_SEPARATOR)
        }
    }

    private fun stringToMenuItems(value: String): List<MenuItem> {
        if (value.isBlank()) return emptyList()
        return value.split(ITEM_SEPARATOR).mapNotNull { rawItem ->
            val fields = rawItem.split(FIELD_SEPARATOR)
            if (fields.size != 7) return@mapNotNull null
            MenuItem(
                id = fields[0].unescape(),
                name = fields[1].unescape(),
                priceCents = fields[2].toIntOrNull() ?: 0,
                available = fields[3].toBooleanStrictOrNull() ?: false,
                description = fields[4].unescape(),
                imageUrl = fields[5].unescape().ifBlank { null },
                options = stringToMenuOptions(fields[6].unescape())
            )
        }
    }

    private fun menuOptionsToString(options: List<MenuOption>): String {
        return options.joinToString(separator = OPTION_SEPARATOR) { option ->
            listOf(
                option.id.escape(),
                option.name.escape(),
                option.priceCents.toString()
            ).joinToString(FIELD_SEPARATOR)
        }
    }

    private fun stringToMenuOptions(value: String): List<MenuOption> {
        if (value.isBlank()) return emptyList()
        return value.split(OPTION_SEPARATOR).mapNotNull { rawOption ->
            val fields = rawOption.split(FIELD_SEPARATOR)
            if (fields.size != 3) return@mapNotNull null
            MenuOption(
                id = fields[0].unescape(),
                name = fields[1].unescape(),
                priceCents = fields[2].toIntOrNull() ?: 0
            )
        }
    }

    private fun String.escape(): String {
        return replace("\\", "\\\\")
            .replace(FIELD_SEPARATOR, "\\f")
            .replace(LIST_SEPARATOR, "\\l")
            .replace(CATEGORY_SEPARATOR, "\\c")
            .replace(ITEM_SEPARATOR, "\\i")
            .replace(OPTION_SEPARATOR, "\\o")
    }

    private fun String.unescape(): String {
        val output = StringBuilder()
        var index = 0
        while (index < length) {
            val char = this[index]
            if (char == '\\' && index + 1 < length) {
                output.append(
                    when (this[index + 1]) {
                        '\\' -> '\\'
                        'f' -> FIELD_SEPARATOR
                        'l' -> LIST_SEPARATOR
                        'c' -> CATEGORY_SEPARATOR
                        'i' -> ITEM_SEPARATOR
                        'o' -> OPTION_SEPARATOR
                        else -> this[index + 1]
                    }
                )
                index += 2
            } else {
                output.append(char)
                index += 1
            }
        }
        return output.toString()
    }

    private companion object {
        const val FIELD_SEPARATOR = "|"
        const val LIST_SEPARATOR = "~list~"
        const val CATEGORY_SEPARATOR = "~category~"
        const val ITEM_SEPARATOR = "~item~"
        const val OPTION_SEPARATOR = "~option~"
    }
}
