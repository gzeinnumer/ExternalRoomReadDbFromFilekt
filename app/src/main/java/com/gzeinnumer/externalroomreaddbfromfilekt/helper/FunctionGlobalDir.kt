package com.gzeinnumer.externalroomreaddbfromfilekt.helper

import android.os.Environment
import android.util.Log
import java.io.File

object FunctionGlobalDir {

    var getStorageCard = Environment.getExternalStorageDirectory().toString()
    var appFolder = "/ExternalRoomReadDbFromFileKt"

    var appFile = "/ExternalBase64Md5ToZip.zip"

    private const val TAG = "FunctionGLobalDir_"

    fun myLogD(tag: String, msg: String) {
        Log.d("MyZein", tag + "_" + msg)
    }

    fun initFolder(): Boolean {
        // create folder
        var folder = File(getStorageCard + appFolder)
        if (!folder.exists()) {
            if (!creatingFolder(folder)) {
                return false
            }
        }
        folder =
            File("$getStorageCard$appFolder/db")
        if (!folder.exists()) {
            if (!creatingFolder(folder)) {
                return false
            }
        }
        return true
    }

    private fun creatingFolder(folder: File): Boolean {
        try {
            if (folder.mkdirs()) {
                myLogD(TAG, "Folder created")
            }
        } catch (e: Exception) {
            myLogD(TAG, "Folder not created")
            return false
        }
        return true
    }

    fun isFileExists(path: String): Boolean {
        val file = File(getStorageCard + path)
        return file.exists()
    }
}