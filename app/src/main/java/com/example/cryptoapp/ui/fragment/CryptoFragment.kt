package com.example.cryptoapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoapp.R
import com.example.cryptoapp.adapter.CryptoSearchAdapter
import com.example.cryptoapp.databinding.FragmentCryptoBinding
import com.example.cryptoapp.model.CryptoData
import com.example.cryptoapp.ui.CryptoViewModel
import com.example.cryptoapp.ui.MainActivity
import com.example.cryptoapp.util.Resource


class CryptoFragment : Fragment() {

    lateinit var binding: FragmentCryptoBinding
    lateinit var viewModel: CryptoViewModel
    lateinit var cryptoSearchAdapter: CryptoSearchAdapter
    private val filterCrypto = mutableListOf<CryptoData>()
    private var originalCrypto = mutableListOf<CryptoData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCryptoBinding.inflate(layoutInflater, container, false)
// setup recyclerview
        setupRecyclerView()

        /// searchView handle
        setupSearchView()

        viewModel = (activity as MainActivity).viewModel

        viewModel.cryptoData.observe(this, Observer { response ->
            when (response) {
                is Resource.Loading -> {
                    binding.loaderLottie.visibility = View.VISIBLE
                    binding.rvCryptoSearch.visibility = View.GONE
                }
                is Resource.Success -> {

                    binding.loaderLottie.visibility = View.GONE
                    binding.rvCryptoSearch.visibility = View.VISIBLE
                    response.data.let { data ->
                        originalCrypto = data as MutableList<CryptoData>
                        filterCrypto.clear()
                        filterCrypto.addAll(originalCrypto)
                        cryptoSearchAdapter.differ.submitList(filterCrypto)
                    }
                }
                is Resource.Error -> {
                    binding.loaderLottie.visibility = View.VISIBLE
                    binding.rvCryptoSearch.visibility = View.VISIBLE
//                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.btnMenuBack.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    private fun setupSearchView() {
        // Access the EditText inside the SearchView
        val searchEditText =
            binding.root.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)

        // Set the text color
        searchEditText.setTextColor(
            ContextCompat.getColor(
                binding.root.context,
                android.R.color.black
            )
        )

        // Set the hint color
        searchEditText.setHintTextColor(
            ContextCompat.getColor(
                binding.root.context,
                android.R.color.black
            )
        )
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