package com.indexer.mover.base

import android.arch.persistence.room.Query
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import com.indexer.mover.model.Job
import com.indexer.mover.model.JobAccept

@Dao
interface JobDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAllJob(products: List<Job>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAcceptJob(products: JobAccept)

  @Query("SELECT * FROM job ORDER BY priority ASC")
  fun getAlljob(): List<Job>

  @Query("SELECT * FROM jobAccept ORDER BY priority ASC")
  fun getAllAcceptjob(): List<JobAccept>

  @Query("SELECT company FROM job")
  fun getAllCompanyName(): List<String>

  @Query("SELECT * FROM job WHERE company =:companyName")
  fun getJobByCompanyName(companyName: String): List<Job>

}