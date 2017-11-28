package com.itmoldova.detail

import com.itmoldova.R
import com.itmoldova.db.ItemDao
import com.itmoldova.model.Item
import com.itmoldova.util.HtmlParser
import com.itmoldova.util.Utils

/**
 * Author vgrec, on 24.07.16.
 */
class DetailPresenter(private val view: DetailContract.View,
                      private val itemDao: ItemDao,
                      private val htmlParser: HtmlParser) : DetailContract.Presenter {

    override fun loadArticleDetail(item: Item) {
        view.showTitle(item.title)

        val content = htmlParser.parse(item.content)
        view.showArticleDetail(content)

        val imageUrl = htmlParser.getHeaderImageUrl()
        if (imageUrl != null) {
            view.showHeaderImage(imageUrl)
        } else {
            view.hideHeaderImage()
        }
    }

    override fun loadRelatedArticles(items: List<Item>, item: Item) {
        val relatedArticles = Utils.getRelatedArticles(items, item, 5)
        view.showRelatedArticles(relatedArticles)
    }

    override fun addRemoveFromBookmarks(item: Item) {
        if (itemDao.getItemById(item.guid) == null) {
            itemDao.insertItem(item)
            view.updateStarIcon(R.drawable.ic_star_full)
        } else {
            itemDao.deleteItem(item)
            view.updateStarIcon(R.drawable.ic_star_outline)
        }
    }

    override fun isItemBookmarked(item: Item): Boolean = itemDao.getItemById(item.guid) != null

}
