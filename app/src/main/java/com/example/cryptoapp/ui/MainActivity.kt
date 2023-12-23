package com.example.cryptoapp.ui

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.cryptoapp.R
import com.example.cryptoapp.adapter.CryptoAdapter
import com.example.cryptoapp.databinding.ActivityMainBinding
import com.example.cryptoapp.repository.CryptoRepository
import com.example.cryptoapp.util.Resource
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: CryptoViewModel
    lateinit var cryptoAdapter: CryptoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // off screen oration
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Stop night mode and follow the system setting
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_UNSPECIFIED)

        // setup recyclerview
        setupRecyclerView()
        /// Setup image Slider
        setupImageSlider()
        // setup SwipeRefreshLayout
        setupSwipeRefreshLayout()

        /// setup viewModel
        val newsRepository = CryptoRepository()
        val viewModelProviderFactory = CryptoViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[CryptoViewModel::class.java]

        viewModel.cryptoData.observe(this, Observer { response ->
            when (response) {
                is Resource.Loading -> {
                    binding.loaderLottie.visibility = View.VISIBLE
                    binding.rvCrypto.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.loaderLottie.visibility = View.GONE
                    binding.rvCrypto.visibility = View.VISIBLE
                    response.data.let { data ->
                        cryptoAdapter.differ.submitList(data)
                    }
                }
                is Resource.Error -> {
                    binding.loaderLottie.visibility = View.GONE
                    binding.rvCrypto.visibility = View.VISIBLE
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.refreshTime.observe(this, Observer { response ->
            binding.tvRefreshTime.text = response
        })


        binding.btnSeeAll.setOnClickListener {
            val bottomSheetDialog = CryptoBottomSheetFragment()
            bottomSheetDialog.show(supportFragmentManager, "Test")
        }
    }

    private fun setupSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.rvCrypto.visibility = View.GONE
            viewModel.fetchData()
            binding.swipeRefreshLayout.isRefreshing = false
        }

    }

    private fun setupImageSlider() {
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.bar1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.bar2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.bar3, ScaleTypes.FIT))
        binding.imageSlider.setImageList(imageList, ScaleTypes.FIT)
    }

    private fun setupRecyclerView() {
        cryptoAdapter = CryptoAdapter()
        binding.rvCrypto.apply {
            adapter = cryptoAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

}