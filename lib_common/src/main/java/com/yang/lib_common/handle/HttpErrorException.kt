package com.yang.lib_common.handle

import java.lang.Exception

/**
 * @ClassName: HttpErrorException
 * @Description:
 * @Author: yxy
 * @Date: 2022/11/27 18:18
 */
class HttpErrorException(override var message: String, var code: String) : Exception() {


}