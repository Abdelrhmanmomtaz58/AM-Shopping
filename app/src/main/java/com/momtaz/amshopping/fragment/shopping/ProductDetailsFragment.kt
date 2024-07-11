package com.momtaz.amshopping.fragment.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.momtaz.amshopping.R
import com.momtaz.amshopping.activites.ShoppingActivity
import com.momtaz.amshopping.adapters.ColorsAdapter
import com.momtaz.amshopping.adapters.SizesAdapter
import com.momtaz.amshopping.adapters.ViewPager2Images
import com.momtaz.amshopping.databinding.FragmentProductDetailsBinding
import com.momtaz.amshopping.util.hideBottomNavigationView

class ProductDetailsFragment:Fragment() {
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding :FragmentProductDetailsBinding
    private val viewpagerAdapter by lazy { ViewPager2Images() }
    private val sizesAdapter by lazy {SizesAdapter() }
    private val colorAdapter by lazy { ColorsAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       hideBottomNavigationView()
        binding = FragmentProductDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        setupSizeRv()
        setupColorRv()
        setupViewpager()
        binding.imageClose.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.apply {
            tvProductName.text =product.name
            tvProductPrice.text = "$ ${product.price}"
            tvProductDescription.text = product.description

            if (product.colors.isNullOrEmpty())
                tvProductColors.visibility = View.INVISIBLE
            if (product.sizes.isNullOrEmpty())
                tvProductSize.visibility = View.INVISIBLE

        }
        viewpagerAdapter.differ.submitList(product.images)
        product.colors?.let {
            colorAdapter.differ.submitList(it)
        }
        product.sizes?.let {
            sizesAdapter.differ.submitList(it)
        }

    }

    private fun setupViewpager() {
        binding.apply {
            viewPagerProductImages.adapter = viewpagerAdapter
        }
    }

    private fun setupColorRv() {
        binding.rvColors.apply {
            adapter = colorAdapter
            layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupSizeRv() {
        binding.rvSizes.apply {
            adapter = sizesAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }
}