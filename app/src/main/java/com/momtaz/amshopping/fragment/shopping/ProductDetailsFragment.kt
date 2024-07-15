package com.momtaz.amshopping.fragment.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.momtaz.amshopping.R
import com.momtaz.amshopping.adapters.ColorsAdapter
import com.momtaz.amshopping.adapters.SizesAdapter
import com.momtaz.amshopping.adapters.ViewPager2Images
import com.momtaz.amshopping.data.CartProduct
import com.momtaz.amshopping.databinding.FragmentProductDetailsBinding
import com.momtaz.amshopping.util.Resource
import com.momtaz.amshopping.util.hideBottomNavigationView
import com.momtaz.amshopping.viewmodel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProductDetailsFragment:Fragment() {
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding :FragmentProductDetailsBinding
    private val viewpagerAdapter by lazy { ViewPager2Images() }
    private val sizesAdapter by lazy {SizesAdapter() }
    private val colorAdapter by lazy { ColorsAdapter() }
    private var selectedColor :Int? =null
    private var selectedSize :String? = null
    private val viewModel by viewModels<DetailsViewModel>()

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

        sizesAdapter.onItemClick ={
         selectedSize =it
        }

        colorAdapter.onItemClick = {
            selectedColor = it
        }

        binding.buttonAddToCart.setOnClickListener {
            viewModel.addUpdateProductInCart(CartProduct(product,1,selectedColor,selectedSize))
        }
        lifecycleScope.launchWhenStarted {
            viewModel.addToCart.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.buttonAddToCart.startAnimation()

                    }
                    is Resource.Success ->{
                        binding.buttonAddToCart.revertAnimation()
                        binding.buttonAddToCart.setBackgroundColor(resources.getColor(R.color.black))
                        Toast.makeText(requireContext(),"product add in cart ✓✓",Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Error ->{
                        binding.buttonAddToCart.stopAnimation()
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
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
            indicator.text = "1 / ${viewpagerAdapter.differ.currentList.size} "

            viewPagerProductImages.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    indicator.text= "${position + 1} / ${viewpagerAdapter.differ.currentList.size}"
                }
            })
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