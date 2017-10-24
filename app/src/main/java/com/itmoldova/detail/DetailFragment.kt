package com.itmoldova.detail

import android.app.Fragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.itmoldova.Extra
import com.itmoldova.R
import com.itmoldova.comments.NewCommentActivity
import com.itmoldova.db.AppDatabase
import com.itmoldova.model.Item
import com.itmoldova.parser.DetailViewCreator
import com.itmoldova.photoview.PhotoViewActivity
import com.squareup.picasso.Picasso
import java.util.*

/**
 * Shows details of an article.
 *
 *
 * Author vgrec, on 09.07.16.
 */
class DetailFragment : Fragment(), DetailContract.View, View.OnClickListener {

    private lateinit var contentGroup: ViewGroup
    private lateinit var relatedGroup: ViewGroup
    private lateinit var titleView: TextView
    private lateinit var toolbar: Toolbar
    private lateinit var imageHeaderView: ImageView

    private lateinit var item: Item
    private lateinit var items: List<Item>
    private lateinit var detailViewCreator: DetailViewCreator

    private lateinit var presenter: DetailContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        item = arguments.getParcelable(Extra.ITEM)
        items = arguments.getParcelableArrayList(Extra.ITEMS)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_details, container, false)

        contentGroup = view.findViewById(R.id.content)
        relatedGroup = view.findViewById(R.id.related)
        titleView = view.findViewById(R.id.title)
        toolbar = view.findViewById(R.id.toolbar)
        imageHeaderView = view.findViewById(R.id.image_header)

        view.findViewById<View>(R.id.fab).setOnClickListener { openNewCommentActivity() }
        view.findViewById<View>(R.id.view_in_browser).setOnClickListener { openInBrowser() }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolbar()

        detailViewCreator = DetailViewCreator(activity)
        presenter = DetailPresenter(this, DetailViewCreator(activity))
        loadArticle(items, item)
    }

    private fun setupToolbar() {
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.articles_list, menu)
    }

    override fun showArticleDetail(views: List<View>) {
        contentGroup.removeAllViews()
        for (view in views) {
            if (view is ImageView) {
                view.setOnClickListener(this)
            }
            contentGroup.addView(view)
        }
    }

    override fun showRelatedArticles(relatedItems: List<Item>) {
        val relatedArticlesView = detailViewCreator.createRelatedViews(relatedItems) { relatedArticle ->
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(Extra.ITEM, relatedArticle)
            intent.putParcelableArrayListExtra(Extra.ITEMS, ArrayList(items))
            activity.finish()
            startActivity(intent)
        }
        relatedGroup.addView(relatedArticlesView)
    }

    override fun showTitle(title: String) {
        titleView.text = title
    }

    override fun showHeaderImage(url: String) {
        imageHeaderView.visibility = View.VISIBLE
        imageHeaderView.tag = url
        imageHeaderView.setOnClickListener(this)
        Picasso.with(activity).load(url).into(imageHeaderView)
    }

    override fun hideHeaderImage() {
        imageHeaderView.visibility = View.GONE
    }

    override fun onClick(v: View) {
        val urls = presenter.extractPhotoUrlsFromArticle()
        val intent = Intent(activity, PhotoViewActivity::class.java)
        intent.putStringArrayListExtra(Extra.PHOTO_URLS, urls as ArrayList<String>)
        intent.putExtra(Extra.CLICKED_URL, v.tag as String)
        startActivity(intent)
    }

    private fun openNewCommentActivity() {
        startActivity(Intent(activity, NewCommentActivity::class.java))
    }

    private fun openInBrowser() {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(resources.getColor(R.color.colorPrimary))
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(activity, Uri.parse(item.link))
    }

    private fun bookMarkArticle() {
        // test code to bookmark an article.
        val db = AppDatabase.getDatabase(activity)
        if (db.itemDao().getItemById(item.guid) == null) {
            db.itemDao().insertItem(item)
        } else {
            db.itemDao().deleteItem(item)
        }

        // show the list of all items:
        val items = db.itemDao().loadAllItems()
        for (item in items) {
            println(item.title)
        }
    }

    fun loadArticle(items: List<Item>, item: Item) {
        presenter.loadArticleDetail(item)
        presenter.loadRelatedArticles(items, item)
    }

    companion object {

        fun newInstance(items: List<Item>, item: Item): DetailFragment {
            val args = Bundle()
            args.putParcelable(Extra.ITEM, item)
            args.putParcelableArrayList(Extra.ITEMS, ArrayList(items))
            val fragment = DetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}