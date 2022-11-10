package com.yang.lib_common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.yang.lib_common.databinding.ViewErrorReLoadDataBinding
import com.yang.lib_common.util.clicks

/**
 * @ClassName: ErrorReLoadView
 * @Description:
 * @Author: yxy
 * @Date: 2022/11/8 16:33
 */
class ErrorReLoadView : ConstraintLayout {


    enum class Status(var status: Int) {

        LOADING(0),

        ERROR(1),

        NORMAL(2)
    }

    var status = Status.LOADING
        set(value) {
            field = value
            showHideError(field)
        }


    var onClick = {}

    private val mBinding by lazy {
        ViewErrorReLoadDataBinding.inflate(LayoutInflater.from(context), this, false)
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mBinding.clContainer.setOnClickListener {
            onClick()
        }
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        addView(mBinding.root)
        showHideError(Status.LOADING)
    }



    private fun showHideError(show: Status) {
        when(show){
            Status.LOADING ->{
                this.mBinding.llLoadingContainer.visibility = VISIBLE
                this.mBinding.llReLoadContainer.visibility = GONE
                this.mBinding.root.visibility = VISIBLE
            }
            Status.ERROR ->{
                this.mBinding.llReLoadContainer.visibility = VISIBLE
                this.mBinding.llLoadingContainer.visibility = GONE
                this.mBinding.root.visibility = VISIBLE
            }
            Status.NORMAL ->{
                this.mBinding.root.visibility = GONE
            }
        }

    }
}