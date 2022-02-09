package com.neosoft.testapplication.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.neosoft.testapplication.R
import com.neosoft.testapplication.adapters.FruitListAdapter
import com.neosoft.testapplication.carouselview.ImageListener
import com.neosoft.testapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var adapter: FruitListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupViews()
        observeViews()
    }

    private fun observeViews() {
        viewModel.displayList.observe(this, {
            adapter.setData(it)
        })
    }

    private fun setupViews() {

        activityMainBinding.carouselView.pageCount = viewModel.sampleImages.size
        activityMainBinding.carouselView.setImageListener(imageListener)
        activityMainBinding.carouselView.addOnPageChangeListener(pageChangeListener)

        activityMainBinding.searchView.setOnCloseListener {
            viewModel.currentPosition = (activityMainBinding.carouselView.currentItem)
            false
        }
        activityMainBinding.searchView.setOnQueryTextListener(onQueryTextListener)

        activityMainBinding.recyclerview.layoutManager = LinearLayoutManager(this)
        adapter = FruitListAdapter()
        activityMainBinding.recyclerview.adapter = adapter
    }


    private val imageListener =
        ImageListener { position, imageView ->
            imageView.setImageResource(viewModel.sampleImages[position])
        }


    private val pageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            activityMainBinding.searchView.setQuery("", true)
            viewModel.currentPosition = position
        }

        override fun onPageScrollStateChanged(state: Int) {
        }

    }

    private val onQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            if (newText != null) {
                viewModel.filterData(newText)
            }

            return false
        }
    }
}