package com.shreyash.matchmate.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.shreyash.matchmate.databinding.ActivityMainBinding
import com.shreyash.matchmate.repository.UserRepository
import com.shreyash.matchmate.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: UserViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = UserRepository.getInstance(applicationContext)
                return UserViewModel(repository) as T
            }
        }
    }

    private val adapter = MatchAdapter { profile, status ->
        viewModel.updateStatus(profile, status)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        viewModel.profiles.observe(this) { profiles ->
            if (profiles.isEmpty())
                binding.emptyState.visibility = View.VISIBLE
            else {
                binding.emptyState.visibility = View.GONE
                adapter.submitList(profiles)
            }
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.fetchProfilesWithRetry()
            binding.swipeRefresh.isRefreshing = false
        }
        viewModel.fetchProfilesWithRetry()
    }
}
