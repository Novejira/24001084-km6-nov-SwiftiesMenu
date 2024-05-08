package com.berkah.swiftiesmenu.feature.DI

import android.content.SharedPreferences
import com.berkah.swiftiesmenu.feature.data.datasource.cart.CartDataSource
import com.berkah.swiftiesmenu.feature.data.datasource.cart.CartDatabaseDataSource
import com.berkah.swiftiesmenu.feature.data.datasource.category.CategoryApiDataSource
import com.berkah.swiftiesmenu.feature.data.datasource.category.CategoryDataSource
import com.berkah.swiftiesmenu.feature.data.datasource.menu.MenuApiDataSource
import com.berkah.swiftiesmenu.feature.data.datasource.menu.MenuDataSource
import com.berkah.swiftiesmenu.feature.data.datasource.user.UserDataSource
import com.berkah.swiftiesmenu.feature.data.datasource.user.UserDataSourceImpl
import com.berkah.swiftiesmenu.feature.data.repository.CartRepository
import com.berkah.swiftiesmenu.feature.data.repository.CartRepositoryImpl
import com.berkah.swiftiesmenu.feature.data.repository.CategoryRepository
import com.berkah.swiftiesmenu.feature.data.repository.CategoryRepositoryImpl
import com.berkah.swiftiesmenu.feature.data.repository.MenuRepository
import com.berkah.swiftiesmenu.feature.data.repository.MenuRepositoryImpl
import com.berkah.swiftiesmenu.feature.data.repository.PreferenceRepository
import com.berkah.swiftiesmenu.feature.data.repository.PreferenceRepositoryImpl
import com.berkah.swiftiesmenu.feature.data.repository.UserRepository
import com.berkah.swiftiesmenu.feature.data.repository.UserRepositoryImpl
import com.berkah.swiftiesmenu.feature.data.source.local.dao.CartDao
import com.berkah.swiftiesmenu.feature.data.source.local.database.AppDatabase
import com.berkah.swiftiesmenu.feature.data.source.local.pref.UserPreference
import com.berkah.swiftiesmenu.feature.data.source.local.pref.UserPreferenceImpl
import com.berkah.swiftiesmenu.feature.data.source.network.firebase.FirebaseAuthDataSource
import com.berkah.swiftiesmenu.feature.data.source.network.firebase.FirebaseAuthDataSourceImpl
import com.berkah.swiftiesmenu.feature.data.source.network.services.SwiftiesMenuApiService
import com.berkah.swiftiesmenu.feature.data.utils.SharedPreferenceUtils
import com.berkah.swiftiesmenu.feature.presentation.cart.CartViewModel
import com.berkah.swiftiesmenu.feature.presentation.checkout.CheckoutViewModel
import com.berkah.swiftiesmenu.feature.presentation.detailfood.DetailFoodViewModel
import com.berkah.swiftiesmenu.feature.presentation.home.HomeViewModel
import com.berkah.swiftiesmenu.feature.presentation.login.LoginViewModel
import com.berkah.swiftiesmenu.feature.presentation.main.MainViewModel
import com.berkah.swiftiesmenu.feature.presentation.profile.ProfileViewModel
import com.berkah.swiftiesmenu.feature.presentation.register.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

object AppModules {
    private val networkModule =
        module {
            single<SwiftiesMenuApiService> { SwiftiesMenuApiService.invoke() }
        }
    private val firebaseModule =
        module {
            single<FirebaseAuth> { FirebaseAuth.getInstance() }
        }
    private val localModule =
        module {
            single { AppDatabase.createInstance(androidContext()) }
            single<CartDao> { get<AppDatabase>().cartDao() }
            single<SharedPreferences> {
                SharedPreferenceUtils.createPreference(
                    androidContext(),
                    UserPreferenceImpl.PREF_NAME,
                )
            }
            single<UserPreference> { UserPreferenceImpl(get()) }
        }
    private val datasource =
        module {
            single<CartDataSource> { CartDatabaseDataSource(get()) }
            single<CategoryDataSource> { CategoryApiDataSource(get()) }
            single<MenuDataSource> { MenuApiDataSource(get()) }
            single<UserDataSource> { UserDataSourceImpl(get()) }
            single<FirebaseAuthDataSource> { FirebaseAuthDataSourceImpl(get()) }
        }

    private val repository =
        module {
            single<CartRepository> { CartRepositoryImpl(get()) }
            single<CategoryRepository> { CategoryRepositoryImpl(get()) }
            single<MenuRepository> { MenuRepositoryImpl(get()) }
            single<UserRepository> { UserRepositoryImpl(get()) }
            single<PreferenceRepository> { PreferenceRepositoryImpl(get()) }
        }
    private val viewModelModule =
        module {
            viewModelOf(::HomeViewModel)
            viewModelOf(::MainViewModel)
            viewModelOf(::CartViewModel)
            viewModelOf(::CheckoutViewModel)
            viewModelOf(::ProfileViewModel)
            viewModel {
                RegisterViewModel(get())
            }

            viewModel {
                LoginViewModel(get())
            }

            viewModel { params ->
                DetailFoodViewModel(
                    extras = params.get(),
                    cartRepository = get(),
                )
            }
        }

    val modules =
        listOf(
            networkModule,
            localModule,
            datasource,
            repository,
            firebaseModule,
            viewModelModule,
        )
}
