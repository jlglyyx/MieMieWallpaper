@file:JvmName("AppUtils")

package com.yang.lib_common.util

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.blankj.utilcode.util.*
import com.blankj.utilcode.util.ImageUtils.getImageType
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import com.jakewharton.rxbinding4.view.clicks
import com.lxj.xpopup.util.XPopupUtils
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.mmkv.MMKV
import com.yang.lib_common.R
import com.yang.lib_common.base.viewmodel.BaseViewModel
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.constant.AppConstant.Constant.CLICK_TIME
import com.yang.lib_common.room.entity.UserInfoData
import io.reactivex.rxjava3.core.Observable
import java.io.*
import java.lang.reflect.Type
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern


private const val TAG = "AppUtils"

val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")

val formatDate_YYYYMMMDDHHMMSS = SimpleDateFormat("yyyyMMddHHmmss")

val formatDate_YYYY_MMM_DD_HHMMSS = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

private val arr = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f")

val  gson = Gson()

/**
 * @return 宽高集合
 */
fun getScreenPx(context: Context): IntArray {
    val resources = context.resources
    val displayMetrics = resources.displayMetrics
    val widthPixels = displayMetrics.widthPixels
    val heightPixels = displayMetrics.heightPixels
    return intArrayOf(widthPixels, heightPixels)
}

/**
 * @return 宽高集合
 */
fun getScreenDpi(context: Context): FloatArray {
    val resources = context.resources
    val displayMetrics = resources.displayMetrics
    val widthPixels = displayMetrics.xdpi
    val heightPixels = displayMetrics.ydpi
    return floatArrayOf(widthPixels, heightPixels)
}

/**
 * @return 获取状态栏高度
 */
fun getStatusBarHeight(context: Context): Int {
    val resources = context.resources
    val identifier = resources.getIdentifier("status_bar_height", "dimen", "android")
    return resources.getDimensionPixelSize(identifier)
}

/**
 * @return 根据手机的分辨率从 dp 的单位 转成为 px(像素)
 */
