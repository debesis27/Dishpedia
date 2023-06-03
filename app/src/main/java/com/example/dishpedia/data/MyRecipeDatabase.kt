package com.example.dishpedia.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MyRecipe::class], version = 1, exportSchema = false)
abstract class MyRecipeDatabase : RoomDatabase() {
    abstract fun myRecipeDao() : MyRecipeDao

    companion object{
        @Volatile
        private var Instance: MyRecipeDatabase? = null

        fun getDatabase(context: Context) : MyRecipeDatabase{
            return Instance ?: synchronized(this){
                Room.databaseBuilder(context, MyRecipeDatabase::class.java, "my_recipe_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}