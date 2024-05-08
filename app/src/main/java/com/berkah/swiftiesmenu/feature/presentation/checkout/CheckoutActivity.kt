package com.berkah.swiftiesmenu.feature.presentation.checkout

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.berkah.swiftiesmenu.R
import com.berkah.swiftiesmenu.databinding.ActivityCheckoutBinding
import com.berkah.swiftiesmenu.feature.data.datasource.cart.CartDataSource
import com.berkah.swiftiesmenu.feature.data.datasource.cart.CartDatabaseDataSource
import com.berkah.swiftiesmenu.feature.data.datasource.menu.MenuApiDataSource
import com.berkah.swiftiesmenu.feature.data.datasource.menu.MenuDataSource
import com.berkah.swiftiesmenu.feature.data.repository.CartRepository
import com.berkah.swiftiesmenu.feature.data.repository.CartRepositoryImpl
import com.berkah.swiftiesmenu.feature.data.repository.MenuRepository
import com.berkah.swiftiesmenu.feature.data.repository.MenuRepositoryImpl
import com.berkah.swiftiesmenu.feature.data.source.local.database.AppDatabase
import com.berkah.swiftiesmenu.feature.data.source.network.services.SwiftiesMenuApiService
import com.berkah.swiftiesmenu.feature.presentation.cart.common.CartListAdapter
import com.berkah.swiftiesmenu.feature.presentation.checkout.adapter.PriceListAdapter
import feature.utils.GenericViewModelFactory
import feature.utils.proceedWhen
import feature.utils.toIndonesianFormat

class CheckoutActivity : AppCompatActivity() {

    private val binding: ActivityCheckoutBinding by lazy {
        ActivityCheckoutBinding.inflate(layoutInflater)
    }
    private val viewModel: CheckoutViewModel by viewModels {
        val db = AppDatabase.getInstance(this)
        val s = SwiftiesMenuApiService.invoke()
        val pds: MenuDataSource = MenuApiDataSource(s)
        val pr: MenuRepository = MenuRepositoryImpl(pds)
        val ds: CartDataSource = CartDatabaseDataSource(db.cartDao())
        val rp: CartRepository = CartRepositoryImpl(ds)
        GenericViewModelFactory.create(CheckoutViewModel(rp,pr))
    }

    private val adapter: CartListAdapter by lazy {
        CartListAdapter()
    }
    private val priceItemAdapter: PriceListAdapter by lazy {
        PriceListAdapter {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupList()
        observeData()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnOrder.setOnClickListener {
            doCheckout()
        }
    }

    private fun doCheckout() {
        viewModel.checkoutCart().observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.layoutState.root.isVisible = false
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = false
                    binding.layoutContent.root.isVisible = true
                    binding.layoutContent.rvCart.isVisible = true
                    viewModel.deleteAllCart()
                    showSuccessDialog()
                },
                doOnLoading = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = true
                    binding.layoutState.tvError.isVisible = false
                    binding.layoutContent.root.isVisible = false
                    binding.layoutContent.rvCart.isVisible = false
                },
                doOnError = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutContent.root.isVisible = false
                    binding.layoutContent.rvCart.isVisible = false
                    Toast.makeText(this,
                        getString(R.string.error_checkout),
                        Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun showSuccessDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.create_order_success))
            .setPositiveButton(getString(R.string.close)) { _, _ ->
                finish()
            }
        dialog.show()
    }

    private fun setupList() {
        binding.layoutContent.rvCart.adapter = adapter
        binding.layoutContent.rvShoppingSummary.adapter = priceItemAdapter
    }

    private fun observeData() {
        viewModel.checkoutData.observe(this) { result ->
            result.proceedWhen(doOnSuccess = {
                binding.layoutState.root.isVisible = false
                binding.layoutState.pbLoading.isVisible = false
                binding.layoutState.tvError.isVisible = false
                binding.layoutContent.root.isVisible = true
                binding.layoutContent.rvCart.isVisible = true
                binding.cvSectionOrder.isVisible = true
                result.payload?.let { (carts, priceItems, totalPrice) ->
                    adapter.submitData(carts)
                    binding.tvTotalPrice.text = totalPrice.toIndonesianFormat()
                    priceItemAdapter.submitData(priceItems)
                }
            }, doOnLoading = {
                binding.layoutState.root.isVisible = true
                binding.layoutState.pbLoading.isVisible = true
                binding.layoutState.tvError.isVisible = false
                binding.layoutContent.root.isVisible = false
                binding.layoutContent.rvCart.isVisible = false
                binding.cvSectionOrder.isVisible = false
            }, doOnError = {
                binding.layoutState.root.isVisible = true
                binding.layoutState.pbLoading.isVisible = false
                binding.layoutState.tvError.isVisible = true
                binding.layoutState.tvError.text = result.exception?.message.orEmpty()
                binding.layoutContent.root.isVisible = false
                binding.layoutContent.rvCart.isVisible = false
                binding.cvSectionOrder.isVisible = false
            }, doOnEmpty = { data ->
                binding.layoutState.root.isVisible = true
                binding.layoutState.pbLoading.isVisible = false
                binding.layoutState.tvError.isVisible = true
                binding.layoutState.tvError.text = getString(R.string.text_cart_is_empty)
                data.payload?.let { (_, _, totalPrice) ->
                    binding.tvTotalPrice.text = totalPrice.toIndonesianFormat()
                }
                binding.layoutContent.root.isVisible = false
                binding.layoutContent.rvCart.isVisible = false
                binding.cvSectionOrder.isVisible = false
            })
        }
    }
}
