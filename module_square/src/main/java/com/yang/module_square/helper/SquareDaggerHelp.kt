@file:JvmName("SquareDaggerHelp")
package com.yang.module_square.helper

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.yang.module_square.di.module.SquareModule
import com.yang.lib_common.helper.getRemoteComponent
import com.yang.module_square.di.component.DaggerSquareComponent
import com.yang.module_square.di.component.SquareComponent


private const val TAG = "SquareDaggerHelp.kt"

fun getSquareComponent(activity: AppCompatActivity): SquareComponent {
    return DaggerSquareComponent.builder().remoteComponent(getRemoteComponent())
        .squareModule(SquareModule()).build()
}
fun getSquareComponent(fragment: Fragment): SquareComponent {
    return DaggerSquareComponent.builder().remoteComponent(getRemoteComponent())
        .squareModule(SquareModule()).build()
}
