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
import androidx.recyclerview.widget.RecyclerView
import com.momtaz.amshopping.R
import com.momtaz.amshopping.adapters.AddressAdapter
import com.momtaz.amshopping.adapters.BillingProductsAdapter
import com.momtaz.amshopping.data.CartProduct
import com.momtaz.amshopping.databinding.FragmentBaseCategoryBinding
import com.momtaz.amshopping.databinding.FragmentBillingBinding
import com.momtaz.amshopping.util.HorizontalItemDecoration
import com.momtaz.amshopping.util.Resource
import com.momtaz.amshopping.viewmodel.BillingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
@AndroidEntryPoint
class BillingFragment : Fragment() {
    private lateinit var binding: FragmentBillingBinding
    private val addressAdapter by lazy { AddressAdapter() }
    private val billingProductsAdapter by lazy { BillingProductsAdapter() }
    private val viewModel by viewModels<BillingViewModel>()
    private val args by navArgs<BillingFragmentArgs>()
    private var products = emptyList<CartProduct>()
    private var totalPrice = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        products = args.products.toList()
        totalPrice = args.totalPrice
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBillingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBillingProductsRv()
        setupAddressRv()

        binding.imageAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }
        lifecycleScope.launchWhenStarted {
            viewModel.address.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.progressbarAddress.visibility =View.VISIBLE
                    }
                    is Resource.Success ->{
                        addressAdapter.differ.submitList(it.data)
                        binding.progressbarAddress.visibility =View.GONE
                    }
                    is Resource.Error ->{
                        binding.progressbarAddress.visibility =View.GONE
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()

                    }else -> Unit
                }
            }
        }
        billingProductsAdapter.differ.submitList(products)
        binding.tvTotalPrice.text= "$ $totalPrice"
    }

    private fun setupAddressRv() {
        binding.rvAddress.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = addressAdapter
            addItemDecoration(HorizontalItemDecoration())

        }
    }

    private fun setupBillingProductsRv() {
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = billingProductsAdapter
            addItemDecoration(HorizontalItemDecoration())
        }
    }

}