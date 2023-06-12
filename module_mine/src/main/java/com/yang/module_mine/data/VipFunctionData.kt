package com.yang.module_mine.data

/**
 * @ClassName: VipFunctionData
 * @Description:
 * @Author: yxy
 * @Date: 2022/11/16 14:26
 */
class VipFunctionData {
}

data class VipPackageData(var id:String,var desc:String,var time:String,var timeDesc:String,var price:String,var originalPrice:String,var isSelect:Boolean = false)

data class PayTypeData(var id:String,var desc:String,var imgResourceId:Int,var balance:String,var isSelect:Boolean = false)

data class VipRightsData(var icon:Int,var name:String)