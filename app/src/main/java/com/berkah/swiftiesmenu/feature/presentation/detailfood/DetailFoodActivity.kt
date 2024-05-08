package com.berkah.swiftiesmenu.feature.presentation.detailfood

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.berkah.swiftiesmenu.R
import com.berkah.swiftiesmenu.databinding.ActivityDetailFoodBinding
import com.berkah.swiftiesmenu.feature.data.datasource.cart.CartDataSource
import com.berkah.swiftiesmenu.feature.data.datasource.cart.CartDatabaseDataSource
import com.berkah.swiftiesmenu.feature.data.model.Menu
import com.berkah.swiftiesmenu.feature.data.repository.CartRepository
import com.berkah.swiftiesmenu.feature.data.repository.CartRepositoryImpl
import com.berkah.swiftiesmenu.feature.data.source.local.database.AppDatabase
import com.berkah.swiftiesmenu.feature.data.utils.GenericViewModelFactory
import com.berkah.swiftiesmenu.feature.data.utils.proceedWhen
import com.berkah.swiftiesmenu.feature.data.utils.toIndonesianFormat

class DetailFoodActivity : AppCompatActivity() {
    private val binding: ActivityDetailFoodBinding by lazy {
        ActivityDetailFoodBinding.inflate(layoutInflater)
    }
    private val viewModel: DetailFoodViewModel by viewModels {
        val db = AppDatabase.getInstance(this)
        val ds: CartDataSource = CartDatabaseDataSource(db.cartDao())
        val rp: CartRepository = CartRepositoryImpl(ds)
        GenericViewModelFactory.create(
            DetailFoodViewModel(intent?.extras, rp)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        bindMenu(viewModel.menu)
        setClickListener()
        observeData()
    }

    private fun setClickListener() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.ivMinus.setOnClickListener {
            viewModel.minus()
        }
        binding.ivPlus.setOnClickListener {
            viewModel.add()
        }
        binding.btnBuy.setOnClickListener {
            addMenuToCart()
        }
    }

    private fun addMenuToCart() {
        viewModel.addToCart().observe(this) {
            it.proceedWhen (
                doOnSuccess = {
                    Toast.makeText(
                        this,
                        getString(R.string.text_add_to_cart_success), Toast.LENGTH_SHORT
                    ).show()
                    finish()
                },
                doOnError = {
                    Toast.makeText(this, getString(R.string.add_to_cart_failed), Toast.LENGTH_SHORT)
                        .show()
                },
                doOnLoading = {
                    Toast.makeText(this, getString(R.string.loading), Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun bindMenu(menu: Menu?) {
        menu?.let { item ->
            binding.ivPhotoFood.load(item.imgUrl) {
                crossfade(true)
            }
            binding.tvNameFood.text = item.name
            binding.tvFoodDesc.text = item.desc
            binding.tvFoodPrice.text = item.price.toIndonesianFormat()
            binding.tvDescLoca.text = item.addres
            binding.tvDescLoca.setOnClickListener {
                // val i= Intent(Intent.ACTION_VIEW)
                //i.setData(Uri.parse(menu.mapURL))
                //startActivity(i)
            }
        }
    }

    private fun observeData() {
        viewModel.priceLiveData.observe(this) {
            binding.btnBuy.isEnabled = it != 0.0
            binding.tvMenuTotalPrice.text = it.toIndonesianFormat()
        }
        viewModel.menuCountLiveData.observe(this) {
            binding.tvMenuCount.text = it.toString()
        }
    }

    companion object {
        const val EXTRA_MENU = "EXTRA_MENU"
        fun startActivity(context: Context, menu: Menu) {
            val intent = Intent(context, DetailFoodActivity::class.java)
            intent.putExtra(EXTRA_MENU, menu)
            context.startActivity(intent)
        }
    }
}
