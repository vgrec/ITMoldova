package com.itmoldova.list

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Toast
import com.itmoldova.Extra
import com.itmoldova.R
import com.itmoldova.adapter.ArticlesAdapter
import com.itmoldova.detail.DetailActivity
import com.itmoldova.http.ITMoldovaServiceCreator
import com.itmoldova.model.Category
import com.itmoldova.model.Item
import com.itmoldova.util.EndlessScrollListener
import java.util.*

/**
 * Shows a list of articles.
 *
 *
 * Author vgrec, on 09.07.16.
 */
class ArticlesFragment : Fragment(), ArticlesContract.View {

    private lateinit var adapter: ArticlesAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var endlessScrollListener: RecyclerViewEndlessScrollListener
    private lateinit var category: Category

    private var presenter: ArticlesContract.Presenter? = null
    private val items = ArrayList<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        category = arguments.getSerializable(Extra.CATEGORY) as Category
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        swipeRefreshLayout = view.findViewById(R.id.refresh)

        val layoutManager = LinearLayoutManager(activity)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = layoutManager


        adapter = ArticlesAdapter(activity, items, { item -> presenter?.onArticleClicked(items, item) })

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

    override fun openArticleDetail(items: List<Item>, item: Item) {
        val intent = Intent(activity, DetailActivity::class.java)
        intent.putExtra(Extra.ITEM, item)
        intent.putParcelableArrayListExtra(Extra.ITEMS, ArrayList(items))
        startActivity(intent)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = ArticlesPresenter(
                ITMoldovaServiceCreator.createItMoldovaService(activity),
                this)
        presenter?.loadArticles(category, 0)
    }

    override fun showArticles(items: List<Item>, clearDataSet: Boolean) {
        if (clearDataSet) {
            this.items.clear()
            adapter.notifyDataSetChanged()
            endlessScrollListener.reset()
        }

        val fromPosition = this.items.size
        val toPosition = items.size
        this.items.addAll(items)
        adapter.notifyItemRangeInserted(fromPosition, toPosition)
    }

    override fun setLoadingIndicator(loading: Boolean) {
        swipeRefreshLayout.post { swipeRefreshLayout.isRefreshing = loading }
    }

    override fun showError() {
        Toast.makeText(activity, "Error.", Toast.LENGTH_LONG).show()
    }

    override fun showNoInternetConnection() {
        Toast.makeText(activity, "No Internet", Toast.LENGTH_LONG).show()
        // TODO: consider showing a SnackBar here
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
