package com.yang.lib_common.widget

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.widget.doOnTextChanged
import com.hjq.shape.layout.ShapeLinearLayout
import com.yang.lib_common.R
import com.yang.lib_common.databinding.ViewCustomEditBinding
import com.yang.lib_common.util.dip2px

/**
 * @ClassName: CustomEditView
 * @Description:
 * @Author: yxy
 * @Date: 2022/11/7 11:41
 */
class CustomEditView : LinearLayout {

    private var passVisible = false

    private val mBinding by lazy {
        ViewCustomEditBinding.inflate(LayoutInflater.from(context),this,true)
    }

    constructor(context: Context) : this(context,null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){

        val mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomEditView)
        val ceHint = mTypedArray.getString(R.styleable.CustomEditView_ceHint)
        val ceType = mTypedArray.getInt(R.styleable.CustomEditView_ceType,-1)
        val ceMaxLength = mTypedArray.getInt(R.styleable.CustomEditView_ceMaxLength,-1)
        val ceLeftSrc = mTypedArray.getResourceId(R.styleable.CustomEditView_ceLeftSrc,-1)


        mTypedArray.recycle()

        mBinding.apply {
            when(ceType){
                -1 ->{
                    ivLeft.visibility = View.GONE
                    ivRight.visibility = View.GONE
                    setInput.doOnTextChanged { text, start, before, count ->
                        if (text.isNullOrEmpty()){
                            sivClear.visibility = View.GONE
                        }else{
                            sivClear.visibility = View.VISIBLE
                        }
                    }

                    setInput.setOnFocusChangeListener { v, hasFocus ->
                        if (hasFocus){
                            sivClear.visibility = View.VISIBLE
                        }else{
                            sivClear.visibility = View.GONE
                        }
                    }


                    sivClear.setOnClickListener {
                        mBinding.setInput.setText("")
                    }
                }
                1 ->{
                    ivRight.visibility = View.GONE
                    setInput.doOnTextChanged { text, start, before, count ->
                        if (text.isNullOrEmpty()){
                            sivClear.visibility = View.GONE
                        }else{
                            sivClear.visibility = View.VISIBLE
                        }
                    }

                    setInput.setOnFocusChangeListener { v, hasFocus ->
                        if (hasFocus){
                            sivClear.visibility = View.VISIBLE
                        }else{
                            sivClear.visibility = View.GONE
                        }
                    }

                    sivClear.setOnClickListener {
                        mBinding.setInput.setText("")
                    }
                }
                2 ->{
                    ivRight.visibility = View.VISIBLE
                    setInput.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
                    ivRight.setOnClickListener {
                        if (passVisible){
                            setInput.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
                            ivRight.setImageResource(R.drawable.iv_password_gone)
                        }else{
                            setInput.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD or InputType.TYPE_CLASS_TEXT
                            ivRight.setImageResource(R.drawable.iv_password_visibility)
                        }
                        passVisible = !passVisible
                        setInput.setSelection(setInput.text?.length?:0)

                    }

                }
            }

            setInput.hint = ceHint

            if (ceMaxLength != -1){
                setInput.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(ceMaxLength))
            }
            if (ceLeftSrc != -1){
                ivLeft.setImageResource(ceLeftSrc)
            }


        }

    }

    fun setText(text:String){
        mBinding.setInput.setText(text)
    }
    fun getText():String{
        return mBinding.setInput.text.toString()
    }
    fun getEdit():EditText{
        return mBinding.setInput
    }
    fun getContainer():ShapeLinearLayout{
        return mBinding.llContainer
    }

    fun setRadius(radius:Float){
        mBinding.llContainer.shapeDrawableBuilder.setRadius(radius.dip2px(context).toFloat()).intoBackground()
    }
}