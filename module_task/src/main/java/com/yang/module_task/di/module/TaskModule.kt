package com.yang.module_task.di.module

import android.app.Application
import com.yang.lib_common.scope.ActivityScope
import com.yang.module_task.api.TaskApi
import com.yang.module_task.di.factory.TaskViewModelFactory
import com.yang.module_task.repository.TaskRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * @ClassName: MineModel
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/21 14:31
 */
@Module
class TaskModule {

    @ActivityScope
    @Provides
    fun provideTaskApi(retrofit: Retrofit): TaskApi {
        return retrofit.create(TaskApi::class.java)
    }

    @ActivityScope
    @Provides
    fun provideTaskRepository(taskApi: TaskApi): TaskRepository {
        return TaskRepository(taskApi)
    }

    @ActivityScope
    @Provides
    fun provideTaskViewModelFactory(
        application: Application,
        taskRepository: TaskRepository
    ): TaskViewModelFactory {
        return TaskViewModelFactory(application, taskRepository)
    }
}