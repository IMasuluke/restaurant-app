package com.example.mrd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mrd.ui.RestaurantApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val deepLinkedRestaurantId = intent?.data
            ?.takeIf { it.host == "restaurants" }
            ?.lastPathSegment

        setContent {
            RestaurantApp(initialRestaurantId = deepLinkedRestaurantId)
        }
    }
}
