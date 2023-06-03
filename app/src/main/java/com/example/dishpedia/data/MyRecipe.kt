package com.example.dishpedia.data

import android.media.Image
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "myRecipe")
data class MyRecipe(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val image: Image,
    val title: String = "",
    val summary: String = "",
    val readyInMinutes: Int = 0,
    val servings: Int = 0,
    val ingredient: List<String> = listOf(""),
    val instructions: List<String> = listOf("")
)
