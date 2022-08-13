package com.example.linkstore.application.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.linkstore.application.db.AppDb.Companion.APP_DB_VERSION
import com.example.linkstore.features.savelink.data.local.dao.LinkDao
import com.example.linkstore.features.savelink.data.local.models.entities.LinkEntity

@Database(entities = [LinkEntity::class], version = APP_DB_VERSION, exportSchema = true)
abstract class AppDb : RoomDatabase() {

    abstract val linkDao: LinkDao

    companion object {
        const val APP_DB_VERSION = 1
        private const val APP_DB_NAME = "LinkStoreAppDb"

        fun getInstance(context: Context): AppDb {
            return synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDb::class.java,
                    APP_DB_NAME
                )
                    .build()
            }
        }
    }

}