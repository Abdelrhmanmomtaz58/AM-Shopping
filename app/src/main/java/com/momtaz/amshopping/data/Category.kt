package com.momtaz.amshopping.data

sealed class Category(val category: String) {

    object Male: Category("Male")
    object Female: Category("Female")
    object Sport: Category("Sport")
    object Accessory: Category("Accessory")
    object Other: Category("Other")
}