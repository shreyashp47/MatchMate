package com.shreyash.matchmate.network

data class ApiResponse(
    val results: List<ApiUser>
)

data class ApiUser(
    val gender: String,
    val login: Login,
    val name: Name,
    val dob: Dob,
    val location: Location,
    val picture: Picture
)

data class Login(val uuid: String)
data class Name(val first: String, val last: String)
data class Dob(val age: Int)
data class Location(val city: String, val state: String,val country: String)
data class Picture(val large: String)

