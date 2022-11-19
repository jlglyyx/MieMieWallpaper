package com.yang.lib_common.handle

import android.content.Context
import android.util.Log
import com.umeng.analytics.MobclickAgent
import com.yang.lib_common.app.BaseApplication
import com.yang.lib_common.service.CustomWallpaperService.Companion.context
import com.yang.lib_common.util.simpleDateFormat
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*


/**
 * @Author Administrator
 * @ClassName CrashHandle
 * @Description
 * @Date 2021/8/4 17:04
 */
class CrashHandle(val context:Context) : Thread.UncaughtExceptionHandler {

    private val uncaughtExceptionHandler: Thread.UncaughtExceptionHandler? =
        Thread.getDefaultUncaughtExceptionHandler()

    companion object {
        private const val TAG = "CrashHandle"

    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        try {
            MobclickAgent.onKillProcess(context)
        }catch (e:java.lang.Exception){
            e.printStackTrace()
        }

        writeErrorMessage(e)
        uncaughtExceptionHandler?.uncaughtException(t, e)
    }


    private fun writeErrorMessage(e: Throwable): Boolean {
        try {
            val stringBuilder = StringBuilder()
            val stringWriter = StringWriter()
            val printWriter = PrintWriter(stringWriter)
            e.printStackTrace(printWriter)
            printWriter.close()
            stringBuilder.append(stringWriter)
            val downloadCacheDirectory = "${BaseApplication.baseApplication.externalCacheDir}/crash/"
            val file = File(downloadCacheDirectory)
            val fileName = "crash_${simpleDateFormat.format(Date(System.currentTimeMillis()))}.log"
            if (!file.exists()) {
                file.mkdirs()
            }
            val fileOutputStream = FileOutputStream(downloadCacheDirectory + fileName)
            fileOutputStream.write(stringBuilder.toString().toByteArray())
            fileOutputStream.flush()
            fileOutputStream.close()
            return true
        } catch (e: Exception) {
            Log.i(TAG, "writeErrorMessage: ${e.message}")
            return false
        }

    }
}