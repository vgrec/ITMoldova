package com.itmoldova.utils

import com.itmoldova.TestUtils
import com.itmoldova.model.Item
import com.itmoldova.util.Utils
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class UtilsTest {

    @Test
    fun testGetRelatedArticles_WhenCurrentItemInTheList() {
        val currentItem = TestUtils.oneArticleWithImage()[0]
        val items = TestUtils.tenArticles()

        // current item is in the list -> should remove it and return the first six items

        val related = Utils.getRelatedArticles(items, currentItem, N)
        assertThat(related.size).isEqualTo(N - 1)
        assertThat(related).doesNotContain(currentItem)
    }

    @Test
    fun testGetRelatedArticles_WhenCurrentItemNotInTheList() {
        val currentItem = Item()
        currentItem.guid = "unique-100"
        val items = TestUtils.tenArticles()

        // current item is not in the list -> should return the first six items

        val related = Utils.getRelatedArticles(items, currentItem, N)
        assertThat(related.size).isEqualTo(N - 1)
        assertThat(related).doesNotContain(currentItem)
    }

    @Test
    fun testGetRelatedArticles_InvalidParams() {
        val related = Utils.getRelatedArticles(null, null, N)
        assertThat(related.size).isEqualTo(0)
    }

    companion object {
        val N = 5
    }
}



