package com.yang.lib_common.remote.di.response

//todo 后续删除
data class MListResult<T : Any>(
    val pageNum: Int, val pageSize: Int,val size : Int,val total:Int,val list:MutableList<T>
)

