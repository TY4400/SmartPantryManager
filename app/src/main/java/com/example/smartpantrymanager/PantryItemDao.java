package com.example.smartpantrymanager;

import androidx.room.Dao;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PantryItemDao {

    // Add a pantry item
    @Insert
    void insert(PantryItem pantryItem);

    // Update an existing pantry item
    @Update
    void update(PantryItem pantryItem);

    // Get all pantry items
    @Query("SELECT * FROM pantry_items ORDER BY id DESC")
    List<PantryItem> getAllItems();

    // Delete a pantry item
    @Delete
    void delete(PantryItem pantryItem);

    // Delete all pantry items
    @Query("DELETE FROM pantry_items")
    void deleteAll();
}
