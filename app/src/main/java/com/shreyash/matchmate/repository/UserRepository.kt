package com.shreyash.matchmate.repository

import android.content.Context
import android.util.Log
import com.shreyash.matchmate.db.AppDatabase
import com.shreyash.matchmate.db.UserProfileDao
import com.shreyash.matchmate.model.MatchStatus
import com.shreyash.matchmate.model.UserProfile
import com.shreyash.matchmate.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.abs
import kotlin.random.Random

class UserRepository(
    private val apiService: ApiService,
    private val userProfileDao: UserProfileDao
) {
    private val TAG = "UserRepository"
    suspend fun fetchAndStoreProfiles(): Result<Unit> = withContext(Dispatchers.IO) {
        // Simulate 30% network failure
        if (Random.nextFloat() < 0.3f) {
            return@withContext Result.failure(Exception("Simulated network failure"))
        }
        val response = apiService.getUsers()
        if (response.isSuccessful) {
            Log.i(TAG, "fetchAndStoreProfiles: Success")
            val apiUsers = response.body()?.results ?: emptyList()
            val profiles = apiUsers.map { apiUser ->
                val education = listOf("B.Tech", "MBA", "M.Sc", "B.A", "PhD").random()
                val religion = listOf("Hindu", "Muslim", "Christian", "Sikh", "Jain").random()
                val name = "${apiUser.name.first} ${apiUser.name.last}"
                val matchScore =
                    calculateMatchScore(apiUser.dob.age, apiUser.location.city, apiUser.gender)
                UserProfile(
                    id = apiUser.login.uuid,
                    name = name,
                    age = apiUser.dob.age,
                    location = apiUser.location.city + ", " + apiUser.location.country,
                    imageUrl = apiUser.picture.large,
                    education = education,
                    religion = religion,
                    matchScore = matchScore,
                    status = MatchStatus.NONE
                )
            }
            userProfileDao.insertProfiles(profiles)
            Result.success(Unit)
        } else {
            Result.failure(Exception("API error"))
        }
    }

    fun getProfiles() = userProfileDao.getAllProfiles()

    suspend fun updateProfile(profile: UserProfile) = userProfileDao.updateProfile(profile)

    private fun calculateMatchScore(age: Int, city: String, gender: String): Int {
        if (gender == "male") return 0
        val preferredAge = 28
        val ageScore = 50 - (abs(preferredAge - age) * 2)
        val cityScore = if (city.contains("India")) 50
        else if (city.contains("Germany")) 40
        else if (city.contains("Canada")) 30
        else if (city.contains("Australia")) 30
        else if (city.contains("Brazil")) 0
        else 10
        return (ageScore + cityScore).coerceIn(0, 100)
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getInstance(context: Context): UserRepository {
            return INSTANCE ?: synchronized(this) {
                val database = AppDatabase.getInstance(context)
                val apiService = ApiService.create()
                val instance = UserRepository(apiService, database.userProfileDao())
                INSTANCE = instance
                instance
            }
        }
    }
}
