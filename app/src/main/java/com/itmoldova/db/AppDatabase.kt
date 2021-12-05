package com.itmoldova.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.itmoldova.model.Article

@Database(entities = arrayOf(Article::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao

}
