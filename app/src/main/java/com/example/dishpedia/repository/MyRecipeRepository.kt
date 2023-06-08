package com.example.dishpedia.repository

import com.example.dishpedia.data.MyRecipe
import com.example.dishpedia.data.MyRecipeDao
import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [MyRecipe] from a given data source.
 */
class MyRecipeRepository(private val myRecipeDao: MyRecipeDao) {
    suspend fun insertMyRecipe(myRecipe: MyRecipe) = myRecipeDao.insert(myRecipe)

    suspend fun updateMyRecipe(myRecipe: MyRecipe) = myRecipeDao.update(myRecipe)

    suspend fun deleteMyRecipe(myRecipe: MyRecipe) = myRecipeDao.delete(myRecipe)

    fun getMyRecipe(id: Int): Flow<MyRecipe?> = myRecipeDao.getMyRecipe(id)

    fun getAllMyRecipe(): Flow<List<MyRecipe>> = myRecipeDao.getAllMyRecipe()
}