package com.sawthandar.newsapp.ui.newsDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.sawthandar.newsapp.R
import com.sawthandar.newsapp.databinding.FragmentNewsDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsDetailFragment: Fragment() {

    private lateinit var binding: FragmentNewsDetailBinding

    private val args: NewsDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNewsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpListener()
        setUpDataObservation()
    }

    private fun setUpListener() {
        binding.backImage.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setUpDataObservation() {
        val article = args.newsArticle

        binding.detailsTitle.text = article.source?.name ?: ""
        binding.newsTitle.text = article.title ?: ""

        Glide.with(requireContext())
            .load(article.urlToImage)
            .placeholder(R.drawable.news_logo)
            .into(binding.newsImage)

        binding.newsContent.text = article.description ?: ""
        binding.newsSource.text = article.source?.name ?: ""
    }
}