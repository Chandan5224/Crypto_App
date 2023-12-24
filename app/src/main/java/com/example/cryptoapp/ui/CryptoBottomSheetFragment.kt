package com.example.cryptoapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoapp.R
import com.example.cryptoapp.adapter.CryptoAdapter
import com.example.cryptoapp.adapter.CryptoSearchAdapter
import com.example.cryptoapp.databinding.FragmentCryptoBottomSheetBinding
import com.example.cryptoapp.model.CryptoData
import com.example.cryptoapp.util.Resource
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CryptoBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCryptoBottomSheetBinding
    lateinit var viewModel: CryptoViewModel
    lateinit var cryptoSearchAdapter: CryptoSearchAdapter
    private val filterCrypto = mutableListOf<CryptoData>()
    private var originalCrypto = mutableListOf<CryptoData>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCryptoBottomSheetBinding.inflate(layoutInflater)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        // setup recyclerview
        setupRecyclerView()

        /// searchView handle
        setupSearchView()

        viewModel = (activity as MainActivity).viewModel
        binding.btnMenuBack.setOnClickListener {
            dismiss()
        }

        viewModel.cryptoData.observe(this, Observer { response ->
            when (response) {
                is Resource.Loading -> {
                    binding.rvCryptoSearch.visibility = View.GONE
                    binding.loaderLottie.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.rvCryptoSearch.visibility = View.VISIBLE
                    binding.loaderLottie.visibility = View.GONE
                    response.data.let { data ->
                        originalCrypto = data as MutableList<CryptoData>
                        filterCrypto.clear()
                        filterCrypto.addAll(originalCrypto)
                        cryptoSearchAdapter.differ.submitList(filterCrypto)
                    }
                }
                is Resource.Error -> {
                    binding.rvCryptoSearch.visibility = View.VISIBLE
                    binding.loaderLottie.visibility = View.GONE
                }
            }
        })
        return binding.root
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    filterMenuItems(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    filterMenuItems(newText)
                }
                return true
            }

        })
    }

    private fun setupRecyclerView() {
        cryptoSearchAdapter = CryptoSearchAdapter()
        binding.rvCryptoSearch.apply {
            adapter = cryptoSearchAdapter
            layoutManager = LinearLayoutManager(binding.root.context)
        }
    }


    private fun filterMenuItems(query: String) {
        filterCrypto.clear()
        originalCrypto.forEachIndexed { index, crypto ->
            if (crypto.name_full.contains(query, ignoreCase = true)) {
                filterCrypto.add(crypto)
            }
        }
        cryptoSearchAdapter.notifyDataSetChanged()
    }

}