@file:JvmName("MainDaggerHelp")
package com.yang.module_main.helper

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.yang.lib_common.helper.getRemoteComponent
import com.yang.module_main.di.module.MainModule
import com.yang.module_main.di.component.DaggerMainComponent
import com.yang.module_main.di.component.MainComponent


private const val TAG = "MainDaggerHelp.kt"

fun getMainComponent(activity: AppCompatActivity): MainComponent {
    return DaggerMainComponent.builder().remoteComponent(getRemoteComponent())
        .mainModule(MainModule()).build()
}
fun getMainComponent(fragment: Fragment): MainComponent {
    return DaggerMainComponent.builder().remoteComponent(getRemoteComponent())
        .mainModule(MainModule()).build()
}
