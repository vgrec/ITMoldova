package com.itmoldova.detail

import com.itmoldova.R
import com.itmoldova.db.ItemDao
import com.itmoldova.model.Item
import com.itmoldova.parser.Block
import com.itmoldova.parser.ContentParser
import com.itmoldova.parser.DetailViewCreator
import com.itmoldova.util.Utils
import java.util.*

/**
 * Author vgrec, on 24.07.16.
 */
class DetailPresenter(private val view: DetailContract.View,
                      private val detailViewCreator: DetailViewCreator,
                      private val itemDao: ItemDao) : DetailContract.Presenter {

    private var blocks: MutableList<Block>? = mutableListOf()
    private var imageHeaderUrl: String? = null

    override fun loadArticleDetail(item: Item) {
        view.showTitle(item.title)

        val parser = ContentParser(item.content)
        blocks = parser.normalize(parser.parse())
        imageHeaderUrl = parser.getHeaderImageFromBlocks(blocks as MutableList<Block>)
        if (imageHeaderUrl != null) {
            view.showHeaderImage(imageHeaderUrl!!)
        } else {
            view.hideHeaderImage()
        }

        val views = detailViewCreator.createViewsFrom(blocks as List<Block>)
        view.showArticleDetail(views)
    }

    override fun loadRelatedArticles(items: List<Item>, item: Item) {
        val relatedArticles = Utils.getRelatedArticles(items, item, 5)
        view.showRelatedArticles(relatedArticles)
    }

    override fun extractPhotoUrlsFromArticle(): List<String> {
        if (blocks == null) {
            return emptyList()
        }

        val urls = ArrayList<String>()
        urls.add(imageHeaderUrl!!)
        blocks!!
                .filter { it.type == Block.Type.IMAGE }
                .mapTo(urls) { it.content }

        return urls
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
