package com.sawthandar.newsapp.ui.search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sawthandar.newsapp.R
import com.sawthandar.newsapp.adapters.news_search_list.NewsSearchItemAdapter
import com.sawthandar.newsapp.databinding.FragmentNewsSearchBinding
import com.sawthandar.newsapp.framework.vos.ArticlesListVO
import com.sawthandar.newsapp.ui.BaseFragment
import com.sawthandar.newsapp.util.getBundleNewsDetail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsSearchListFragment : BaseFragment() {

    private lateinit var binding: FragmentNewsSearchBinding

    private lateinit var newsSearchListViewModel: NewsSearchListViewModel

    private lateinit var newsSearchItemAdapter: NewsSearchItemAdapter

    var isListEnd = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewsSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewModel()
        setUpRecyclerView()
        setUpListener()

        setUpDataObservation()
    }

    private fun setUpViewModel() {
        newsSearchListViewModel = ViewModelProviders.of(this)[NewsSearchListViewModel::class.java]
    }

    private fun setUpRecyclerView() {
        newsSearchItemAdapter = NewsSearchItemAdapter(newsSearchListViewModel)
        binding.newsSearchRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.newsSearchRecyclerview.adapter = newsSearchItemAdapter
    }

    private fun setUpListener() {

        binding.backImage.setOnClickListener {
            findNavController().popBackStack()
        }

        val handler = Handler(Looper.getMainLooper())
        binding.searchEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                binding.materialLoader.visibility = View.VISIBLE

                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    s?.toString()?.let { searchWord ->
                        binding.materialLoader.visibility = View.GONE
                        onChangeTextAfterSecond(searchWord)
                    }
                }, 600)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })
    }

    private fun onChangeTextAfterSecond(searchWord: String) {
        newsSearchListViewModel.loadSearchNews(searchWord)

        binding.newsSearchRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = binding.newsSearchRecyclerview.layoutManager!!.childCount
                val totalItemCount = binding.newsSearchRecyclerview.layoutManager!!.itemCount
                val pastVisibleItems = (binding.newsSearchRecyclerview.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                if (visibleItemCount + pastVisibleItems < totalItemCount) {
                    isListEnd = false
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && (recyclerView.layoutManager as LinearLayoutManager)
                        .findLastCompletelyVisibleItemPosition() == recyclerView.adapter!!.itemCount - 1
                    && !isListEnd
                ) {
                    isListEnd = true
                    newsSearchListViewModel.loadMoreSearchNews(searchWord)

                }
            }

        })
    }

    private fun setUpDataObservation() {

        newsSearchListViewModel.getSearchNewsList().observe(viewLifecycleOwner) {
            it?.let { articleList ->
                binding.newsSearchRecyclerview.visibility = View.VISIBLE
                binding.noResultLayout.visibility = View.GONE
                newsSearchItemAdapter.setNewData(articleList as MutableList<ArticlesListVO>)
            }
        }

        newsSearchListViewModel.getNavigateSearchListToDetailData().observe(viewLifecycleOwner) { article ->
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                article?.let { article ->
                    findNavController().navigate(
                        R.id.action_searchFragment_to_newsDetailFragment,
                        getBundleNewsDetail(article)
                    )
                }
            }
        }

        newsSearchListViewModel.getShowOrHideProgress().observe(viewLifecycleOwner) {
            it?.let { data ->
                if (data == 1) {
                    showProgressDialog()
                } else {
                    hideProgressDialog()
                }
            }
        }

        newsSearchListViewModel.getErrorMessage().observe(viewLifecycleOwner) {
            it?.let { errorMessage ->

                var mErrorMessage: String = ""

                if (errorMessage.contains("HTTP 429") || errorMessage.contains("HTTP 426")) {
                    mErrorMessage = "rateLimited"
                }

                if (mErrorMessage != "") {
                    binding.newsSearchRecyclerview.visibility = View.GONE
                    binding.noResultLayout.visibility = View.VISIBLE
                    binding.errorMsg.text = mErrorMessage
                } else {
                    binding.newsSearchRecyclerview.visibility = View.VISIBLE
                    binding.noResultLayout.visibility = View.GONE
                }

            }
        }

        newsSearchListViewModel.getErrorMessageMore().observe(viewLifecycleOwner) {
            it?.let { errorMessageMore ->
                var mErrorMessageMore: String = ""
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