fun Float.dip2px(context: Context): Int {
    val scale = context.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

/**
 * @return 根据手机的分辨率从 px(像素) 的单位 转成为 dp
 */
fun Float.px2dip(context: Context): Int {
    val scale = context.resources.displayMetrics.density
    return (this / scale + 0.5f).toInt()
}

/**
 * view点击添加防抖动
 */
fun View.clicks(): Observable<Unit> {
    return this.clicks().throttleFirst(CLICK_TIME, TimeUnit.MILLISECONDS)
}


/**
 * @return toJson
 */
fun Any.toJson(): String {
    return gson.toJson(this)
}


/**
 * @return 解析Json
 */
inline fun <reified T> String.fromJson(typeOfT: Type = T::class.java): T {

    return gson.fromJson(this, typeOfT)
}

/**
 * @return 获取文件夹下所有文件路径
 */
fun getFilePath(
    path: String = "${Environment.getExternalStorageDirectory()}/MFiles/picture",
    mutableListOf: MutableList<String> = mutableListOf()
): MutableList<String> {
    //val file = File("${Environment.getExternalStorageDirectory()}/MFiles/picture")
    //val file = File("${Environment.getExternalStorageDirectory()}/DCIM/Camera")
    val file = File(path)
    if (file.isDirectory) {
        val listFiles = file.listFiles()
        listFiles?.let {
            for (mFiles in listFiles) {
                if (mFiles.isDirectory) {
                    getFilePath(mFiles.absolutePath, mutableListOf)
                } else {
                    mutableListOf.add(mFiles.absolutePath)
                }

            }
        }
    } else {
        mutableListOf.add(file.absolutePath)
    }

    return mutableListOf
}

fun MutableList<String>.filterEmptyFile():MutableList<String>{
    return this.filterNot {
        val file = File(it)
        !file.exists()
    }.toMutableList()
}

/**
 * @return 获取文件夹下所有文件夹路径
 */
fun getDirectoryName(
    path: String = "${Environment.getExternalStorageDirectory()}/MFiles/picture/A",
    mutableListOf: MutableList<File> = mutableListOf()
): MutableList<File> {
    val file = File(path)
    if (file.isDirectory) {
        val listFiles = file.listFiles()
        listFiles?.forEach {
            if (it.isDirectory) {
                getDirectoryName(it.path, mutableListOf)
                Log.i(TAG, "getDirectoryName: ${it.name}  ${it.path}")
                mutableListOf.add(it)
            }
        }
    }
    return mutableListOf
}


/**
 * @return 获取getDefaultMMKV
 */
fun getDefaultMMKV(): MMKV {
    return MMKV.defaultMMKV()
}

/**
 * @return 获取用户缓存
 */
fun getUserInfo(): UserInfoData? {
    val userInfo = getDefaultMMKV().decodeString(AppConstant.Constant.USER_INFO, "")
    if (!userInfo.isNullOrEmpty()) {
        return Gson().fromJson<UserInfoData>(userInfo, UserInfoData::class.java)
    }
    return null
}
/**
 * @return 更新用户缓存
 */
fun updateUserInfo(userInfoData:UserInfoData) {
    getDefaultMMKV().encode(AppConstant.Constant.USER_INFO, userInfoData.toJson())
}

fun getCurrentUserId():String{
    val userInfo = getUserInfo()
    return userInfo?.id?:""
}

/**
 * @return 返回xx,xx
 */
fun MutableList<String>.formatWithSymbol(symbol: String = ","): String {
    val stringBuilder = StringBuilder()
    this.forEachIndexed { index, s ->
        if (index == this.size - 1) {
            stringBuilder.append(s)
        } else {
            stringBuilder.append(s).append(symbol)
        }
    }
    return stringBuilder.toString()
}

/**
 * @return 解析xx,xx
 */
fun String.symbolToList(symbol: String = ","): MutableList<String> {
    val mutableListOf = mutableListOf<String>()
    val split = this.split(symbol)
    mutableListOf.addAll(split)
    return mutableListOf
}

/**
 * 不行 content://com.android.externalstorage.documents/document/primary%3AMFiles%2Fpicture%2F1638856053728_a.jpg
 * 可以 content://com.miui.gallery.open/raw/%2Fstorage%2Femulated%2F0%2FPictures%2F20211223_092453.jpg
 * @return uri2path
 */
fun uri2path(context: Context, uri: Uri): String {
    var path = ""
    val contentResolver = context.contentResolver
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val query =
        contentResolver.query(uri, projection, null, null, null)
    try {
        query?.let {
            val columnIndex = query.getColumnIndex(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            path = query.getString(columnIndex)
            Log.i(TAG, "uri2path: $path")
            query.close()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Log.i(TAG, "uri2path: ${e.message}")
    }

    return path
}

/**
 * @return 获取app VersionCode
 */
fun getVersionCode(context: Context): Int {
    val packageManager = context.packageManager
    val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
    return packageInfo.versionCode

}

/**
 * @return 获取app VersionName
 */
fun getVersionName(context: Context): String {
    val packageManager = context.packageManager
    val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
    return packageInfo.versionName
}

/**
 * @return 获取指定文件大小
 */
fun getAllFileSize(file: File): Long {
    var size = 0L
    if (file.isDirectory) {
        val listFiles = file.listFiles()
        listFiles?.let {
            if (listFiles.isEmpty()) {
                return 0
            }
            for (mFile in listFiles) {
                size += if (mFile.isDirectory) {
                    getAllFileSize(mFile)
                } else {
                    mFile.length()
                }
            }
        }
    } else {
        size = file.length()
    }
    return size
}

/**
 * @return 格式化文件大小格式
 */
fun formatSize(size: Long): String {
    Log.i(TAG, "formatSize: $size")

    val k = size.toFloat() / 1024
    if (k < 1) {
        return "0K"
    }
    val m = k / 1024

    if (m < 1) {
        return DecimalFormat("0.00K").format(k)
    }

    val g = m / 1024

    if (g < 1) {
        return DecimalFormat("0.00M").format(m)
    }

    val t = g / 1024

    if (t < 1) {
        return DecimalFormat("0.00G").format(g)
    }
    val t1 = t / 1024

    if (t1 < 1) {
        return DecimalFormat("0.00G").format(t)
    }
    return DecimalFormat("0.00T").format(t1)
}

/**
 * @return 删除文件夹
 */
fun deleteDirectory(file: File,context: Context) {
    try {
        if (file.isDirectory) {
            file.listFiles()?.let {
                if (it.isNotEmpty()) {
                    for (mFile in it) {
                        if (mFile.isDirectory) {
                            deleteDirectory(file,context)
                        } else {
                            toDeleteFile(mFile,context)
                        }
                    }
                }
            }
        } else {
            toDeleteFile(file,context)
        }
    }catch (e:Exception){
        e.printStackTrace()
    }

}

/**
 * @return 删除文件
 */
fun toDeleteFile(file: File, context: Context) {
    val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    val contentResolver = context.contentResolver
    val url = MediaStore.Images.Media.DATA + "=?"
    val delete = contentResolver.delete(uri, url, arrayOf(file.absolutePath))
    if (delete == 0){
        if (file.exists()) {
            file.delete()
        }
    }
}


/**
 * 跳转登录页
 */
fun buildARouterLogin(mContext: Context){
    buildARouter(AppConstant.RoutePath.LOGIN_ACTIVITY)
        .withOptionsCompat(ActivityOptionsCompat.makeCustomAnimation(mContext, R.anim.bottom_in, R.anim.bottom_out))
        .withInt(AppConstant.Constant.DATA,0)
        .navigation(mContext)
}

/**
 * 是否是手机号
 */
fun String.isPhone():Boolean{
    val pattern = Pattern.compile("^1[0-9]{10}")
    val matcher = pattern.matcher(this)
    return matcher.matches()
}

/**
 *
 */
fun toCloseAd(vipLevel:Int):Boolean{
    val userInfo = getUserInfo()
    userInfo?.let {
        /*如果过期了返回*/
        if (it.userVipExpired){
            return false
        }
        if (it.userVipLevel >= vipLevel){
            return true
        }
    }
    return false
}

/**
 * 关闭键盘
 * @param context
 * @param window
 */
fun hideSoftInput(context: Context,view: View){
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken,0)
}


fun createAppId(updateAppId:String = "",path:String = "/storage/emulated/0/Android/"):String{
    val file = File(path, "._MieMieAppId.txt")
    var appId = "MieMie_${System.currentTimeMillis()}_${UUID.randomUUID().toString().replace("-","")}"
    if (!TextUtils.isEmpty(updateAppId)){
        appId = updateAppId
    }
    if (!file.exists()){
        file.createNewFile()
        val fileInputStream = FileOutputStream(file)
        fileInputStream.write(appId.toByteArray())
    }else{
        if (!TextUtils.isEmpty(updateAppId)){
            val fileInputStream = FileOutputStream(file)
            fileInputStream.write(appId.toByteArray())
        }
    }
    return appId
}

fun getAppId(path:String = "/storage/emulated/0/Android/"):String{
    val file = File(path, "._MieMieAppId.txt")
    if (file.exists()){
        val fileInputStream = FileInputStream(file)
        val byteArrayOf = ByteArray(fileInputStream.available())
        fileInputStream.read(byteArrayOf)
        return String(byteArrayOf)
    }

    return createAppId()
}


fun getUriWithPath(context: Context, filePath: String): Uri {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        //7.0以上的读取文件uri要用这种方式了
        FileProvider.getUriForFile(
            context.applicationContext,
            "com.yang.miemie.wallpaper.fileProvider",
            File(filePath)
        )
    } else {
        Uri.fromFile(File(filePath))
    }
}

fun save2Album(source: File?, dirName: String?,context: Context){

    if (source == null) {
        showShort("保存失败")
        return
    }
    try {
        val safeDirName =
            if (TextUtils.isEmpty(dirName)) context.packageName else dirName!!
        val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),safeDirName)
        if (!dir.exists()) dir.mkdirs()
        val destFile :File = if(source.name.endsWith(".mp4")){
            File(dir, System.currentTimeMillis().toString() + "." + "mp4")
        }else{
            File(dir, System.currentTimeMillis().toString() + "." +  getImageType(source).value )
        }

        if (Build.VERSION.SDK_INT < 29) {
            if (destFile.exists()) destFile.delete()
            destFile.createNewFile()
            FileOutputStream(destFile).use { out ->
                writeFileFromIS(out, FileInputStream(source))
            }
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            intent.data = Uri.parse("file://" + destFile.absolutePath)
            context.sendBroadcast(intent)
        } else {
            //android10以上，增加了新字段，自己insert，因为RELATIVE_PATH，DATE_EXPIRES，IS_PENDING是29新增字段
            val contentValues = ContentValues()

            val contentUri: Uri
            if (source.name.endsWith(".mp4")){
                contentValues.put(MediaStore.Video.Media.DISPLAY_NAME, destFile.name)
                contentValues.put(MediaStore.Video.Media.MIME_TYPE, "video/*")
                contentUri = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else {
                    MediaStore.Video.Media.INTERNAL_CONTENT_URI
                }
                contentValues.put(
                    MediaStore.Video.Media.RELATIVE_PATH,
                    Environment.DIRECTORY_DCIM + "/" + context.packageName
                )
            }else{
                contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, destFile.name)
                contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/*")
                contentUri = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else {
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI
                }
                contentValues.put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    Environment.DIRECTORY_DCIM + "/" + context.packageName
                )
            }


            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 1)
            val uri = context.contentResolver.insert(contentUri, contentValues)
            if (uri == null) {
                showShort("保存失败")
                return
            }
            val resolver = context.contentResolver
            resolver.openOutputStream(uri).use { out ->
                writeFileFromIS(out!!, FileInputStream(source))
            }
            // Everything went well above, publish it!
            contentValues.clear()
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
            //contentValues.putNull(MediaStore.MediaColumns.DATE_EXPIRES);
            resolver.update(uri, contentValues, null, null)
        }
        showShort("已保存到系统相册")
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        showShort("保存失败")
    }

}


private fun writeFileFromIS(fos: OutputStream, `is`: InputStream): Boolean {
    var os: OutputStream? = null
    return try {
        os = BufferedOutputStream(fos)
        val data = ByteArray(8192)
        var len: Int
        while (`is`.read(data, 0, 8192).also { len = it } != -1) {
            os.write(data, 0, len)
        }
        true
    } catch (e: IOException) {
        e.printStackTrace()
        false
    } finally {
        try {
            `is`.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            os?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

fun String.isVideo():Boolean{

    return this.endsWith(".mp4",true)

}
fun String.isImage():Boolean{

    return this.endsWith(".jpg",true) || this.endsWith(".png",true)
            || this.endsWith(".gif",true)
            || this.endsWith(".tiff",true)
            || this.endsWith(".bmp",true)
            || this.endsWith(".webp",true)
            || this.endsWith(".ico",true)

}

fun getRandomColor(): Int {
    val buffer = StringBuffer()
    for (i in 0..5) {
        buffer.append(arr[(Math.random() * arr.size).toInt()])
    }
    return Color.parseColor("#$buffer")
}


fun Context.getColor(color:Int){
    ContextCompat.getColor(this,color)
}



fun <T> SmartRefreshLayout.smartRefreshLayoutData(data:MutableList<T>?, adapter: BaseQuickAdapter<T, BaseViewHolder>,mViewModel:BaseViewModel, emptyView:Int = R.layout.view_empty_data){
    when {
        this.isRefreshing -> {
            mViewModel.uC.refreshEvent.call()
            if (data.isNullOrEmpty()) {
                mViewModel.showRecyclerViewEmptyEvent()
            } else {
                adapter.replaceData(data)
                this.setNoMoreData(false)
            }
        }
        this.isLoading -> {
            mViewModel.uC.loadMoreEvent.call()
            if (data.isNullOrEmpty()) {
                this.setNoMoreData(true)
            } else {
                this.setNoMoreData(false)
                adapter.addData(data)
            }
        }
        else -> {
            if (data.isNullOrEmpty()) {
                mViewModel.showRecyclerViewEmptyEvent()
            } else {
                adapter.replaceData(data)
                this.setNoMoreData(false)
            }
        }
    }

}


