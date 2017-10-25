package com.itmoldova.utils;

import com.itmoldova.TestUtils;
import com.itmoldova.model.Item;
import com.itmoldova.util.Utils;

import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UtilsTest {

    public static final int N = 5;

    @Test
    public void testGetRelatedArticles_WhenCurrentItemInTheList() {
        Item currentItem = TestUtils.oneArticleWithImage().get(0);
        List<Item> items = TestUtils.tenArticles();

        // current item is in the list -> should remove it and return the first six items

        List<Item> related = Utils.INSTANCE.getRelatedArticles(items, currentItem, N);
        assertThat(related.size()).isEqualTo(N - 1);
        assertThat(related).doesNotContain(currentItem);
    }

    @Test
    public void testGetRelatedArticles_WhenCurrentItemNotInTheList() {
        Item currentItem = new Item();
        currentItem.setGuid("unique-100");
        List<Item> items = TestUtils.tenArticles();

        // current item is not in the list -> should return the first six items

        List<Item> related = Utils.INSTANCE.getRelatedArticles(items, currentItem, N);
        assertThat(related.size()).isEqualTo(N - 1);
        assertThat(related).doesNotContain(currentItem);
    }

    @Test
    public void testGetRelatedArticles_InvalidParams() {
        List<Item> related = Utils.INSTANCE.getRelatedArticles(null, null, N);
        assertThat(related.size()).isEqualTo(0);
    }
}



