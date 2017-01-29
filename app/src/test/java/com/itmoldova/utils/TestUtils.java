package com.itmoldova.utils;

import com.itmoldova.model.Channel;
import com.itmoldova.model.Item;
import com.itmoldova.model.Rss;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class that provides test data.
 */
public class TestUtils {

    public static Rss rssResponse() {
        Channel channel = new Channel();
        channel.setItemList(TestUtils.oneArticleWithImage());
        Rss rss = new Rss();
        rss.setChannel(channel);
        return rss;
    }

    public static List<Item> oneArticleWithImage() {
        List<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setTitle("ITMoldova a recunoscut");
        item.setContent("În trei ani şi jumătate care au trecut de la lansarea portabilităţii numerelor de telefon. <img class=\"aligncenter\" src=\"https://upload.wikimedia.org/wikipedia/commons/3/36/Hopetoun_falls.jpg\" />");
        item.setPubDate("Thu, 05 Jan 2017 08:29:47 +0000");
        items.add(item);
        return items;
    }

    public static List<Item> oneArticleWithNoImage() {
        List<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setTitle("Oare ce a fost?");
        item.setContent("Deoarece? şi jumătate care au trecut de la lansarea portabilităţii numerelor de telefon.");
        item.setPubDate("Wed, 04 Jan 2017 08:17:38 +0000");
        items.add(item);
        return items;
    }

    public static List<Item> twoArticles() {
        List<Item> items = new ArrayList<>();
        items.addAll(oneArticleWithImage());
        items.addAll(oneArticleWithNoImage());
        return items;
    }

    public static List<Item> sixArticles() {
        List<Item> items = new ArrayList<>();
        items.addAll(twoArticles());

        Item item1 = new Item();
        item1.setPubDate("Wed, 04 Jan 2017 05:05:10 +0000");
        items.add(item1);

        Item item2 = new Item();
        item2.setPubDate("Mon, 02 Jan 2017 08:08:19 +0000");
        items.add(item2);

        Item item3 = new Item();
        item3.setPubDate("Mon, 02 Jan 2017 08:05:19 +0000");
        items.add(item3);

        Item item4 = new Item();
        item4.setPubDate("Fri, 30 Dec 2016 06:35:32 +0000");
        items.add(item4);

        return items;
    }
}

// https://www.youtube.com/watch?v=cA4iEmWuSB8, min 32:33, for injecting dependencies
// in Espresso tests.
