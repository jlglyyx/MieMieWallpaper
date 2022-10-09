package com.yang.module_task.di.component

import com.yang.lib_common.remote.di.component.RemoteComponent
import com.yang.lib_common.scope.ActivityScope
import com.yang.module_task.di.factory.TaskViewModelFactory
import com.yang.module_task.di.module.TaskModule
import com.yang.module_task.ui.fragment.TaskFragment

import dagger.Component

/**
 * @ClassName: TaskComponent
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/21 14:24
 */
@ActivityScope
@Component(modules = [TaskModule::class],dependencies = [RemoteComponent::class])
interface TaskComponent {

    fun provideTaskViewModelFactory(): TaskViewModelFactory

    fun inject(inject: TaskFragment)

}