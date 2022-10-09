package com.yang.module_task.repository

import com.yang.lib_common.base.repository.BaseRepository
import com.yang.lib_common.remote.di.response.MResult
import com.yang.module_task.api.TaskApi
import com.yang.module_task.data.TaskProgressData
import javax.inject.Inject

/**
 * @ClassName: TaskRepository
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/14 11:36
 */
class TaskRepository @Inject constructor(private val taskApi: TaskApi) :BaseRepository(){




    suspend fun getTaskList(params:MutableMap<String,Any>): MResult<MutableList<TaskProgressData>> {
        return withContextIO {
            taskApi.getTaskList(params)
        }
    }
}