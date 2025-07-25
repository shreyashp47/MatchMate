package com.shreyash.matchmate.network

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/")
    suspend fun getUsers(@Query("results") results: Int = 20): Response<ApiResponse>

    companion object {
        private var INSTANCE: ApiService? = null

        fun create(): ApiService {
            return INSTANCE ?: synchronized(this) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://randomuser.me/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val instance = retrofit.create(ApiService::class.java)
                INSTANCE = instance
                instance
            }
        }
    }
}
