package com.example.assignment2.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment2.databinding.ActivityDashboardBinding
import com.example.assignment2.ui.details.DetailsActivity
import com.example.assignment2.ui.login.LoginActivity
import com.example.assignment2.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private val viewModel: DashboardViewModel by viewModels()
    private var keypass: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        keypass = intent.getStringExtra(LoginActivity.EXTRA_KEYPASS).orEmpty()
        if (keypass.isBlank()) {
            Toast.makeText(this, "Missing session. Please log in again.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        binding.recyclerViewEntities.layoutManager = LinearLayoutManager(this)
        viewModel.dashboardState.observe(this) { state -> renderState(state) }
        viewModel.loadDashboard(keypass)
    }

    private fun renderState(state: Resource<List<Map<String, String>>>) {
        when (state) {
            is Resource.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.textEmpty.visibility = View.GONE
            }
            is Resource.Success -> {
                binding.progressBar.visibility = View.GONE
                binding.textEmpty.visibility = if (state.data.isEmpty()) View.VISIBLE else View.GONE
                binding.recyclerViewEntities.adapter = EntityAdapter(state.data) { entity ->
                    val intent = Intent(this, DetailsActivity::class.java).apply {
                        putExtra(DetailsActivity.EXTRA_ENTITY, HashMap(entity))
                    }
                    startActivity(intent)
                }
            }
            is Resource.Error -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}
