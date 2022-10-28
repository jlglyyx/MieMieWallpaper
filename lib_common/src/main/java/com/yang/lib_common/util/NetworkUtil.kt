package com.yang.lib_common.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log

/**
 * @Author Administrator
 * @ClassName NetworkUtil
 * @Description
 * @Date 2021/7/30 10:44
 */
class NetworkUtil {

    companion object {

        private const val TAG = "NetworkUtil"


        @JvmStatic
        fun getNetworkStatus(): ConnectivityManager.NetworkCallback =
            object : ConnectivityManager.NetworkCallback() {

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                        when {
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                                //wifi

                            }
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                                //蜂窝
                                //showShort("当前为流量上网，请注意流量消耗")

                            }
                        }
                    }


                }

                override fun onLost(network: Network) {
                    super.onLost(network)
//                    showShort("当前网络无连接，请检查网络配置")
                }

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)

//                    netStatus = 0
                }
            }


        @JvmStatic
        fun isNetworkClient(context: Context): Boolean {
            var connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                        ?: return false
                return when {
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        true
                    }
                    else -> {
                        false
                    }
                }

            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo ?: return false

                return activeNetworkInfo.isAvailable
            }
        }
    }


    fun getNetworkStatus(context: Context) {
        var connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                    ?: return
            when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {

                    //wifi
                    showShort("wifi")
                }
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {

                    //蜂窝
                    showShort("蜂窝")
                }
            }

        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo ?: return
            if (activeNetworkInfo.isAvailable) {
                when (activeNetworkInfo.type) {
                    ConnectivityManager.TYPE_MOBILE -> {
                        showShort("蜂窝")
                    }
                    ConnectivityManager.TYPE_WIFI -> {
                        showShort("wifi")
                    }
                }
            } else {
                showShort("没网")
            }
        }
    }


}