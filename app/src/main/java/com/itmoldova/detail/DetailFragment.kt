package com.itmoldova.detail

import android.annotation.TargetApi
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.customtabs.CustomTabsIntent
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.transition.Transition
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.itmoldova.Extra
import com.itmoldova.R
import com.itmoldova.db.AppDatabase
import com.itmoldova.db.ItemDao
import com.itmoldova.kotlinex.lollipopAndAbove
import com.itmoldova.model.Item
import com.itmoldova.parser.DetailViewCreator
import com.itmoldova.photoview.PhotoViewActivity
import com.itmoldova.util.Utils
import com.squareup.picasso.Picasso

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
    private lateinit var fab: FloatingActionButton

    private lateinit var item: Item
    private lateinit var items: List<Item>
    private lateinit var detailViewCreator: DetailViewCreator

    private lateinit var presenter: DetailContract.Presenter
    private lateinit var dao: ItemDao

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

        fab = view.findViewById(R.id.fab)
        fab.setOnClickListener { presenter.addRemoveFromBookmarks(item) }

        view.findViewById<View>(R.id.view_in_browser).setOnClickListener { openInBrowser() }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolbar()

        dao = AppDatabase.getDatabase(activity).itemDao()
        detailViewCreator = DetailViewCreator(activity)

        presenter = DetailPresenter(this, DetailViewCreator(activity), dao)

        fab.setImageResource(if (presenter.isItemBookmarked(item)) R.drawable.ic_star_full else R.drawable.ic_star_outline)
        loadArticle(items, item)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        showFabOnTransitionEnd()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun showFabOnTransitionEnd() {
        lollipopAndAbove {
            val sharedElementTransition = activity.window.sharedElementEnterTransition
            sharedElementTransition?.let {
                it.addListener(object : Utils.TransactionListenerAdapter() {
                    override fun onTransitionEnd(transition: Transition?) {
                        fab.show()
                    }

                    override fun onTransitionStart(transition: Transition?) {
                        fab.visibility = View.INVISIBLE
                    }
                })
            }
        }
    }

    private fun setupToolbar() {
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowTitleEnabled(false)
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

    override fun updateStarIcon(iconResId: Int) {
        fab.setImageResource(iconResId)
    }

    override fun onClick(v: View) {
        val urls = presenter.extractPhotoUrlsFromArticle()
        val intent = Intent(activity, PhotoViewActivity::class.java)
        intent.putStringArrayListExtra(Extra.PHOTO_URLS, urls as ArrayList<String>)
        intent.putExtra(Extra.CLICKED_URL, v.tag as String)
        startActivity(intent)
    }

    private fun openInBrowser() {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(resources.getColor(R.color.colorPrimary))
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(activity, Uri.parse(item.link))
    }

    fun loadArticle(items: List<Item>, item: Item) {
        presenter.loadArticleDetail(item)
        if (items.isNotEmpty()) {
            presenter.loadRelatedArticles(items, item)
        }
    }

    companion object {

        fun newInstance(items: List<Item>?, item: Item): DetailFragment {
            val args = Bundle()
            args.putParcelable(Extra.ITEM, item)
            args.putParcelableArrayList(Extra.ITEMS, items?.let { ArrayList(items) } ?: ArrayList())
            val fragment = DetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
