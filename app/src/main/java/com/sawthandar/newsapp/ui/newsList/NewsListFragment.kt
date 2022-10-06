package com.sawthandar.newsapp.ui.newsList

import android.os.Bundle
import android.view.LayoutInflater
import com.sawthandar.newsapp.framework.vos.ArticlesListVO
import com.sawthandar.newsapp.framework.vos.NewsListVO

import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sawthandar.newsapp.R
import com.sawthandar.newsapp.adapters.news_list.NewsListItemAdapter
import com.sawthandar.newsapp.databinding.FragmentNewsListBinding
import com.sawthandar.newsapp.ui.BaseFragment
import com.sawthandar.newsapp.util.getBundleNewsDetail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsListFragment : BaseFragment() {

    private lateinit var binding: FragmentNewsListBinding

    private lateinit var newsListViewModel: NewsListViewModel

    private lateinit var newsListItemAdapter: NewsListItemAdapter

    var isListEnd = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNewsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewModel()
        setUpRecyclerView()
        setUpListener()
        setUpOnUIReady()
        setUpDataObservation()
    }

    private fun setUpViewModel() {
        newsListViewModel = ViewModelProviders.of(this)[NewsListViewModel::class.java]
    }

    private fun setUpRecyclerView() {
        newsListItemAdapter = NewsListItemAdapter(newsListViewModel)
        binding.newsRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.newsRecyclerview.adapter = newsListItemAdapter
    }

    private fun setUpListener() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            setUpOnUIReady()
        }

        binding.searchLayout.setOnClickListener {
            findNavController().navigate(R.id.action_newsListFragment_to_searchFragment)
        }

        binding.newsRecyclerview.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = binding.newsRecyclerview.layoutManager?.childCount
                val totalItemCount = binding.newsRecyclerview.layoutManager?.itemCount
                val pastVisibleItems = (binding.newsRecyclerview.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                if (visibleItemCount != null) {
                    if (visibleItemCount + pastVisibleItems < totalItemCount!!) {
                        isListEnd = false
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE && (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() == recyclerView.adapter!!.itemCount - 1 && !isListEnd) {
                    isListEnd = true
                    newsListViewModel.loadMoreNewsList()
                }
            }
        })
    }

    private fun setUpOnUIReady() {
        newsListViewModel.onUIReady()
    }

    private fun setUpDataObservation() {
        newsListViewModel.getNewsListFromDatabase().observe(viewLifecycleOwner) { it ->
            it?.let { news ->

                val mNewsList: MutableList<NewsListVO> = mutableListOf()
                val mArticleList: MutableList<ArticlesListVO> = mutableListOf()

                mNewsList.addAll(news.distinctBy { it.id })

                mNewsList.forEach { news ->
                    news.articles?.let { article ->
                        mArticleList.addAll(article.distinctBy { it.description })
                    }
                }

                newsListItemAdapter.setNewData(mArticleList)

            }
        }

        newsListViewModel.navigateSearchDataToDetails().observe(viewLifecycleOwner) { article ->
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                article?.let { article ->
                    findNavController().navigate(
                        R.id.action_newsListFragment_to_newsDetailFragment,
                        getBundleNewsDetail(article)
                    )
                }
            }
        }

        newsListViewModel.getShowOrHideProgress().observe(viewLifecycleOwner) {
            it?.let { data ->
                if (data == 1) {
                    showProgressDialog()
                } else {
                    hideProgressDialog()
                }
            }
        }

        newsListViewModel.getErrorMessage().observe(viewLifecycleOwner) {
            it?.let { errorMessage ->

                var mErrorMessage = ""

                if (errorMessage.contains("HTTP 429") || errorMessage.contains("HTTP 426")) {
                    mErrorMessage = "rateLimited"
                }

                if (mErrorMessage != "") {
                    binding.newsRecyclerview.visibility = View.GONE
                    binding.noResultLayout.visibility = View.VISIBLE
                    binding.errorMsg.text = mErrorMessage
                } else {
                    binding.newsRecyclerview.visibility = View.VISIBLE
                    binding.noResultLayout.visibility = View.GONE
                }

            }
        }

        newsListViewModel.getErrorMessageMore().observe(viewLifecycleOwner) {
            it?.let { errorMessageMore ->
                var mErrorMessageMore = ""
                if (errorMessageMore.contains("HTTP 429") || errorMessageMore.contains("HTTP 426")) {
                    mErrorMessageMore = "maximumResultsReached"
                }

                if (errorMessageMore != "") {
                    Toast.makeText(context, mErrorMessageMore, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}