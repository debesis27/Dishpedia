package com.example.dishpedia.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MyRecipeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(myRecipe: MyRecipe)

    @Update
    suspend fun update(myRecipe: MyRecipe)

    @Delete
    suspend fun delete(myRecipe: MyRecipe)

    @Query("SELECT * from myRecipe WHERE id = :id")
    fun getMyRecipe(id: Int) : Flow<MyRecipe>

    @Query("SELECT * FROM myRecipe ORDER BY title ASC")
    fun getAllMyRecipe() : Flow<List<MyRecipe>>
}