package com.yang.module_task.api

import com.yang.lib_common.remote.di.response.MResult
import com.yang.module_task.data.TaskProgressData
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @ClassName: TaskApi
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/21 14:16
 */
interface TaskApi {

    @POST("api/user/query/task")
    suspend fun getTaskList(@Body params:MutableMap<String,Any>):MResult<MutableList<TaskProgressData>>
}