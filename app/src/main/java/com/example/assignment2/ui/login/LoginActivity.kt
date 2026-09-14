package com.example.assignment2.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment2.databinding.ActivityLoginBinding
import com.example.assignment2.ui.dashboard.DashboardActivity
import com.example.assignment2.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {
            val username = binding.editTextUsername.text.toString().trim()
            val password = binding.editTextPassword.text.toString()
            viewModel.login(username, password)
        }

        viewModel.loginState.observe(this) { state -> renderState(state) }
    }

    private fun renderState(state: Resource<String>) {
        when (state) {
            is Resource.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.buttonLogin.isEnabled = false
            }
            is Resource.Success -> {
                binding.progressBar.visibility = View.GONE
                binding.buttonLogin.isEnabled = true
                val intent = Intent(this, DashboardActivity::class.java).apply {
                    putExtra(EXTRA_KEYPASS, state.data)
                }
                startActivity(intent)
                finish()
            }
            is Resource.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.buttonLogin.isEnabled = true
                Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        const val EXTRA_KEYPASS = "extra_keypass"
    }
}
