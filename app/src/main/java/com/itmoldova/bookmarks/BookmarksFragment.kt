package com.itmoldova.bookmarks


import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.itmoldova.Extra

import com.itmoldova.R
import com.itmoldova.adapter.ArticlesAdapter
import com.itmoldova.db.AppDatabase
import com.itmoldova.detail.DetailActivity
import com.itmoldova.model.Item


/**
 * Show the list of bookmarked articles
 */
class BookmarksFragment : Fragment(), BookmarksContract.View {
    private lateinit var recyclerView: RecyclerView
    private lateinit var presenter: BookmarksPresenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_bookmarks, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        presenter = BookmarksPresenter(this, AppDatabase.getDatabase(activity).itemDao())
        presenter.loadBookmarks()
    }

    override fun showBookmarks(items: List<Item>) {
        val adapter = ArticlesAdapter(activity, items, { item, image -> openArticleDetail(item, image) })
        adapter.setShowingBookmarks(true)
        recyclerView.adapter = adapter
    }

    private fun openArticleDetail(item: Item, image: ImageView) {
        val intent = Intent(activity, DetailActivity::class.java)
        intent.putExtra(Extra.ITEM, item)

        val transitionName = ViewCompat.getTransitionName(image)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,
                image,
                transitionName)

        startActivity(intent, options.toBundle())
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.cancel()
    }

}
