package com.itmoldova.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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
