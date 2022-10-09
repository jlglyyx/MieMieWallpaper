package com.yang.module_task.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.yang.lib_common.base.viewmodel.BaseViewModel
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.util.getDefaultMMKV
import com.yang.module_task.data.TaskProgressData
import com.yang.module_task.repository.TaskRepository
import javax.inject.Inject

/**
 * @ClassName: MineViewModel
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/21 14:13
 */
class TaskViewModel @Inject constructor(
    application: Application,
    private val taskRepository: TaskRepository
) : BaseViewModel(application) {


    var pageNum = 1;

    val taskLiveData = MutableLiveData<MutableList<TaskProgressData>>()

    val pictureListLiveData = MutableLiveData<MutableList<String>>()

    fun getTaskList() {
        launch({
            val params = mutableMapOf<String, Any>()
            params[AppConstant.Constant.ID] = "" ?: ""
            params[AppConstant.Constant.PAGE_NUMBER] = pageNum
            params[AppConstant.Constant.PAGE_SIZE] = AppConstant.Constant.PAGE_SIZE_COUNT
            taskRepository.getTaskList(params)
        }, {


        }, {
            if (pageNum > 3) {
                taskLiveData.postValue(mutableListOf())
            } else {
                taskLiveData.postValue(mutableListOf<TaskProgressData>().apply {
                    if (getDefaultMMKV().getInt(AppConstant.Constant.LOGIN_USER_TYPE, 0) == AppConstant.Constant.NUM_ZERO){
                        add(TaskProgressData(2, 0).apply {
                    })

                    add(TaskProgressData(2, 0).apply {
                    })

                    add(TaskProgressData(2, 0).apply {
                    })
                    }else{
                        add(TaskProgressData(0, 0).apply {
                            addSubItem(TaskProgressData(1,1))
                        })

                        add(TaskProgressData(0, 0).apply {
                            addSubItem(TaskProgressData(1,1))
                            addSubItem(TaskProgressData(1,1))
                            addSubItem(TaskProgressData(1,1))
                            addSubItem(TaskProgressData(1,1))
                        })

                        add(TaskProgressData(0, 0).apply {
                            addSubItem(TaskProgressData(1,1))
                            addSubItem(TaskProgressData(1,1))
                        })
                    }

                })


            }

//            cancelRefreshLoadMore()
//            showRecyclerViewErrorEvent()
        }, errorDialog = false)
    }

}