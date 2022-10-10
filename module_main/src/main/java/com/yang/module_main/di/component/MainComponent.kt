package com.yang.module_main.di.component

import com.yang.lib_common.remote.di.component.RemoteComponent
import com.yang.lib_common.scope.ActivityScope
import com.yang.module_main.ui.activity.MainActivity
import com.yang.module_main.di.factory.MainViewModelFactory
import com.yang.module_main.di.module.MainModule
import com.yang.module_main.ui.fragment.MainFragment
import com.yang.module_main.ui.fragment.MainItemFragment
import dagger.Component

/**
 * @ClassName: Compent
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/21 14:24
 */
@ActivityScope
@Component(modules = [MainModule::class],dependencies = [RemoteComponent::class])
interface MainComponent {

    fun provideMainViewModelFactory(): MainViewModelFactory

    fun inject(inject: MainActivity)

//    fun inject(inject: LoginActivity)
//
//    fun inject(inject: AddTaskActivity)
//
//    fun inject(inject: TaskDetailActivity)


    fun inject(inject: MainFragment)

    fun inject(inject: MainItemFragment)
}