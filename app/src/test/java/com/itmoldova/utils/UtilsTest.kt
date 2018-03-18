package com.itmoldova.utils

import com.itmoldova.TestUtils
import com.itmoldova.model.Article
import com.itmoldova.util.Utils
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class UtilsTest {

    @Test
    fun testGetRelatedArticles_WhenCurrentArticleInTheList() {
        val currentArticle = TestUtils.oneArticleWithImage()[0]
        val articles = TestUtils.tenArticles()

        // current article is in the list -> should remove it and return the first six articles

        val related = Utils.getRelatedArticles(articles, currentArticle, N)
        assertThat(related.size).isEqualTo(N - 1)
        assertThat(related).doesNotContain(currentArticle)
    }

    @Test
    fun testGetRelatedArticles_WhenCurrentArticleNotInTheList() {
        val currentArticle = Article()
        currentArticle.guid = "unique-100"
        val articles = TestUtils.tenArticles()

        // current article is not in the list -> should return the first six articles

        val related = Utils.getRelatedArticles(articles, currentArticle, N)
        assertThat(related.size).isEqualTo(N - 1)
        assertThat(related).doesNotContain(currentArticle)
    }

    @Test
    fun testGetRelatedArticles_InvalidParams() {
        val related = Utils.getRelatedArticles(null, null, N)
        assertThat(related.size).isEqualTo(0)
    }

    companion object {
        val N = 5L
    }
}



