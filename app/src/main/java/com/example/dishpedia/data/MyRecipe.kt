package com.example.dishpedia.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@Entity(tableName = "myRecipe")
//@TypeConverters(Converters::class)
data class MyRecipe(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val image: String,
    val title: String = "",
    val summary: String = "",
    val readyInMinutes: Double = 0.0,
    val category: String = "",
    val vegetarian: Boolean,
    val servings: Double = 0.0,
    val ingredient: String,
    val instructions: String
)

//object Converters {
//    @TypeConverter
//    fun fromString(value: String?): ArrayList<String> {
//        val listType = object : TypeToken<ArrayList<String?>?>() {}.type
//        return Gson().fromJson(value, listType)
//    }
//
//    @TypeConverter
//    fun fromArrayList(list: ArrayList<String?>?): String {
//        val gson = Gson()
//        return gson.toJson(list)
//    }
//}
