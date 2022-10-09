@file:JvmName("MineDaggerHelp")
package com.yang.module_mine.helper

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.yang.lib_common.helper.getRemoteComponent
import com.yang.module_mine.di.module.MineModule
import com.yang.module_mine.di.component.DaggerMineComponent
import com.yang.module_mine.di.component.MineComponent


private const val TAG = "MineDaggerHelp.kt"

fun getMineComponent(activity: AppCompatActivity): MineComponent {
    return DaggerMineComponent.builder().remoteComponent(getRemoteComponent())
        .mineModule(MineModule()).build()
}
fun getMineComponent(fragment: Fragment): MineComponent {
    return DaggerMineComponent.builder().remoteComponent(getRemoteComponent())
        .mineModule(MineModule()).build()
}
