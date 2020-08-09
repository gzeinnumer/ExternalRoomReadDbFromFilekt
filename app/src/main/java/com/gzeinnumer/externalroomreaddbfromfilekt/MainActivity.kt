package com.gzeinnumer.externalroomreaddbfromfilekt

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.gzeinnumer.externalroomreaddbfromfilekt.data.AppDatabase
import com.gzeinnumer.externalroomreaddbfromfilekt.helper.FunctionGlobalDir
import com.gzeinnumer.externalroomreaddbfromfilekt.model.SampleTable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity_"

    var msg = "externalroomreaddbfromfilekt\n"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = TAG

        val appDatabase = AppDatabase.getInstance(applicationContext)
        FunctionGlobalDir.myLogD(TAG, AppDatabase.getInstance(applicationContext).openHelper.writableDatabase.path)

        FunctionGlobalDir.myLogD(TAG, "Jumlah Data dalam dalam table sample_data ada " + appDatabase.sampleTableDao().getAll().size)
        msg += "Jumlah Data dalam dalam table sample_data ada " + appDatabase.sampleTableDao().getAll().size +"\n"

        appDatabase.sampleTableDao().insertAll(SampleTable(0, "data baru"))
        AppDatabase.copyFile(appDatabase)
        msg += "Data baru di insert\n"

        FunctionGlobalDir.myLogD(TAG, "Jumlah Data dalam dalam table sample_data ada " + appDatabase.sampleTableDao().getAll().size)
        msg += "Jumlah Data dalam dalam table sample_data ada " + appDatabase.sampleTableDao().getAll().size +"\n"

        tv.text = msg
    }
}