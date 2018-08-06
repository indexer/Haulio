package com.indexer.mover.model

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "job")
data class Job(
  @PrimaryKey(autoGenerate = false)
  val id: Int,
  @SerializedName("job-id")
  val job_id: Int,
  val priority: Int,
  val company: String,
  val address: String,
  @SerializedName("geolocation")
  @Embedded
  val geolocation: Geolocation
) : Serializable