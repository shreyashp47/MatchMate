package com.shreyash.matchmate.viewmodel

import androidx.lifecycle.*
import com.shreyash.matchmate.model.UserProfile
import com.shreyash.matchmate.model.MatchStatus
import com.shreyash.matchmate.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    val profiles: LiveData<List<UserProfile>> = repository.getProfiles()

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchProfilesWithRetry() {
        viewModelScope.launch {
            repeat(3) {
                val result = repository.fetchAndStoreProfiles()
                if (result.isSuccess) {
                    _error.value = null
                    return@launch
                }
            }
            _error.value = "Failed to fetch profiles after retries."
        }
    }

    fun updateStatus(profile: UserProfile, status: MatchStatus) {
        viewModelScope.launch {
            repository.updateProfile(profile.copy(status = status))
        }
    }
}

