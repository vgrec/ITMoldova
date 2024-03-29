package com.itmoldova.bookmarks


import android.app.Fragment
import android.content.Intent
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itmoldova.*

import com.itmoldova.adapter.ArticlesAdapter
import com.itmoldova.db.AppDatabase
import com.itmoldova.detail.DetailActivity
import com.itmoldova.model.Article
import com.itmoldova.util.UiUtils
import javax.inject.Inject


/**
 * Show the list of bookmarked articles
 */
class BookmarksFragment : Fragment(), BookmarksContract.View {
    private lateinit var emptyView: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var presenter: BookmarksPresenter

    @Inject
    lateinit var database: AppDatabase

    @Inject
    lateinit var appSettings: AppSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ITMoldova.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_bookmarks, container, false)
        emptyView = view.findViewById(R.id.empty_view)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val dividerItemDecoration = DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL)
        val drawable = resources.getDrawable(R.drawable.list_divider)
        drawable.setTint(UiUtils.getDividerColor(view.context, appSettings.isDarkModeEnabled))
        dividerItemDecoration.setDrawable(drawable)
        recyclerView.addItemDecoration(dividerItemDecoration)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = BookmarksPresenter(this, database.articleDao())
    }

    override fun onResume() {
        super.onResume()
        presenter.loadBookmarks()
    }

    override fun onPause() {
        super.onPause()
        presenter.cancel()
    }

    override fun showBookmarks(articles: List<Article>) {
        val adapter = ArticlesAdapter(activity, articles, { article, image -> openArticleDetail(article, image) })
        adapter.setShowingBookmarks(true)
        recyclerView.adapter = adapter
        emptyView.visibility = if (articles.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun openArticleDetail(article: Article, image: ImageView) {
        val intent = Intent(activity, DetailActivity::class.java)
        intent.putExtra(Extra.ARTICLE, article)

        val transitionName = ViewCompat.getTransitionName(image) ?: ""
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            activity,
            image,
            transitionName)

        startActivity(intent, options.toBundle())
    }
}
