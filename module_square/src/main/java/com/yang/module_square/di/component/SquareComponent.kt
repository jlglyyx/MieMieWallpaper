package com.yang.module_square.di.component

import com.yang.lib_common.remote.di.component.RemoteComponent
import com.yang.lib_common.scope.ActivityScope
import com.yang.module_square.di.factory.SquareViewModelFactory
import com.yang.module_square.di.module.SquareModule
import com.yang.module_square.ui.fragment.SquareFragment
import com.yang.module_square.ui.fragment.SquareItemFragment
import dagger.Component

/**
 * @ClassName: SquareComponent
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/21 14:24
 */
@ActivityScope
@Component(modules = [SquareModule::class],dependencies = [RemoteComponent::class])
interface SquareComponent {

    fun provideMineViewModelFactory(): SquareViewModelFactory


    fun inject(inject: SquareFragment)

    fun inject(inject: SquareItemFragment)
//
//    fun inject(inject: MyFansFragment)
//
//    fun inject(inject: MyCollectionFragment)
//
//    fun inject(inject: MyDownFragment)
//
//    fun inject(inject: MineSettingActivity)
//
//    fun inject(inject: MineChangePasswordActivity)
//
//    fun inject(inject: MineChangePhoneActivity)
//
//    fun inject(inject: MineUserInfoActivity)
//
//    fun inject(inject: MineChangeUserInfoActivity)
}