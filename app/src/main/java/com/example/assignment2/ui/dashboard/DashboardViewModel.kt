package com.example.assignment2.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment2.data.repository.DashboardRepository
import com.example.assignment2.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository
) : ViewModel() {

    private val _dashboardState = MutableLiveData<Resource<List<Map<String, String>>>>()
    val dashboardState: LiveData<Resource<List<Map<String, String>>>> = _dashboardState

    fun loadDashboard(keypass: String) {
        _dashboardState.value = Resource.Loading
        viewModelScope.launch {
            _dashboardState.value = dashboardRepository.getDashboard(keypass)
        }
    }
}
