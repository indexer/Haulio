package com.indexer.mover.rest

import com.indexer.mover.model.Job
import retrofit2.Call
import retrofit2.http.GET


interface ApiService {
    @GET(Config.jobList)
    fun getJobList(): Call<List<Job>>

}
