package com.yang.lib_common.service

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.yang.lib_common.R
import com.yang.lib_common.constant.AppConstant
import org.osgi.framework.Constants.SERVICE_ID

/**
 * @Author Administrator
 * @ClassName DaemonService
 * @Description
 * @Date 2021/12/13 10:43
 */
class DaemonService : Service() {

    private val daemonBinder: DaemonBinder by lazy {
         DaemonBinder()
    }

    private val daemonRemoteConnection: DaemonRemoteService.DaemonRemoteConnection by lazy {
        DaemonRemoteService().DaemonRemoteConnection()
    }

    override fun onBind(intent: Intent?): IBinder {

        return daemonBinder
    }

    class DaemonBinder : Binder() {

    }


    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val build = NotificationCompat.Builder(applicationContext, AppConstant.NoticeChannel.DOWNLOAD)
            startForeground(1,build.build())
        }
        bindService(Intent(this@DaemonService,DaemonRemoteService::class.java),daemonRemoteConnection,BIND_AUTO_CREATE)
    }

    inner class DaemonConnection : ServiceConnection{

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

            Log.i("TAG", "onServiceConnected: DaemonRemoteService 服务连接")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i("TAG", "onServiceConnected: DaemonRemoteService 服务关闭")
            bindService(Intent(this@DaemonService,DaemonRemoteService::class.java),daemonRemoteConnection,BIND_AUTO_CREATE)
        }

    }
}