@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.gzeinnumer.externalroomreaddbfromfilekt.helper

import android.os.Environment
import android.util.Base64
import java.io.*
import java.nio.channels.FileChannel
import java.util.zip.ZipFile

object FunctionGlobalZip {
    private const val TAG = "FunctionGLobalZip_"

    //start genFile
    //jika ingin replace file yang sudah ada, maka beri flag true, jika tidak maka false saja
    open fun initFileFromStringToZipToFile(
        base64EncodeFromFile: String,
        md5EncodeFromFile: String,
        isNew: Boolean
    ): Boolean {
        return if (FunctionGlobalDir.isFileExists(FunctionGlobalDir.appFolder + FunctionGlobalDir.appFile) && !isNew) {
            FunctionGlobalDir.myLogD(TAG, "File sudah dibuat")
            true
        } else {
            if (converToZip(base64EncodeFromFile)) {
                if (compareMd5(md5EncodeFromFile)) {
                    try {
                        if (unzip()) {
                            FunctionGlobalDir.myLogD(TAG, "Success Zip")
                            true
                        } else {
                            FunctionGlobalDir.myLogD(TAG, "Failed Zip")
                            false
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                        FunctionGlobalDir.myLogD(TAG, "Failed Zip " + e.message)
                        false
                    }
                } else {
                    false
                }
            } else {
                false
            }
        }
    }

    private fun converToZip(base64: String): Boolean {
        FunctionGlobalDir.myLogD(TAG, "converToZip")
        val fileZip =
            File(FunctionGlobalDir.getStorageCard + FunctionGlobalDir.appFolder + FunctionGlobalDir.appFile)
        val pdfAsBytes: ByteArray
        return try {
            pdfAsBytes = Base64.decode(base64, 0)
            val os: FileOutputStream
            try {
                os = FileOutputStream(fileZip, false)
                os.write(pdfAsBytes)
                os.flush()
                os.close()
                true
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                false
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun compareMd5(md5OriginValue: String): Boolean {
        FunctionGlobalDir.myLogD(TAG, "compareMd5")
        try {
            val filePath = FunctionGlobalDir.getStorageCard + FunctionGlobalDir.appFolder + FunctionGlobalDir.appFile
            val fis = FileInputStream(filePath)
            val md5Checksum = Md5Checksum.md5(fis)
            return md5Checksum == md5OriginValue
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            FunctionGlobalDir.myLogD(TAG, "onCreate")
        }
        return false
    }

    @Throws(IOException::class)
    private fun unzip(): Boolean {
        FunctionGlobalDir.myLogD(TAG, "unzip")
        val zipFile = FunctionGlobalDir.getStorageCard + FunctionGlobalDir.appFolder + FunctionGlobalDir.appFile
        val unzipLocation = FunctionGlobalDir.getStorageCard + FunctionGlobalDir.appFolder + "/db/"
        val archive = ZipFile(zipFile)
        val e = archive.entries()
        while (e.hasMoreElements()) {
            val entry = e.nextElement()
            val file = File(unzipLocation, entry.name)
            if (entry.isDirectory) {
                file.mkdirs()
            } else {
                if (!file.parentFile.exists()) {
                    file.parentFile.mkdirs()
                }
                val `in` = archive.getInputStream(entry)
                val out: OutputStream =
                    BufferedOutputStream(FileOutputStream(file))
                val buffer = ByteArray(1024)
                var read: Int
                while (`in`.read(buffer).also { read = it } != -1) {
                    out.write(buffer, 0, read)
                }
                `in`.close()

                // write the output file (You have now copied the file)
                out.flush()
                out.close()
                return true
            }
        }
        return false
    } //end genFile
}
