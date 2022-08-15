package com.example.linkstore.application.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import com.example.linkstore.application.db.AppDb.Companion.APP_DB_VERSION
import com.example.linkstore.features.savelink.data.local.dao.LinkDao
import com.example.linkstore.features.savelink.data.models.entities.LinkEntity

@Database(entities = [LinkEntity::class], version = APP_DB_VERSION, exportSchema = true)
abstract class AppDb : RoomDatabase() {

    abstract val linkDao: LinkDao

    companion object {
        const val APP_DB_VERSION = 2
        private const val APP_DB_NAME = "LinkStoreAppDb"

        private val MIGRATION_1_2 = Migration(startVersion = 1, endVersion = 2) {
            it.execSQL("CREATE TABLE `new_links_table` (`originalLink` TEXT NOT NULL, `groupName` TEXT NOT NULL, `storingTimeStamp` INTEGER NOT NULL, `extraNote` TEXT, `previewThumbnail` TEXT NOT NULL DEFAULT '', PRIMARY KEY(`originalLink`))")
            it.execSQL("INSERT INTO new_links_table(originalLink, groupName, storingTimeStamp, extraNote) SELECT originalLink, groupName, storingTimeStamp, extraNote FROM links_table")
            it.execSQL("DROP TABLE links_table")
            it.execSQL("ALTER TABLE new_links_table RENAME TO links_table")
        }

        fun getInstance(context: Context): AppDb {
            return synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDb::class.java,
                    APP_DB_NAME
                )
                    .addMigrations(
                        MIGRATION_1_2
                    )
                    .build()
            }
        }
    }

}