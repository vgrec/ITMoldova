package com.itmoldova.detail

import com.itmoldova.R
import com.itmoldova.db.ArticleDao
import com.itmoldova.model.Article
import com.itmoldova.util.HtmlParser
import com.itmoldova.util.Utils
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Author vgrec, on 24.07.16.
 */
class DetailPresenter(private val view: DetailContract.View,
                      private val articleDao: ArticleDao,
                      private val htmlParser: HtmlParser) : DetailContract.Presenter {

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun loadArticleDetail(article: Article) {
        view.showTitle(article.title)

        val content = htmlParser.parse(article.content)
        view.showArticleDetail(content)

        val imageUrl = htmlParser.getHeaderImageUrl()
        if (imageUrl != null && imageUrl.isNotEmpty()) {
            view.showHeaderImage(imageUrl)
        } else {
            view.hideHeaderImage()
        }
    }

    override fun loadRelatedArticles(topArticles: List<Article>, article: Article) {
        val relatedArticles = Utils.getRelatedArticles(topArticles, article, 5)
        view.showRelatedArticles(relatedArticles)
    }

    override fun addOrRemoveFromBookmarks(isAlreadyAdded: Boolean, article: Article) {
        if (isAlreadyAdded) {
            disposables.add(Completable.fromAction { articleDao.deleteArticle(article) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ view.updateFavoriteIcon(R.drawable.ic_favorite_outline) }))
        } else {
            disposables.add(Completable.fromAction { articleDao.insertArticle(article) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ view.updateFavoriteIcon(R.drawable.ic_favorite_full) }))
        }
    }

    override fun setProperBookmarkIcon(article: Article) {
        disposables.add(articleDao.getArticleById(article.guid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { _ -> view.updateFavoriteIcon(R.drawable.ic_favorite_full) },
                        { _ -> view.updateFavoriteIcon(R.drawable.ic_favorite_outline) }))
    }

    override fun cancel() {
        disposables.clear()
    }

}
