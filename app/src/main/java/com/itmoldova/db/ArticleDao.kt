package com.itmoldova.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

import com.itmoldova.model.Article

import io.reactivex.Flowable

@Dao
interface ArticleDao {

    @Query("select * from Article")
    fun loadAllArticles(): Flowable<List<Article>>

    @Query("select * from Article where guid = :id")
    fun getArticleById(id: String): Flowable<Article>

    @Delete
    fun deleteArticle(article: Article)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticle(article: Article)
}
