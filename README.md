# ExternalRoomReadDbFromFileKT
 part 5, setelah zip dibuat , buat instanse untuk room

Using [Room](https://developer.android.com/training/data-storage/room?hl=id) as your local database.

#### Code for AppDataBase
```kotlin
@Database(entities = [SampleTable::class], version = 3)
abstract class AppDatabase : RoomDatabase() {

    abstract fun sampleTableDao(): SampleTableDao

    companion object {

        private val TAG = "AppDatabase"
        private val dbName = "ExternalBase64Md5ToZip.db"

        fun getInstance(context: Context): AppDatabase {
            val folder = File(Environment.getExternalStorageDirectory().toString()+"FolderOnExternal"+"/db/ExternalBase64Md5ToZip.db")
            return Room.databaseBuilder(context, AppDatabase::class.java, "ExternalBase64Md5ToZip.db")
                .createFromFile(folder)
                .setJournalMode(JournalMode.TRUNCATE)
                .allowMainThreadQueries()
                .build()
        }

        fun copyFile(appDatabase: AppDatabase) {
            appDatabase.close()
            try {
                val localDir = File(Environment.getExternalStorageDirectory().toString()+"FolderOnExternal" + "/db")
                val sd = Environment.getExternalStorageDirectory()
                if (sd.canWrite()) {
                    val currentDBPath = appDatabase.openHelper.writableDatabase.path
                    val backupDBPath: String = dbName
                    val currentDB = File(currentDBPath)
                    val backupDB = File(localDir, backupDBPath)
                    Log.d(TAG, "path local  $localDir")
                    Log.d(TAG, "path sumber  $currentDB")
                    Log.d(TAG, "path tujuan  $backupDB")
                    if (currentDB.exists()) {
                        val src = FileInputStream(currentDB).channel
                        val dst = FileOutputStream(backupDB).channel
                        dst.transferFrom(src, 0, src.size())
                        src.close()
                        dst.close()
                    }
                } else {
                    Log.d(TAG, "path Cannon write")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d(TAG, "path " + e.message)
            }
        }
}
```

#### Important
Every new transaction process `Insert, Update, Delete` or `ON APP DESTROY` you should use `copyFile` to close Database and backup Database `Room` from root to eksternal. Here is the example.

```kotlin
val appDatabase = AppDatabase.getInstance(applicationContext)
appDatabase.sampleTableDao().insertAll(SampleTable(0, "data baru"))
AppDatabase.copyFile(appDatabase)
```

---

```
Copyright 2020 M. Fadli Zein
```
