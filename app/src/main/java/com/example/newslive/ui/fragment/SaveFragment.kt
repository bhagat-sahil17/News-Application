package com.example.newslive.ui.fragment


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newslive.R
import com.example.newslive.adapters.MyAdapter
import com.example.newslive.ui.NewsViewModel
import com.example.newslive.ui.activity.MainActivity
import com.example.newslive.ui.activity.MainActivity2
import com.google.android.material.snackbar.Snackbar


class SaveFragment: Fragment(R.layout.frag_save) {
    lateinit var viewModel: NewsViewModel
    private lateinit var myAdapter: MyAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)

        val rvSaveNews = view.findViewById<RecyclerView>(R.id.RVsave)


        viewModel = ( activity as MainActivity).viewModel
        setupRecyclerView(rvSaveNews)

        myAdapter.setOnItemClickListener(object : MyAdapter.onItemClickListener {
            override fun onItemClicking(position: Int) {
                val article = myAdapter.differ.currentList[position]
                val intent = Intent(requireContext(), MainActivity2::class.java)
                intent.putExtra("article", article)
                startActivity(intent)
            }
        })

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = myAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(view,"News removed from save",Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo"){
                        viewModel.saveArticle(article)
                    }
                }.show()
            }

        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rvSaveNews)
        }

        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer {newArticleList->
            myAdapter.differ.submitList(newArticleList)
        })
    }

    private fun setupRecyclerView(view: View) {
        myAdapter = MyAdapter()


        val rvSaveNews = view.findViewById<RecyclerView>(R.id.RVsave)
        rvSaveNews.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}