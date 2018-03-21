package com.itmoldova.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.itmoldova.model.Article

@Database(entities = arrayOf(Article::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao

}
