package com.berkah.swiftiesmenu.feature.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.berkah.swiftiesmenu.R
import com.berkah.swiftiesmenu.databinding.FragmentHomeBinding
import com.berkah.swiftiesmenu.feature.data.datasource.category.CategoryApiDataSource
import com.berkah.swiftiesmenu.feature.data.datasource.category.CategoryDataSource
import com.berkah.swiftiesmenu.feature.data.datasource.menu.MenuApiDataSource
import com.berkah.swiftiesmenu.feature.data.datasource.menu.MenuDataSource
import com.berkah.swiftiesmenu.feature.data.model.Category
import com.berkah.swiftiesmenu.feature.data.model.Menu
import com.berkah.swiftiesmenu.feature.data.repository.CategoryRepository
import com.berkah.swiftiesmenu.feature.data.repository.CategoryRepositoryImpl
import com.berkah.swiftiesmenu.feature.data.repository.MenuRepository
import com.berkah.swiftiesmenu.feature.data.repository.MenuRepositoryImpl
import com.berkah.swiftiesmenu.feature.data.source.network.services.SwiftiesMenuApiService
import com.berkah.swiftiesmenu.feature.presentation.detailfood.DetailFoodActivity
import com.berkah.swiftiesmenu.feature.presentation.home.adapter.CategoryListAdapter
import com.berkah.swiftiesmenu.feature.presentation.home.adapter.MenuListAdapter
import feature.utils.GenericViewModelFactory
import feature.utils.proceedWhen

class Homefragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var menuAdapter: MenuListAdapter

    private var gridLayoutManager: GridLayoutManager? = null

    private val viewModel: HomeViewModel by viewModels {
        val service = SwiftiesMenuApiService.invoke()
        val menuDataSource: MenuDataSource = MenuApiDataSource(service)
        val menuRepository: MenuRepository = MenuRepositoryImpl(menuDataSource)
        val categoryDataSource: CategoryDataSource = CategoryApiDataSource(service)
        val categoryRepository: CategoryRepository = CategoryRepositoryImpl(categoryDataSource)
        GenericViewModelFactory.create(HomeViewModel(categoryRepository,menuRepository))

    }
    private val categoryAdapter: CategoryListAdapter by lazy {
        CategoryListAdapter {
            //when category clicked
            getMenuData(it.name)

        }
    }


    private fun getMenuData(categoryName: String? = null) {
        viewModel.getMenus(categoryName).observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    it.payload?.let { data -> bindMenu(data)
                    }
                }
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        getCategoryData()
        setClickAction()
        getMenuData(null)
        observeGridMode()
    }
    private fun observeGridMode() {
        viewModel.isUsingGrid.observe(viewLifecycleOwner) {
            setButtonText(it)
            bindMenuList(it)
        }
    }

    private fun getCategoryData() {
        viewModel.getCategories().observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    it.payload?.let { data -> bindCategoryList(data) }
                }
            )
        }
    }

    private fun setupRecyclerViews() {
        binding.rvCategory.adapter = categoryAdapter
        bindAdapterMenu()
    }

    private fun bindCategoryList(data: List<Category>) {
        categoryAdapter.submitData(data)
    }
    private fun bindMenuList(isUsingGrid: Boolean ) {
        val columnCount = if (isUsingGrid) 2 else 1
        val listType = if (isUsingGrid) MenuListAdapter.MODE_GRID else MenuListAdapter.MODE_LIST

        gridLayoutManager = GridLayoutManager(requireContext(), columnCount)
        binding.rvMenuList.adapter = menuAdapter
        binding.rvMenuList.layoutManager = gridLayoutManager
        menuAdapter.listMode = listType
        menuAdapter.notifyDataSetChanged()
    }
    private fun bindAdapterMenu(){
        val listType = if (viewModel.isUsingGrid.value == true) MenuListAdapter.MODE_GRID else MenuListAdapter.MODE_LIST
        menuAdapter = MenuListAdapter(object : MenuListAdapter.OnItemClickedListener<Menu> {
            override fun onItemClicked(item: Menu) {
                navigateToDetail(item)
            }
        }, listType)
    }
    private fun changeListMode() {
        viewModel.changeGridMode()
    }
    private fun bindMenu(data: List<Menu>) {
        menuAdapter.submitData(data)
    }

    private fun setClickAction() {
        binding.btnChangeListMode.setOnClickListener {
            changeListMode()
        }
    }
    private fun setButtonText(usingGridMode: Boolean) {
        val textResId = if (usingGridMode) R.string.text_list_mode else R.string.text_grid_mode
        binding.btnChangeListMode.setText(textResId)
    }
    private fun navigateToDetail(item: Menu) {
        val intent = Intent(requireContext(), DetailFoodActivity::class.java).apply {
            putExtra(DetailFoodActivity.EXTRA_MENU, item)
        }
        startActivity(intent)
    }
}
