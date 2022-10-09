@file:JvmName("TaskDaggerHelp")
package com.yang.module_task.helper

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.yang.lib_common.helper.getRemoteComponent
import com.yang.module_task.di.component.DaggerTaskComponent
import com.yang.module_task.di.module.TaskModule
import com.yang.module_task.di.component.TaskComponent


private const val TAG = "TaskDaggerHelp.kt"

fun getTaskComponent(activity: AppCompatActivity): TaskComponent {
    return DaggerTaskComponent.builder().remoteComponent(getRemoteComponent())
        .taskModule(TaskModule()).build()
}
fun getTaskComponent(fragment: Fragment): TaskComponent {
    return DaggerTaskComponent.builder().remoteComponent(getRemoteComponent())
        .taskModule(TaskModule()).build()
}
