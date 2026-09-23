package com.example.smartpantrymanager;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {

    // Add a recipe
    @Insert
    void insert(Recipe recipe);

    // Get all recipes
    @Query("SELECT * FROM recipes ORDER BY id ASC")
    List<Recipe> getAllRecipes();

    // Check how many recipes are stored
    @Query("SELECT COUNT(*) FROM recipes")
    int getRecipeCount();

    // Delete all recipes
    @Query("DELETE FROM recipes")
    void deleteAll();
}
