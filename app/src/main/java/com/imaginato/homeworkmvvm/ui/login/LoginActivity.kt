package com.imaginato.homeworkmvvm.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.imaginato.homeworkmvvm.data.remote.login.response.LoginResponse
import com.imaginato.homeworkmvvm.databinding.ActivityLoginBinding
import com.imaginato.homeworkmvvm.exts.clearError
import com.imaginato.homeworkmvvm.exts.getIMEI
import com.imaginato.homeworkmvvm.exts.getIMSI
import com.imaginato.homeworkmvvm.exts.removeError
import com.imaginato.homeworkmvvm.ui.demo.MainActivity
import com.imaginato.homeworkmvvm.ui.login.entity.Status
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private val viewModel by viewModel<LoginViewModel>()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Init
        init()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnLogin.id -> {
                viewModel.validateForm(
                    binding.edtUserName.text.toString(),
                    binding.edtPassword.text.toString()
                )
            }
        }
    }

    /***
     * Initialize for the first time
     */
    private fun init() {
        // Set view listener
        setClickListener()

        // Init Observer Field State
        observeFieldState()

        // Init Observe API State
        observeAPIState()
    }

    /***
     * Set view click listener
     */
    private fun setClickListener() {
        binding.btnLogin.setOnClickListener(this)

        binding.tilUserName.removeError()
        binding.tilPassword.removeError()
    }

    /***
     * Observe Field State
     */
    private fun observeFieldState() {
        if (viewModel.verifyData.hasObservers()) {
            viewModel.verifyData.removeObservers(this)
        } else {
            viewModel.verifyData.observe(this) {
                when (it.status) {
                    // For User Name Text Field
                    Status.ERROR_USER_NAME -> {
                        binding.tilUserName.error = it.msg
                    }

                    // For Password Text Field
                    Status.ERROR_PASSWORD -> {
                        binding.tilPassword.error = it.msg
                    }

                    // For Validation Success
                    Status.SUCCESS -> {
                        binding.tilUserName.clearError()
                        binding.tilPassword.clearError()

                        /// Make API call for login user
                        viewModel.loginUser(
                            getIMSI(),
                            getIMEI(),
                            binding.edtUserName.text.toString(),
                            binding.edtPassword.text.toString(),
                        )
                    }

                    // Show and hide progress
                    Status.SHOW_PROGRESS -> {
                        showLoader(it.isValid)
                    }

                    else -> {
                        // Check for other state
                    }
                }
            }
        }
    }

    private fun observeAPIState() {
        if (viewModel.remoteAPIState.hasObservers()) {
            viewModel.remoteAPIState.removeObservers(this)
        } else {
            viewModel.remoteAPIState.observe(this) {
                when (it.apiStatus) {
                    Status.LOGIN_API -> {
                        when (it.status) {
                            // API loading State
                            Status.LOADING -> {
                                showLoader(true)
                            }

                            // API success state
                            Status.SUCCESS -> {
                                // Hide Loader
                                showLoader(false)

                                // Show Success Message
                                (it.data as LoginResponse).errorMessage?.let { msg -> showToast(msg) }

                                // Redirect to home screen
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finish()
                            }

                            // API failure
                            Status.FAIL -> {
                                // Hide Loader
                                showLoader(false)

                                // Show toast
                                if (it.data is String) {
                                    showToast(it.data)
                                }
                            }

                            else -> {
                                showLoader(false)
                            }
                        }
                    }

                    else -> {
                        showLoader(false)
                    }
                }
            }
        }
    }

    /***
     * Show Loader
     */
    private fun showLoader(isLoading: Boolean) {
        binding.btnLogin.visibility = if (isLoading) {
            View.GONE
        } else {
            View.VISIBLE
        }

        binding.pbLoading.visibility = if (isLoading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    /***
     * Show toast message
     */
    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}