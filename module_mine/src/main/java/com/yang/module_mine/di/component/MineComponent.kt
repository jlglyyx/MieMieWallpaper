package com.yang.module_mine.di.component

import com.yang.lib_common.remote.di.component.RemoteComponent
import com.yang.lib_common.scope.ActivityScope
import com.yang.module_mine.di.factory.MineViewModelFactory
import com.yang.module_mine.di.module.MineModule
import com.yang.module_mine.ui.activity.*
import com.yang.module_mine.ui.fragment.MineFragment
import com.yang.module_mine.ui.fragment.MyCollectionFragment
import com.yang.module_mine.ui.fragment.MyDownFragment
import com.yang.module_mine.ui.fragment.MyFansFragment
import dagger.Component

/**
 * @ClassName: Compent
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/21 14:24
 */
@ActivityScope
@Component(modules = [MineModule::class],dependencies = [RemoteComponent::class])
interface MineComponent {

    fun provideMineViewModelFactory(): MineViewModelFactory


    fun inject(inject: MineFragment)

    fun inject(inject: MyFansFragment)

    fun inject(inject: MyCollectionFragment)

    fun inject(inject: MyDownFragment)

    fun inject(inject: MineSettingActivity)

    fun inject(inject: MineChangePasswordActivity)

    fun inject(inject: MineChangePhoneActivity)

    fun inject(inject: MineUserInfoActivity)

    fun inject(inject: MineChangeUserInfoActivity)

    fun inject(inject: MineSquareActivity)
}