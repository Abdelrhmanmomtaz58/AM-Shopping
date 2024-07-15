package com.momtaz.amshopping.fragment.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.momtaz.amshopping.R
import com.momtaz.amshopping.adapters.HomeViewpagerAdapter
import com.momtaz.amshopping.databinding.FragmentHomeBinding
import com.momtaz.amshopping.fragment.categories.AccessoryFragment
import com.momtaz.amshopping.fragment.categories.MaleFragment
import com.momtaz.amshopping.fragment.categories.FemaleFragment
import com.momtaz.amshopping.fragment.categories.OtherFragment
import com.momtaz.amshopping.fragment.categories.MainCategoryFragment
import com.momtaz.amshopping.fragment.categories.SportFragment

class HomeFragment: Fragment(R.layout.fragment_home) {
    private lateinit var binding:FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoriesFragment = arrayListOf<Fragment>(
            MainCategoryFragment(),
            MaleFragment(),
            FemaleFragment(),
            SportFragment(),
            AccessoryFragment(),
            OtherFragment()
        )
        binding.viewpagerHome.isUserInputEnabled = false
        val viewpager2Adapter = HomeViewpagerAdapter(categoriesFragment,childFragmentManager,lifecycle)
        binding.viewpagerHome.adapter = viewpager2Adapter
        TabLayoutMediator(binding.tabLayout,binding.viewpagerHome){tab , position ->
            when(position){
                0 -> tab.text = "Main"
                1 -> tab.text = "Male"
                2 -> tab.text = "Female"
                3 -> tab.text = "Sport"
                4 -> tab.text = "Accessory"
                5 -> tab.text = "Other"
            }
        }.attach()


    }
}