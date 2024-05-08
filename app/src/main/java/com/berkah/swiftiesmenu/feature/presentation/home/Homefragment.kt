package com.berkah.swiftiesmenu.feature.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.berkah.swiftiesmenu.R
import com.berkah.swiftiesmenu.databinding.FragmentHomeBinding
import com.berkah.swiftiesmenu.feature.data.model.Category
import com.berkah.swiftiesmenu.feature.data.model.Menu
import com.berkah.swiftiesmenu.feature.data.utils.proceedWhen
import com.berkah.swiftiesmenu.feature.presentation.detailfood.DetailFoodActivity
import com.berkah.swiftiesmenu.feature.presentation.home.adapter.CategoryListAdapter
import com.berkah.swiftiesmenu.feature.presentation.home.adapter.MenuListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class Homefragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var menuAdapter: MenuListAdapter

    private var gridLayoutManager: GridLayoutManager? = null

    private val homeViewModel: HomeViewModel by viewModel()

    private val categoryAdapter: CategoryListAdapter by lazy {
        CategoryListAdapter {
            // when category clicked
            getMenuData(it.name)
        }
    }

    private fun getMenuData(categoryName: String? = null) {
        homeViewModel.getMenus(categoryName).observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    it.payload?.let { data ->
                        bindMenu(data)
                    }
                },
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        getCategoryData()
        setClickAction()
        getMenuData(null)
        observeGridMode()
    }

    private fun observeGridMode() {
        homeViewModel.isUsingGrid.observe(viewLifecycleOwner) {
            setButtonText(it)
            bindMenuList(it)
        }
    }

    private fun getCategoryData() {
        homeViewModel.getCategories().observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    it.payload?.let { data -> bindCategoryList(data) }
                },
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

    private fun bindMenuList(isUsingGrid: Boolean) {
        val columnCount = if (isUsingGrid) 2 else 1
        val listType = if (isUsingGrid) MenuListAdapter.MODE_GRID else MenuListAdapter.MODE_LIST

        gridLayoutManager = GridLayoutManager(requireContext(), columnCount)
        binding.rvMenuList.adapter = menuAdapter
        binding.rvMenuList.layoutManager = gridLayoutManager
        menuAdapter.listMode = listType
        menuAdapter.notifyDataSetChanged()
    }

    private fun bindAdapterMenu() {
        val listType = if (homeViewModel.isUsingGrid.value == true) MenuListAdapter.MODE_GRID else MenuListAdapter.MODE_LIST
        menuAdapter =
            MenuListAdapter(
                object : MenuListAdapter.OnItemClickedListener<Menu> {
                    override fun onItemClicked(item: Menu) {
                        navigateToDetail(item)
                    }
                },
                listType,
            )
    }

    private fun changeListMode() {
        homeViewModel.changeGridMode()
    }

    private fun bindMenu(data: List<Menu>) {
        menuAdapter.submitData(data)
    }

    private fun setClickAction() {
        binding.btnChangeListMode.setOnClickListener {
            val newMode = !homeViewModel.isUsingGridMode()
            homeViewModel.setUsingGridMode(newMode)
            changeListMode()
        }
    }

    private fun setButtonText(usingGridMode: Boolean) {
        val textResId = if (usingGridMode) R.string.text_list_mode else R.string.text_grid_mode
        binding.btnChangeListMode.setText(textResId)
    }

    private fun navigateToDetail(item: Menu) {
        val intent =
            Intent(requireContext(), DetailFoodActivity::class.java).apply {
                putExtra(DetailFoodActivity.EXTRA_MENU, item)
            }
        startActivity(intent)
    }
}
