package com.somethingsimple.nasapod

import android.app.Application
import androidx.room.Room
import com.somethingsimple.nasapod.data.local.PodDao
import com.somethingsimple.nasapod.data.local.PodDatabase

class NasaPodApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {

        private var appInstance: NasaPodApplication? = null
        private var db: PodDatabase? = null
        private const val DB_NAME = "pod.db"

        private fun getDatabase(): PodDatabase {
            if (db == null) {
                synchronized(PodDatabase::class.java) {
                    if (db == null) {
                        if (appInstance == null) throw IllegalStateException("Application is null while creating DataBase")
                        db = Room.databaseBuilder(
                            appInstance!!.applicationContext,
                            PodDatabase::class.java,
                            DB_NAME
                        ).allowMainThreadQueries()
                            .build()
                    }
                }
            }
            return db!!
        }

        fun getPodDao(): PodDao {
            return getDatabase().podDao()
        }
    }
}