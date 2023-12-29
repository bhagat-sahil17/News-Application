package com.example.newslive.ui.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.newslive.R
import com.example.newslive.database.ArticleDataBase
import com.example.newslive.repository.NewsRepository
import com.example.newslive.ui.NewsViewModel
import com.example.newslive.ui.NewsViewModelProviderFactory
import com.example.newslive.ui.fragment.HomeFragment
import com.example.newslive.ui.fragment.SaveFragment
import com.example.newslive.ui.fragment.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.provider.Settings


class MainActivity : AppCompatActivity() {


    lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fetchData()



    }
    override fun onResume() {
        super.onResume()
        if (isNetworkAvailable()) {
            // The user is back from settings and has connectivity
            // Restart your data fetching logic or other necessary tasks here
            fetchData()
        }
    }
    private fun replaceFragment(fragment: Fragment ) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.FL,fragment)
        fragmentTransaction.commit()
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    // Method to prompt user to enable connectivity
    private fun promptForConnectivity() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("No Internet Connection")
            .setMessage("Please connect to mobile data or Wi-Fi to use the app.")
            .setPositiveButton("Go to Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(intent)
            }
            .setNegativeButton("Close App") { _, _ ->
                finish() // Close the app
            }
            .show()
    }

    // Check connectivity before fetching data
    private fun fetchData() {
        if (isNetworkAvailable()) {
            val newsRepository = NewsRepository(ArticleDataBase(this))
            val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)

            viewModel = ViewModelProvider(this
                ,viewModelProviderFactory).get(NewsViewModel::class.java)

            val bottomNavView: BottomNavigationView = findViewById(R.id.bottomNavigationView)


            replaceFragment(HomeFragment())

            bottomNavView.setOnItemSelectedListener {
                when(it.itemId)
                {
                    R.id.btnHome->{

                        replaceFragment(HomeFragment())
                    }

                    R.id.btnSave->replaceFragment(SaveFragment())
                    R.id.btnSearch->replaceFragment(SearchFragment())
                    else->{

                    }

                }
                true

            }
        } else {
            // Prompt the user to connect
            promptForConnectivity()
        }
    }
}