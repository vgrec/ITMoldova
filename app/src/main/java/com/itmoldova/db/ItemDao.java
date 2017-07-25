package com.itmoldova.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.itmoldova.model.Item;

import java.util.List;

@Dao
public interface ItemDao {

    @Query("select * from item")
    List<Item> loadAllItems();

    @Query("select * from item where guid = :id")
    Item getItemById(String id);

    @Delete
    void deleteItem(Item item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItem(Item item);
}
