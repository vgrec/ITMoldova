package com.itmoldova.detail

import android.animation.ValueAnimator
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
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.transition.Transition
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import com.itmoldova.Extra
import com.itmoldova.ITMoldova
import com.itmoldova.R
import com.itmoldova.db.AppDatabase
import com.itmoldova.kotlinex.lollipopAndAbove
import com.itmoldova.model.Article
import com.itmoldova.photoview.PhotoViewActivity
import com.itmoldova.util.HtmlParser
import com.itmoldova.util.UiUtils
import com.itmoldova.util.Utils
import com.squareup.picasso.Picasso
import javax.inject.Inject

/**
 * Shows details of an article.
 *
 *
 * Author vgrec, on 09.07.16.
 */
class DetailFragment : Fragment(), DetailContract.View, View.OnClickListener {

    private lateinit var relatedGroup: ViewGroup
    private lateinit var titleView: TextView
    private lateinit var toolbar: Toolbar
    private lateinit var imageHeaderView: ImageView
    private lateinit var fab: FloatingActionButton
    private lateinit var webView: WebView

    private lateinit var article: Article
    private lateinit var topArticles: List<Article>

    private lateinit var presenter: DetailContract.Presenter

    @Inject
    lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        article = arguments.getParcelable(Extra.ARTICLE)
        topArticles = arguments.getParcelableArrayList(Extra.ARTICLES)
        setHasOptionsMenu(true)
        ITMoldova.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_details, container, false)

        relatedGroup = view.findViewById(R.id.related)
        titleView = view.findViewById(R.id.title)
        toolbar = view.findViewById(R.id.toolbar)
        imageHeaderView = view.findViewById(R.id.image_header)

        webView = view.findViewById(R.id.webview)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                openInBrowser(url)
                return true
            }
        }

        fab = view.findViewById(R.id.fab)
        fab.setOnClickListener { presenter.addOrRemoveFromBookmarks(fab.tag == R.drawable.ic_star_full, article) }

        view.findViewById<View>(R.id.view_in_browser).setOnClickListener { openInBrowser(article.link) }

        val scrimView = view.findViewById<View>(R.id.image_header_scrim)

        view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                view.postDelayed({
                    val animator = ValueAnimator.ofFloat(0f, 1f)
                    animator.duration = resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
                    animator.addUpdateListener { animation -> scrimView.alpha = animation.animatedValue as Float }
                    animator.start()
                }, resources.getInteger(android.R.integer.config_shortAnimTime).toLong())
            }
        })

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolbar()

        val displayWidth = activity.windowManager.defaultDisplay.width
        val displayDensity = resources.displayMetrics.density
        val bgColor = ContextCompat.getColor(activity, R.color.content_main_background)
        val textColor = ContextCompat.getColor(activity, R.color.article_title)
        val linkColor = ContextCompat.getColor(activity, R.color.colorAccent)

        val htmlParser = HtmlParser(displayWidth, displayDensity, bgColor, textColor, linkColor)
        presenter = DetailPresenter(this, database.articleDao(), htmlParser)

        loadArticle(topArticles, article)
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

    override fun showArticleDetail(content: String) {
        webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null)
    }

    override fun showRelatedArticles(relatedArticles: List<Article>) {
        val relatedArticlesView = UiUtils.createRelatedViews(activity, relatedArticles) { relatedArticle ->
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(Extra.ARTICLE, relatedArticle)
            intent.putParcelableArrayListExtra(Extra.ARTICLES, ArrayList(this.topArticles))
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
        fab.tag = iconResId
    }

    override fun onClick(v: View) {
        val urls = UiUtils.extractPhotoUrlsFromArticle(article.content)
        val intent = Intent(activity, PhotoViewActivity::class.java)
        intent.putStringArrayListExtra(Extra.PHOTO_URLS, urls as ArrayList<String>)
        intent.putExtra(Extra.CLICKED_URL, v.tag as String)
        startActivity(intent)
    }

    private fun openInBrowser(url: String?) {
        url?.let {
            val builder = CustomTabsIntent.Builder()
            builder.setToolbarColor(resources.getColor(R.color.colorPrimary))
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(activity, Uri.parse(url))
        }
    }

    fun loadArticle(topArticles: List<Article>, article: Article) {
        presenter.loadArticleDetail(article)
        presenter.setProperBookmarkIcon(article)
        if (topArticles.isNotEmpty()) {
            presenter.loadRelatedArticles(topArticles, article)
        }
    }

    companion object {

        val FAB_CLOSE_ANIM_DURATION = 150L

        fun newInstance(relatedArticles: List<Article>?, article: Article): DetailFragment {
            val args = Bundle()
            args.putParcelable(Extra.ARTICLE, article)
            args.putParcelableArrayList(Extra.ARTICLES, relatedArticles?.let { ArrayList(relatedArticles) } ?: ArrayList())
            val fragment = DetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    fun hideFab() {
        fab.animate()
                .scaleX(0f)
                .scaleY(0f)
                .setDuration(FAB_CLOSE_ANIM_DURATION)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.cancel()
    }
}
