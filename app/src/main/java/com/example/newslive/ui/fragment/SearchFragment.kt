package com.example.newslive.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newslive.R
import com.example.newslive.adapters.MyAdapter
import com.example.newslive.constants.Resource
import com.example.newslive.constants.constants
import com.example.newslive.ui.NewsViewModel
import com.example.newslive.ui.activity.MainActivity
import com.example.newslive.ui.activity.MainActivity2
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment: Fragment(R.layout.frag_search) {

    val RVsearch = view?.findViewById<RecyclerView>(R.id.RVsearch)

    //val query = view?.findViewById<EditText>(R.id.ETsearch)
    private lateinit var viewModel: NewsViewModel
    val TAG = "Search News Fragment"
    private lateinit var myAdapter: MyAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ( activity as MainActivity).viewModel
        setupRecyclerView(view)
        val ET = view.findViewById<EditText>(R.id.ETsearch)


        myAdapter.setOnItemClickListener(object : MyAdapter.onItemClickListener {
            override fun onItemClicking(position: Int) {


                val article = myAdapter.differ.currentList[position]
                val intent = Intent(requireContext(), MainActivity2::class.java)
                intent.putExtra("article", article)
                startActivity(intent)
            }

        })


        var job : Job? = null
        ET?.addTextChangedListener {editable->
            job?.cancel()
            job = MainScope().launch {
                delay(600L)
                editable?.let {
                    if ( editable.toString().isNotEmpty()){
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }


        viewModel.searchNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data.let { newsResponse ->
                        myAdapter.differ.submitList(newsResponse?.articles)
//                        val totalPage = newsResponse!!.totalResults/ (constants.Page_Count + 2)
//                        isLastPage = viewModel.searchNewsPage == totalPage
//                        if ( isLastPage){
//                            RVsearch?.setPadding(0,0,0,0)
//                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }

                else -> {}
            }
        }

    }
    private fun hideProgressBar(){
        view?.findViewById<ProgressBar>(R.id.paginationProgressBar)?.visibility   = View.INVISIBLE
        //var isLoading = false
    }
    private fun showProgressBar(){
        view?.findViewById<ProgressBar>(R.id.paginationProgressBar)?.visibility   = View.VISIBLE
        //var isLoading = true
    }


//    var isLoading = false
//    var isLastPage = false
//    var isScrolling = false
//
//    val scroll = object : RecyclerView.OnScrollListener(){
//        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//            super.onScrollStateChanged(recyclerView, newState)
//            if ( newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
//                isScrolling = true
//            }
//        }
//
//        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//            super.onScrolled(recyclerView, dx, dy)
//            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
//            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
//            val visibleItemCount = layoutManager.childCount
//            val totalCount = layoutManager.itemCount
//
//
//            val isNotLoadingAndNotAtLastPage = !isLoading && !isLastPage
//            val isAtLastItem = firstVisibleItemPosition+ visibleItemCount >= totalCount
//            val isNotAtBeginig = firstVisibleItemPosition >= 0
//            val isTotalMoreThanVisible = totalCount>= constants.Page_Count
//
//            val paginateOrNot  = isNotLoadingAndNotAtLastPage &&
//                    isAtLastItem && isNotAtBeginig && isTotalMoreThanVisible && isScrolling
//            if ( paginateOrNot ){
//                viewModel.searchNews(query?.text.toString())
//                isScrolling = false
//            }
//        }
//
//    }
    private fun setupRecyclerView(view: View) {
        myAdapter = MyAdapter()
        val rvSearchNews = view.findViewById<RecyclerView>(R.id.RVsearch)

        rvSearchNews.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(activity)
           // addOnScrollListener(this@SearchFragment.scroll)
        }
    }
}