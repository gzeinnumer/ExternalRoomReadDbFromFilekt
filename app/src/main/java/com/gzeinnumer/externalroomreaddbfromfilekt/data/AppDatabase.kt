package com.gzeinnumer.externalroomreaddbfromfilekt.data

import android.content.Context
import android.os.Environment
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gzeinnumer.externalroomreaddbfromfilekt.dao.SampleTableDao
import com.gzeinnumer.externalroomreaddbfromfilekt.helper.FunctionGlobalDir
import com.gzeinnumer.externalroomreaddbfromfilekt.model.SampleTable
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@Database(entities = [SampleTable::class], version = 3)
abstract class AppDatabase : RoomDatabase() {

    abstract fun sampleTableDao(): SampleTableDao

    companion object {

        private val TAG = "AppDatabase"
        private val dbName = "ExternalBase64Md5ToZip.db"

        fun getInstance(context: Context): AppDatabase {
            val folder =
                File(FunctionGlobalDir.getStorageCard + FunctionGlobalDir.appFolder + "/db/ExternalBase64Md5ToZip.db")
            return Room.databaseBuilder(context, AppDatabase::class.java, "ExternalBase64Md5ToZip.db")
                //                .addMigrations(AppDatabase.MIGRATION_1)
                .createFromFile(folder)
                .setJournalMode(JournalMode.TRUNCATE)
                .allowMainThreadQueries()
                .build()
        }

        fun copyFile(appDatabase: AppDatabase) {
            appDatabase.close()
            try {
                val localDir = File(FunctionGlobalDir.getStorageCard + FunctionGlobalDir.appFolder + "/db")
                val sd = Environment.getExternalStorageDirectory()
                if (sd.canWrite()) {
                    val currentDBPath = appDatabase.openHelper.writableDatabase.path
                    val backupDBPath: String = dbName
                    val currentDB = File(currentDBPath)
                    val backupDB = File(localDir, backupDBPath)
                    FunctionGlobalDir.myLogD(TAG, "path local  $localDir")
                    FunctionGlobalDir.myLogD(TAG, "path sumber  $currentDB")
                    FunctionGlobalDir.myLogD(TAG, "path tujuan  $backupDB")
                    if (currentDB.exists()) {
                        val src = FileInputStream(currentDB).channel
                        val dst = FileOutputStream(backupDB).channel
                        dst.transferFrom(src, 0, src.size())
                        src.close()
                        dst.close()
                    }
                } else {
                    FunctionGlobalDir.myLogD(TAG, "path Cannon write")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                FunctionGlobalDir.myLogD(TAG, "path " + e.message)
            }
        }

        val MIGRATION_1: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `sample_table` (`id` INTEGER, `name` TEXT, PRIMARY KEY(`id`))")
            }
        }
    }
}