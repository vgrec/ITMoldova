package com.itmoldova.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.itmoldova.model.Article;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface ArticleDao {

    @Query("select * from Article")
    Flowable<List<Article>> loadAllArticles();

    @Query("select * from Article where guid = :id")
    Flowable<Article> getArticleById(String id);

    @Delete
    void deleteArticle(Article article);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertArticle(Article article);
}
