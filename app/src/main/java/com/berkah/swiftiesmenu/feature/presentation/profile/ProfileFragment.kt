
package com.berkah.swiftiesmenu.feature.presentation.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation
import com.berkah.swiftiesmenu.R
import com.berkah.swiftiesmenu.databinding.FragmentProfileBinding
import com.berkah.swiftiesmenu.feature.data.repository.UserRepositoryImpl
import com.berkah.swiftiesmenu.feature.data.source.network.firebase.FirebaseAuthDataSourceImpl
import com.berkah.swiftiesmenu.feature.data.utils.proceedWhen
import com.berkah.swiftiesmenu.feature.presentation.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    private val profileviewModel: ProfileViewModel by viewModel()

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                changePhotoProfile(uri)
            }
        }

    private fun changePhotoProfile(uri: Uri) {
        profileviewModel.updateProfilePicture(uri)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupForm()
        showUserData()
        setClickListeners()
        observeData()
    }

    private fun setClickListeners() {
        binding.btnChangeProfile.setOnClickListener {
            if (checkNameValidation()) {
                changeProfileData()
            }
        }
        binding.ivEditPhoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.tvChangePwd.setOnClickListener {
            requestChangePassword()
        }
        binding.tvLogout.setOnClickListener {
            doLogout()
        }
    }

    private fun requestChangePassword() {
        profileviewModel.createChangePwdRequest()
        val dialog =
            AlertDialog.Builder(requireContext())
                .setMessage(
                    "Change password request sended to your email : ${profileviewModel.getCurrentUser()?.email} Please check to your inbox or spam",
                )
                .setPositiveButton(
                    "Okay",
                ) { dialog, id ->
                }.create()
        dialog.show()
    }

    private fun doLogout() {
        val dialog =
            AlertDialog.Builder(requireContext()).setMessage("Do you want to logout ?")
                .setPositiveButton(
                    "Yes",
                ) { dialog, id ->
                    profileviewModel.doLogout()
                    navigateToLogin()
                }
                .setNegativeButton(
                    "No",
                ) { dialog, id ->
                    // no-op , do nothing
                }.create()
        dialog.show()
    }

    private fun navigateToLogin() {
        startActivity(
            Intent(requireContext(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            },
        )
    }

    private fun changeProfileData() {
        val fullName = binding.layoutForm.etName.text.toString().trim()
        profileviewModel.updateFullName(fullName)
    }

    private fun checkNameValidation(): Boolean {
        val fullName = binding.layoutForm.etName.text.toString().trim()
        return if (fullName.isEmpty()) {
            binding.layoutForm.tilName.isErrorEnabled = true
            binding.layoutForm.tilName.error = getString(R.string.text_error_name_cannot_empty)
            false
        } else {
            binding.layoutForm.tilName.isErrorEnabled = false
            true
        }
    }

    private fun observeData() {
        profileviewModel.changePhotoResult.observe(viewLifecycleOwner) {
            it.proceedWhen(doOnSuccess = {
                Toast.makeText(requireContext(), "Change Photo Profile Success !", Toast.LENGTH_SHORT).show()
                showUserData()
            }, doOnError = {
                Toast.makeText(requireContext(), "Change Photo Profile Failed !", Toast.LENGTH_SHORT).show()
                showUserData()
            })
        }
        profileviewModel.changeProfileResult.observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.pbLoading.isVisible = false
                    binding.btnChangeProfile.isVisible = true
                    Toast.makeText(requireContext(), "Change Profile data Success !", Toast.LENGTH_SHORT).show()
                },
                doOnError = {
                    binding.pbLoading.isVisible = false
                    binding.btnChangeProfile.isVisible = true
                    Toast.makeText(requireContext(), "Change Profile data Failed !", Toast.LENGTH_SHORT).show()
                },
                doOnLoading = {
                    binding.pbLoading.isVisible = true
                    binding.btnChangeProfile.isVisible = false
                },
            )
        }
    }

    private fun setupForm() {
        binding.layoutForm.tilName.isVisible = true
        binding.layoutForm.tilEmail.isVisible = true
        binding.layoutForm.etEmail.isEnabled = false
    }

    private fun showUserData() {
        profileviewModel.getCurrentUser()?.let {
            binding.layoutForm.etName.setText(it.fullName)
            binding.layoutForm.etEmail.setText(it.email)
            binding.ivProfilePict.load(it.photoUrl) {
                crossfade(true)
                placeholder(R.drawable.iv_profile_placeholder)
                error(R.drawable.iv_profile_placeholder)
                transformations(CircleCropTransformation())
            }
        }
    }

    private fun createViewModel(): ProfileViewModel {
        val firebaseAuth = FirebaseAuth.getInstance()
        val dataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
        val repo = UserRepositoryImpl(dataSource)
        return ProfileViewModel(repo)
    }
}
