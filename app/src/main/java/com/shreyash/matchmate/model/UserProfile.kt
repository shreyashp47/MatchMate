package com.shreyash.matchmate.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey val id: String,
    val name: String,
    val age: Int,
    val location: String,
    val imageUrl: String,
    val education: String,
    val religion: String,
    val matchScore: Int,
    val status: MatchStatus
)

enum class MatchStatus {
    NONE, ACCEPTED, DECLINED
}

