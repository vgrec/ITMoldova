package com.itmoldova

import com.itmoldova.model.Channel
import com.itmoldova.model.Article
import com.itmoldova.model.Rss

import java.util.ArrayList

/**
 * Helper class that provides test data.
 */
object TestData {

    fun rssResponse(): Rss {
        val channel = Channel()
        channel.articles = TestData.oneArticleWithImage()
        val rss = Rss()
        rss.channel = channel
        return rss
    }

    fun oneArticleWithImage(): List<Article> {
        val items = ArrayList<Article>()
        val item = Article()
        item.title = "ITMoldova a recunoscut"
        item.content = "În trei ani şi jumătate care au trecut de la lansarea portabilităţii numerelor de telefon. <img class=\"aligncenter\" src=\"https://upload.wikimedia.org/wikipedia/commons/3/36/Hopetoun_falls.jpg\" />"
        item.pubDate = "Thu, 05 Jan 2017 08:29:47 +0000"
        item.guid = "1"
        items.add(item)
        return items
    }

    fun oneArticleWithNoImage(): List<Article> {
        val items = ArrayList<Article>()
        val item = Article()
        item.title = "Oare ce a fost?"
        item.content = "Deoarece? şi jumătate care au trecut de la lansarea portabilităţii numerelor de telefon."
        item.pubDate = "Wed, 04 Jan 2017 08:17:38 +0000"
        item.guid = "2"
        items.add(item)
        return items
    }

    fun twoArticles(): List<Article> {
        val items = ArrayList<Article>()
        items.addAll(oneArticleWithImage())
        items.addAll(oneArticleWithNoImage())
        return items
    }

    fun sixArticles(): List<Article> {
        val items = ArrayList<Article>()
        items.addAll(twoArticles())

        val item1 = Article()
        item1.pubDate = "Wed, 04 Jan 2017 05:05:10 +0000"
        item1.guid = "3"
        items.add(item1)

        val item2 = Article()
        item2.pubDate = "Mon, 02 Jan 2017 08:08:19 +0000"
        item2.guid = "4"
        items.add(item2)

        val item3 = Article()
        item3.pubDate = "Mon, 02 Jan 2017 08:05:19 +0000"
        item3.guid = "5"
        items.add(item3)

        val item4 = Article()
        item4.pubDate = "Fri, 30 Dec 2016 06:35:32 +0000"
        item4.guid = "6"
        items.add(item4)

        return items
    }

    fun tenArticles(): List<Article> {
        val items = ArrayList<Article>()
        items.addAll(sixArticles())

        val item1 = Article()
        item1.pubDate = "Wed, 07 Jan 2018 05:05:10 +0000"
        item1.guid = "7"
        items.add(item1)

        val item2 = Article()
        item2.pubDate = "Mon, 07 Jan 2018 08:08:19 +0000"
        item2.guid = "8"
        items.add(item2)

        val item3 = Article()
        item3.pubDate = "Mon, 07 Jan 2018 08:05:19 +0000"
        item3.guid = "9"
        items.add(item3)

        val item4 = Article()
        item4.pubDate = "Fri, 07 Dec 2018 06:35:32 +0000"
        item4.guid = "10"
        items.add(item4)

        return items
    }
}

// https://www.youtube.com/watch?v=cA4iEmWuSB8, min 32:33, for injecting dependencies
// in Espresso tests.
