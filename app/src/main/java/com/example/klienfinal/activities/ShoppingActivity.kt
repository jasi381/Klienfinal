package com.example.klienfinal.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.klienfinal.R
import com.example.klienfinal.firebaseDatabase.FirebaseDb
import com.example.klienfinal.resource.Resource
import com.example.klienfinal.viewmodel.shopping.ShoppingViewModel
import com.example.klienfinal.viewmodel.shopping.ShoppingViewModelProviderFactory
import com.example.klienfinal.viewmodel.shopping.cart.CartViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val TAG = "ShoppingActivity"

class ShoppingActivity : AppCompatActivity() {

    val viewModel by lazy {
        val fDatabase = FirebaseDb()
        val providerFactory = ShoppingViewModelProviderFactory(fDatabase)
        ViewModelProvider(this, providerFactory)[ShoppingViewModel::class.java]
    }

    private lateinit var cartViewModel: CartViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping)



        cartViewModel = CartViewModel()
//        supportActionBar!!.hide()

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController = Navigation.findNavController(this, R.id.host_fragment)
        NavigationUI.setupWithNavController(bottomNavigation, navController)

        observeCartProductsCount(bottomNavigation)
    }


    private fun observeCartProductsCount(bottomNavigation: BottomNavigationView) {

        cartViewModel.cartItemsCount.observe(this, Observer { response ->
            when (response) {
                is Resource.Loading -> {
                    return@Observer
                }

                is Resource.Success -> {
                    if (response.data != 0)
                        bottomNavigation.getOrCreateBadge(R.id.cartFragment).apply {
                            backgroundColor = ContextCompat.getColor(this@ShoppingActivity, R.color.g_dark_blue)
                            number = response.data!!
                        }
                    else {
                        bottomNavigation.getOrCreateBadge(R.id.cartFragment).apply {
                            backgroundColor = ContextCompat.getColor(this@ShoppingActivity, R.color.g_white)
                            number = response.data
                        }
                    }
                    return@Observer
                }

                is Resource.Error -> {
                    Log.e(TAG, response.message.toString())
                    Toast.makeText(this, "Oops error occurred", Toast.LENGTH_SHORT).show()
                    return@Observer
                }
            }
        })
    }


}