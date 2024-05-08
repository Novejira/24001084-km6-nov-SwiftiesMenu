package com.berkah.swiftiesmenu.feature.presentation.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.berkah.swiftiesmenu.R
import com.berkah.swiftiesmenu.databinding.FragmentCartBinding
import com.berkah.swiftiesmenu.feature.data.model.Cart
import com.berkah.swiftiesmenu.feature.data.repository.CartRepository
import com.berkah.swiftiesmenu.feature.data.repository.UserRepository
import com.berkah.swiftiesmenu.feature.data.utils.GenericViewModelFactory
import com.berkah.swiftiesmenu.feature.data.utils.hideKeyboard
import com.berkah.swiftiesmenu.feature.data.utils.proceedWhen
import com.berkah.swiftiesmenu.feature.data.utils.toIndonesianFormat
import com.berkah.swiftiesmenu.feature.presentation.cart.common.CartListAdapter
import com.berkah.swiftiesmenu.feature.presentation.cart.common.CartListener
import com.berkah.swiftiesmenu.feature.presentation.checkout.CheckoutActivity
import com.berkah.swiftiesmenu.feature.presentation.login.LoginActivity
import org.koin.android.ext.android.get

class CartFragment : Fragment() {
    private var isLogin = false

    private lateinit var binding: FragmentCartBinding

    private val cartviewModel: CartViewModel by viewModels {
        val rp: CartRepository = get()
        val repo: UserRepository = get()
        GenericViewModelFactory.create(CartViewModel(rp, repo))
    }

    private val adapter: CartListAdapter by lazy {
        CartListAdapter(
            object : CartListener {
                override fun onPlusTotalItemCartClicked(cart: Cart) {
                    cartviewModel.increaseCart(cart)
                }

                override fun onMinusTotalItemCartClicked(cart: Cart) {
                    cartviewModel.decreaseCart(cart)
                }

                override fun onRemoveCartClicked(cart: Cart) {
                    cartviewModel.removeCart(cart)
                }

                override fun onUserDoneEditingNotes(cart: Cart) {
                    cartviewModel.setCartNotes(cart)
                    hideKeyboard()
                }
            },
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentCartBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        observeData()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.btnOrder.setOnClickListener {
            if (cartviewModel.isUserLoggedIn()) {
                isLogin = true
                navigateToCheckout()
            } else {
                navigateToLogin()
            }
        }
    }

    private fun navigateToCheckout() {
        startActivity(Intent(requireContext(), CheckoutActivity::class.java))
    }

    private fun navigateToLogin() {
        startActivity(Intent(requireContext(), LoginActivity::class.java))
    }

    private fun observeData() {
        cartviewModel.getAllCarts().observe(viewLifecycleOwner) { result ->
            result.proceedWhen(
                doOnLoading = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = true
                    binding.layoutState.tvError.isVisible = false
                    binding.rvCart.isVisible = false
                },
                doOnSuccess = {
                    binding.layoutState.root.isVisible = false
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = false
                    binding.rvCart.isVisible = true
                    result.payload?.let { (carts, totalPrice) ->
                        // set list cart data
                        adapter.submitData(carts)
                        binding.tvTotalPrice.text = totalPrice.toIndonesianFormat()
                    }
                },
                doOnError = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = result.exception?.message.orEmpty()
                    binding.rvCart.isVisible = false
                },
                doOnEmpty = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = getString(R.string.text_cart_is_empty)
                    binding.rvCart.isVisible = false
                    result.payload?.let { (carts, totalPrice) ->
                        binding.tvTotalPrice.text = totalPrice.toIndonesianFormat()
                    }
                },
            )
        }
    }

    private fun setupList() {
        binding.rvCart.adapter = this@CartFragment.adapter
    }
}
