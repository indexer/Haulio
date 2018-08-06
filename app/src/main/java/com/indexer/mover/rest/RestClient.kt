package com.indexer.mover.rest

import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RestClient private constructor() {
    private val mService: ApiService

    init {
        val restAdapter = Retrofit.Builder().baseUrl(Config.base)
            .addConverterFactory(GsonConverterFactory.create()).client(getNewHttpClient()).build()
        mService = restAdapter.create(ApiService::class.java)
    }

    private fun getNewHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder().followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)
                .cache(null).apply {
                    addInterceptor(HttpLoggingInterceptor().setLevel(Level.BODY))
                    connectTimeout(10, TimeUnit.SECONDS)
                    followRedirects(true)
                    followSslRedirects(true)
                    writeTimeout(60, TimeUnit.SECONDS)
                    readTimeout(60, TimeUnit.SECONDS)
                    connectTimeout(60, TimeUnit.SECONDS)
                            .writeTimeout(60, TimeUnit.SECONDS)
                            .readTimeout(60, TimeUnit.SECONDS)
                }
        return builder.build()
    }




    companion object {
        private var instance: RestClient? = null
        @Synchronized
        fun getService(): ApiService {
            return getRestInstance().mService
        }

        private fun getRestInstance(): RestClient {
            if (instance == null) {
                instance = RestClient()
            }
            return instance as RestClient
        }
    }
}