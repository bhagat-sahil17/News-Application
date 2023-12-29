package com.example.newslive.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.pdf.PdfDocument.Page
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newslive.R
import com.example.newslive.adapters.MyAdapter
import com.example.newslive.constants.constants.Companion.Page_Count
import com.example.newslive.models.Article
import com.example.newslive.ui.NewsViewModel
import com.example.newslive.ui.activity.MainActivity
import com.example.newslive.ui.activity.MainActivity2


//https://gnews.io/api/v4/top-headlines?category=general&lang=en&country=us&max=10&apikey=5d2c31c3a46b3c6ace50ab8ee6f7d60f
class HomeFragment : Fragment(R.layout.frag_home) {



    val RVfrag = view?.findViewById<RecyclerView>(R.id.RVhfrag)


    private lateinit var myAdapter: MyAdapter

    lateinit var viewModel: NewsViewModel
    private val TAG = "Breaking News Fragment"

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        viewModel = ( activity as MainActivity).viewModel
        setupRecyclerView(view)




        myAdapter.setOnItemClickListener(object : MyAdapter.onItemClickListener {
            override fun onItemClicking(position: Int) {

                val article = myAdapter.differ.currentList[position]
                val intent = Intent(requireContext(), MainActivity2::class.java)
                intent.putExtra("article", article)
                startActivity(intent)

            }
        })

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response->
            when(response){
                is com.example.newslive.constants.Resource.Success->{
                    hideProgressBar()
                    response.data.let {newsResponse ->
                        myAdapter.differ.submitList(newsResponse?.articles?.toList())
                        val totalPage = newsResponse!!.totalResults/ (Page_Count + 2)
                        isLastPage = viewModel.breakingNewsPage == totalPage
                        if ( isLastPage){
                            RVfrag?.setPadding(0,0,0,0)
                        }
                    }
                }
                is com.example.newslive.constants.Resource.Error->{
                    hideProgressBar()
                    response.message?.let {message->
                        Log.e(TAG,"An error occured: $message")
                    }
                }
                is com.example.newslive.constants.Resource.Loading->{
                    showProgressBar()
                }

            }
        })

    }

    private fun hideProgressBar(){
        view?.findViewById<ProgressBar>(R.id.paginationProgressBar)?.visibility   = View.INVISIBLE
        isLoading = false
    }
    private fun showProgressBar(){
        view?.findViewById<ProgressBar>(R.id.paginationProgressBar)?.visibility   = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scroll = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if ( newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalCount = layoutManager.itemCount


            val isNotLoadingAndNotAtLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition+ visibleItemCount >= totalCount
            val isNotAtBeginig = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalCount>= Page_Count

            val paginateOrNot  = isNotLoadingAndNotAtLastPage &&
                    isAtLastItem && isNotAtBeginig && isTotalMoreThanVisible && isScrolling
            if ( paginateOrNot ){
                viewModel.getBreakingNews("in")
                isScrolling = false
            }



        }
    }

    private fun setupRecyclerView(view: View) {
        myAdapter = MyAdapter()
        val rvBreakingNews = view.findViewById<RecyclerView>(R.id.RVhfrag)

        rvBreakingNews.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@HomeFragment.scroll)
        }

    }
}
