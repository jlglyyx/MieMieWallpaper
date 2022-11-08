package com.yang.module_main.viewmodel

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.yang.lib_common.base.viewmodel.BaseViewModel
import com.yang.lib_common.bus.event.LiveDataBus
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.util.showShort
import com.yang.lib_common.util.updateUserInfo
import com.yang.lib_common.data.WallpaperData
import com.yang.lib_common.data.WallpaperTabData
import com.yang.lib_common.util.setMMKVValue
import com.yang.module_main.repository.MainRepository
import javax.inject.Inject

/**
 * @ClassName: MainViewModel
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/21 14:13
 */
class MainViewModel @Inject constructor(
    application: Application,
    private val mainRepository: MainRepository
) : BaseViewModel(application) {

    var loginType = 0

    var phoneText = ""

    var passwordText = ""

    var verificationText = ""

    var checkStatus = true

    var pageNum = 1

    var loginUserType = 0

    var order = 0

//    val taskLiveData = MutableLiveData<MutableList<TaskData>>()

    val pictureListLiveData = MutableLiveData<MutableList<String>>()

    val mWallpaperData = MutableLiveData<MutableList<WallpaperData>>()

    val mWallpaperTabData = MutableLiveData<MutableList<WallpaperTabData>>()
//
//    fun getA() {
//        launch({
//            mainRepository.getA()
//        }, {
//            //showShort(it)
//        })
//    }
//
//
    fun login() {
        if (TextUtils.isEmpty(phoneText)) {
            showShort("请输入手机号")
            return
        }
        if (loginType == 1) {
            if (TextUtils.isEmpty(passwordText)) {
                showShort("请输入密码")
                return
            }
        } else {
            if (TextUtils.isEmpty(verificationText)) {
                showShort("请输入验证码")
                return
            }
        }
        if (!checkStatus) {
            showShort("请勾选协议")
            return
        }
//        MobSDK.submitPolicyGrantResult(checkStatus)
        launch({
            val params = mutableMapOf<String,Any>()
            params[AppConstant.Constant.USER_ACCOUNT] = phoneText
            if (loginType == 1) {
                params[AppConstant.Constant.USER_PASSWORD] = passwordText

            }else{
                params[AppConstant.Constant.VERIFICATION] = verificationText
            }

           mainRepository.login(params)
        }, {
            updateUserInfo(it.data)
            setMMKVValue(AppConstant.Constant.LOGIN_STATUS,AppConstant.Constant.LOGIN_SUCCESS)
            LiveDataBus.instance.with(AppConstant.Constant.LOGIN_STATUS).postValue(AppConstant.Constant.LOGIN_SUCCESS)
            finishActivity()
        },{

        },messages = arrayOf("登录中...","登录成功!"))
    }


    fun getTabs(wallType:Int){
        launch({
            mainRepository.getTabs(wallType)
        },{
            mWallpaperTabData.postValue(it.data)
        },{
          requestFail()
        },errorDialog = false)
    }



    fun getWallpaper(tabId:String?,keyword:String = ""){
        launch({
            if (TextUtils.isEmpty(keyword)){
                params[AppConstant.Constant.ORDER] = order
                params["tabId"] = tabId
            }else{
                params[AppConstant.Constant.KEYWORD] = keyword
            }
            params[AppConstant.Constant.PAGE_NUMBER] = pageNum
            params[AppConstant.Constant.PAGE_SIZE] = AppConstant.Constant.PAGE_SIZE_COUNT
            mainRepository.getWallpaper(params)
        },{
            mWallpaperData.postValue(it.data.list)
        },{
            cancelRefreshLoadMore()
            showRecyclerViewErrorEvent()
        },errorDialog = false)
    }



//
//
//    fun getUserInfo(id:String){
//        launch({
//            mainRepository.getUserInfo(id)
//        },{
//
//
//        },{
//            cancelRefreshLoadMore()
//            showRecyclerViewErrorEvent()
//        },errorDialog = false)
//    }
//    fun getTaskList(){
//        launch({
//            val params = mutableMapOf<String,Any>()
//            params[AppConstant.Constant.ID] = ""?:""
//            params[AppConstant.Constant.PAGE_NUMBER] = pageNum
//            params[AppConstant.Constant.PAGE_SIZE] = AppConstant.Constant.PAGE_SIZE_COUNT
//            mainRepository.getTaskList(params)
//        },{
//
//
//        },{
//            if(pageNum > 3){
//                taskLiveData.postValue(mutableListOf())
//            }else{
//                taskLiveData.postValue(mutableListOf<TaskData>().apply {
//                    add(TaskData().apply {
//                        userName = "张三"
//                        imageUrls =
//                            "https://img1.baidu.com/it/u=1834859148,419625166&fm=26&fmt=auto&gp=0.jpg" +
//                                    "#https://img2.baidu.com/it/u=3583098839,704145971&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=889" +
//                                    "#https://img1.baidu.com/it/u=1834859148,419625166&fm=26&fmt=auto&gp=0.jpg" +
//                                    "#https://img1.baidu.com/it/u=1834859148,419625166&fm=26&fmt=auto&gp=0.jpg" +
//                                    "#https://img1.baidu.com/it/u=1834859148,419625166&fm=26&fmt=auto&gp=0.jpg"
//                        taskTitle = "标题标题标题"
//                        taskContent =
//                            "存在某些设备上显示不全的情况。同时考虑到现在基本都是以dp为单位去做的适配，如果新的方案不支持dp，那么迁移成本也非常高。"
//                        taskNumber = 3
//                        taskNumbering = 1
//                        taskNumbered = 1
//                        taskStatus = "进行中"
//                        taskType = "补单任务"
//                        taskCommission = "6"
//                        userVipLevel = 5
//                        taskPersonList = mutableListOf<TaskPersonData>().apply {
//                            for (i in 0..30){
//                                add(TaskPersonData("$i","https://img2.baidu.com/it/u=3583098839,704145971&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=889",i%3))
//                            }
//
//                        }
//                    })
//
//                    add(TaskData().apply {
//                        userName = "李四"
//                        taskTitle="标题标题标题"
//                        taskContent = "首先来梳理下我们的需求，一般我们设计图都是以固定的尺寸来设计的。比如以分辨率1920px * 1080px来设计，以density为3来标注，也就是屏幕其实是640dp * 360dp。如果我们想在所有设备上显示完全一致，其实是不现实的，因为屏幕高宽比不是固定的，16:9、4:3甚至其他宽高比层出不穷，宽高比不同，显示完全一致就不可能了。但是通常下，我们只需要以宽或高一个维度去适配，比如我们Feed是上下滑动的，只需要保证在所有设备中宽的维度上显示一致即可，再比如一个不支持上下滑动的页面，那么需要保证在高这个维度上都显示一致，尤其不能存在某些设备上显示不全的情况。同时考虑到现在基本都是以dp为单位去做的适配，如果新的方案不支持dp，那么迁移成本也非常高。"
//                        imageUrls =
//                            "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4"
//                        taskNumber = 3
//                        taskNumbering = 1
//                        taskNumbered = 1
//                        taskStatus = "进行中"
//                        taskType = "补单任务"
//                        taskCommission = "10"
//                    })
//
//                    add(TaskData().apply {
//                        userName = "哇哈哈"
//                        taskTitle="标题标题标题"
//                        taskContent = "首先来梳理下我们的需求，一般我们设计图都是以固定的尺寸来设计的。比如以分辨率1920px * 1080px来设计，以density为3来标注，也就是屏幕其实是640dp * 360dp。如果我们想在所有设备上显示完全一致，其实是不现实的，因为屏幕高宽比不是固定的，16:9、4:3甚至其他宽高比层出不穷，宽高比不同，显示完全一致就不可能了。但是通常下，我们只需要以宽或高一个维度去适配，比如我们Feed是上下滑动的，只需要保证在所有设备中宽的维度上显示一致即可，再比如一个不支持上下滑动的页面，那么需要保证在高这个维度上都显示一致，尤其不能存在某些设备上显示不全的情况。同时考虑到现在基本都是以dp为单位去做的适配，如果新的方案不支持dp，那么迁移成本也非常高。"
//                        imageUrls =
//                            "https://img1.baidu.com/it/u=3222474767,386356710&fm=26&fmt=auto#https://img1.baidu.com/it/u=1834859148,419625166&fm=26&fmt=auto&gp=0.jpg#https://img2.baidu.com/it/u=3583098839,704145971&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=889"
//                        taskNumber = 6
//                        taskNumbering = 1
//                        taskNumbered = 1
//                        taskStatus = "进行中"
//                        taskType = "浏览任务"
//                        taskCommission = "7"
//                    })
//                })
//
//
//            }
//
////            cancelRefreshLoadMore()
////            showRecyclerViewErrorEvent()
//        },errorDialog = false)
//    }
//
//    fun insertTask(taskData: TaskData) {
//        launch({
//            mainRepository.insertTask(taskData)
//        }, {
//            LiveDataBus.instance.with("refresh_dynamic").value = "refresh_dynamic"
//            finishActivity()
//        }, messages = *arrayOf(getString(R.string.string_insert_dynamic_ing), getString(R.string.string_insert_dynamic_success), getString(R.string.string_insert_dynamic_fail)))
//    }
//
//    fun uploadFile(filePaths: MutableList<MediaInfoBean>) {
//        launch({
//            val mutableMapOf = mutableMapOf<String, RequestBody>()
//            filePaths.forEach {
//                val file = File(it.filePath.toString())
//                val requestBody = RequestBody.create(MediaType.parse(AppConstant.ClientInfo.CONTENT_TYPE), file)
//                val encode =
//                    URLEncoder.encode("${System.currentTimeMillis()}_${file.name}", AppConstant.ClientInfo.UTF_8)
//                mutableMapOf["file\";filename=\"$encode"] = requestBody
//            }
//            mainRepository.uploadFile(mutableMapOf)
//        }, {
//            pictureListLiveData.postValue(it.data)
//        }, messages = *arrayOf(getString(R.string.string_uploading), getString(R.string.string_insert_success)))
//    }
//
//
//    fun uploadFileAndParam(filePaths: MutableList<MediaInfoBean>) {
//        launch({
//            val mutableListOf = mutableListOf<RequestBody>()
//            filePaths.forEach {
//                val file = File(it.filePath.toString())
//                val requestBody = RequestBody.create(MediaType.parse(AppConstant.ClientInfo.CONTENT_TYPE), file)
//                val encode =
//                    URLEncoder.encode("${System.currentTimeMillis()}_${file.name}", AppConstant.ClientInfo.UTF_8)
//                val build = MultipartBody.Builder()
//                    .addFormDataPart("param", "".toJson())
//                    .addFormDataPart("file", encode, requestBody).build()
//                mutableListOf.add(build)
//            }
//
//            mainRepository.uploadFileAndParam(mutableListOf)
//        }, {
//            pictureListLiveData.postValue(it.data)
//        }, messages = *arrayOf(getString(R.string.string_uploading), getString(R.string.string_insert_success)))
//    }
}