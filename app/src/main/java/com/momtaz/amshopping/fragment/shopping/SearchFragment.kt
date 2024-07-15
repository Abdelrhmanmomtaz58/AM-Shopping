package com.momtaz.amshopping.fragment.shopping

import SearchProductAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.momtaz.amshopping.R
import com.momtaz.amshopping.data.Product
import com.momtaz.amshopping.databinding.FragmentSearchBinding
import com.momtaz.amshopping.util.HorizontalItemDecoration
import com.momtaz.amshopping.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var searchAdapter: SearchProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchView()

        viewModel.products.observe(viewLifecycleOwner,Observer<List<Product>> { productList ->
            searchAdapter.submitList(productList)
        })
        searchAdapter.onClick = {searchProduct ->
            navigateToProductDetails(searchProduct)

        }
    }

    private fun setupRecyclerView() {
        searchAdapter = SearchProductAdapter()
        binding.rvSearchProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            addItemDecoration(HorizontalItemDecoration())
            adapter = searchAdapter
        }
    }

    private fun setupSearchView() {
        binding.edSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchProducts(it)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.searchProducts(it)
                }
                return false
            }
        })
    }
    private fun navigateToProductDetails(product: Product) {
        val action = SearchFragmentDirections.actionSearchFragmentToProductDetailsFragment(product)
        findNavController().navigate(action)
    }
}
