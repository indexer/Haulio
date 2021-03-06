package com.indexer.mover.base

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.indexer.mover.model.Job
import com.indexer.mover.model.JobAccept

@Database(entities = [Job::class, JobAccept::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

  abstract fun jobDao(): JobDao

  companion object {

    private var INSTANCE: AppDatabase? = null

    private val sLock = Any()

    fun getInstance(context: Context): AppDatabase {
      synchronized(sLock) {
        if (INSTANCE == null) {
          INSTANCE = Room.databaseBuilder(
              context.applicationContext,
              AppDatabase::class.java, "job.db"
          ).allowMainThreadQueries().build()
        }
        return INSTANCE as AppDatabase
      }
    }
  }

}