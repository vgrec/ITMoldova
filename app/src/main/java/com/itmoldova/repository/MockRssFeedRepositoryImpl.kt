package com.itmoldova.repository

import com.itmoldova.model.Article
import com.itmoldova.model.Channel
import com.itmoldova.model.Rss
import io.reactivex.Observable
import java.util.UUID
import javax.inject.Inject

private val MOCK_TITLES = listOf(
    "Tehnologii de viitor",
    "A fi sau a nu fi",
    "Trenduri in Tehnologia Informatiei",
    "Lorem Ipsum si alte Istorii",
    "200 Mb/s Viteza Mazima",
    "Ce? Unde? Cand?",
)

private val MOCK_DESCRIPTIONS = listOf(
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut " +
            "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco " +
            "laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit " +
            "in voluptate velit esse cillum dolore eu fugiat nulla pariatur.\n",

    "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, " +
            "totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae " +
            "vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit " +
            "aut fugit, sed quia consequuntur magni dolores.\n"
)

private val MOCK_IMAGE_URLS = listOf(
    "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9e/Netflix_iPhone.jpg/320px-Netflix_iPhone.jpg",
    "https://upload.wikimedia.org/wikipedia/commons/thumb/4/46/Cybersecurity.png/640px-Cybersecurity.png",
    "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7c/Micro_USB_phone_charger.jpg/574px-Micro_USB_phone_charger.jpg",
    "https://upload.wikimedia.org/wikipedia/commons/3/39/Berom%C3%BCnster_-_Blosenbergturm2.jpg",
    "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9f/A_cell_phone.jpg/640px-A_cell_phone.jpg",
    "https://upload.wikimedia.org/wikipedia/commons/thumb/a/af/Engineering_College_Building_of_Southern_University_of_Science_and_Technology_Under_Construction.jpg/640px-Engineering_College_Building_of_Southern_University_of_Science_and_Technology_Under_Construction.jpg"
)

class MockRssFeedRepositoryImpl @Inject constructor() : RssFeedRepository {

    override fun getDefaultRssFeed(page: Int): Observable<Rss> {
        return Observable.just(createMockRssResponse())
    }

    override fun getRssFeedByCategory(categoryName: String, page: Int): Observable<Rss> {
        return Observable.just(createMockRssResponse())
    }

    private fun createMockRssResponse(): Rss {
        return Rss().apply {
            version = "1.0"
            channel = Channel().apply {
                articles = listOf(
                    mockArticle(),
                    mockArticle(),
                    mockArticle(),
                    mockArticle(),
                    mockArticle(),
                    mockArticle(),
                    mockArticle(),
                    mockArticle(),
                    mockArticle(),
                )
            }
        }
    }

    private fun mockArticle() = buildMockArticle(
        titles = MOCK_TITLES,
        descriptions = MOCK_DESCRIPTIONS,
        imageUrls = MOCK_IMAGE_URLS
    )

    private fun buildMockArticle(
        titles: List<String>,
        descriptions: List<String>,
        imageUrls: List<String>,
    ): Article {
        val imageTag = "<img class=\"aligncenter\" src=\"${imageUrls.random()}\" />"
        return Article().apply {
            title = titles.random()
            content = "${descriptions.random()} $imageTag"
            pubDate = "Thu, 05 Jan 2017 08:29:47 +0000"
            guid = UUID.randomUUID().toString()
        }
    }
}