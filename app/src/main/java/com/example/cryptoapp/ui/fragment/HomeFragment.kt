package com.example.cryptoapp.ui.fragment

import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.cryptoapp.R
import com.example.cryptoapp.adapter.CryptoAdapter
import com.example.cryptoapp.databinding.FragmentHomeBinding
import com.example.cryptoapp.ui.CryptoViewModel
import com.example.cryptoapp.ui.MainActivity
import com.example.cryptoapp.util.CryptoApplication
import com.example.cryptoapp.util.Resource
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import javax.inject.Inject

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    lateinit var cryptoAdapter: CryptoAdapter
    private lateinit var viewModel: CryptoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        viewModel = (activity as MainActivity).viewModel

        /// countDown setup
        binding.countDown.start(1000 * 60 * 3)
        binding.countDown.setOnCountdownEndListener {
            binding.countDown.start(1000 * 60 * 3)
        }

        // setup recyclerview
        setupRecyclerView()
        /// Setup image Slider
        setupImageSlider()
        // setup SwipeRefreshLayout
        setupSwipeRefreshLayout()

        //Observe Data
        observeData()

        // click handle
        binding.btnSeeAll.setOnClickListener {
            val bottomSheetDialog = CryptoBottomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager, "Test")
        }
        binding.btnNotification.setOnClickListener {
            val bottomSheetDialog = NotificationBottomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager, "Test")
        }

        return binding.root
    }

    private fun observeData() {
        /// login observe
        viewModel.checkLogin.observe(this, Observer {
            if (it) {
                binding.tvUserName.text = "Hello \n" + viewModel.loadUsername()
                binding.imgUser.setImageBitmap(viewModel.loadImage())
            } else {
                val bottomSheetDialog = UserOnboardingFragment()
                bottomSheetDialog.show(parentFragmentManager, "Test")
            }
        })

        //// cryptoData
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
                    binding.loaderLottie.visibility = View.VISIBLE
                    binding.rvCrypto.visibility = View.GONE
                    Toast.makeText(binding.root.context, response.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

        // refresh time
        viewModel.refreshTime.observe(this, Observer { response ->
            binding.tvRefreshTime.text = response
        })

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
            layoutManager = LinearLayoutManager(binding.root.context)
        }
    }



}