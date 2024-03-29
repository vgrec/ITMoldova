package com.itmoldova.list

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.itmoldova.AppSettings
import com.itmoldova.Extra
import com.itmoldova.ITMoldova
import com.itmoldova.R
import com.itmoldova.adapter.ArticlesAdapter
import com.itmoldova.detail.DetailActivity
import com.itmoldova.model.Article
import com.itmoldova.model.Category
import com.itmoldova.util.EndlessScrollListener
import com.itmoldova.util.UiUtils
import java.util.*
import javax.inject.Inject

/**
 * Shows a list of articles.
 *
 *
 * Author vgrec, on 09.07.16.
 */
class ArticlesFragment : Fragment(), ArticlesContract.View {

    @Inject
    lateinit var appSettings: AppSettings

    private lateinit var adapter: ArticlesAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var endlessScrollListener: RecyclerViewEndlessScrollListener
    private lateinit var category: Category

    private var presenter: ArticlesContract.Presenter? = null
    private val articles = mutableListOf<Article>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ITMoldova.appComponent.inject(this)
        setHasOptionsMenu(true)
        category = arguments?.getSerializable(Extra.CATEGORY) as Category
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        swipeRefreshLayout = view.findViewById(R.id.refresh)

        val layoutManager = LinearLayoutManager(requireActivity())
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL)
        val drawable = resources.getDrawable(R.drawable.list_divider)
        drawable.setTint(UiUtils.getDividerColor(view.context, appSettings.isDarkModeEnabled))
        dividerItemDecoration.setDrawable(drawable)
        recyclerView.addItemDecoration(dividerItemDecoration)


        adapter = ArticlesAdapter(requireActivity(), articles) { article, imageView ->
            presenter?.onArticleClicked(articles, article, imageView)
        }

        recyclerView.adapter = adapter
        endlessScrollListener = RecyclerViewEndlessScrollListener(layoutManager)
        recyclerView.addOnScrollListener(endlessScrollListener)

        swipeRefreshLayout = view.findViewById(R.id.refresh)
        swipeRefreshLayout.setOnRefreshListener { presenter?.refreshArticles(category) }

        return view
    }

    private inner class RecyclerViewEndlessScrollListener(layoutManager: LinearLayoutManager) : EndlessScrollListener(layoutManager) {

        override fun onLoadMore(page: Int, totalItemsCount: Int) {
            presenter?.loadArticles(category, page)
        }
    }

    override fun openArticleDetail(articles: List<Article>, article: Article, imageView: ImageView) {
        val intent = Intent(activity, DetailActivity::class.java)
        intent.putExtra(Extra.ARTICLE, article)
        intent.putParcelableArrayListExtra(Extra.ARTICLES, ArrayList(articles))

        val transitionName = ViewCompat.getTransitionName(imageView) ?: ""
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                requireActivity(),
                imageView,
                transitionName)

        startActivity(intent, options.toBundle())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = ArticlesPresenter(this)
        presenter?.loadArticles(category, 0)
    }

    override fun showArticles(articles: List<Article>, clearDataSet: Boolean) {
        if (clearDataSet) {
            this.articles.clear()
            adapter.notifyDataSetChanged()
            endlessScrollListener.reset()
        }

        val fromPosition = this.articles.size
        val toPosition = articles.size
        this.articles.addAll(articles)
        adapter.notifyItemRangeInserted(fromPosition, toPosition)
    }

    override fun setLoadingIndicator(loading: Boolean) {
        swipeRefreshLayout.post { swipeRefreshLayout.isRefreshing = loading }
    }

    override fun showError() {
        showSnackbar(R.string.snackbar_generic_error)
    }

    override fun showNoInternetConnection() {
        showSnackbar(R.string.snackbar_no_connection)
    }

    private fun showSnackbar(@StringRes stringResId: Int) {
        activity?.let {
            Snackbar.make(
                    it.findViewById<RecyclerView>(R.id.recycler_view),
                    stringResId,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.snackbar_retry_action, { presenter?.refreshArticles(category) })
                    .show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.articles_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            when (item.itemId) {
                R.id.action_refresh -> {
                    presenter?.refreshArticles(category)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.cancel()
    }

    companion object {

        fun newInstance(category: Category): Fragment {
            val args = Bundle()
            args.putSerializable(Extra.CATEGORY, category)
            val fragment = ArticlesFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
