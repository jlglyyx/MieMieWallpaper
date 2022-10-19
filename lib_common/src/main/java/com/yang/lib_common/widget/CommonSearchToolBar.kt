package com.yang.lib_common.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import com.google.android.material.imageview.ShapeableImageView
import com.yang.lib_common.R
import com.yang.lib_common.databinding.ViewCommonSearchToolbarBinding
import com.yang.lib_common.util.clicks
import com.yang.lib_common.util.getStatusBarHeight

class CommonSearchToolBar : ConstraintLayout {

    var imageBackCallBack: ImageBackCallBack? = null

    var rightContentCallBack: RightContentCallBack? = null

    var imageAddCallBack: ImageAddCallBack? = null


    lateinit var ivBack: ImageView


    lateinit var ivAdd: ImageView

    lateinit var etSearch: EditText

    lateinit var tvRight: TextView

    var searchListener: OnSearchListener? = null

    interface OnSearchListener {
        fun onSearch(text: String)
    }


    interface ImageBackCallBack {
        fun imageBackClickListener()
    }
    interface RightContentCallBack {
        fun rightContentClickListener()
    }

    interface ImageAddCallBack {
        fun imageAddClickListener()
    }


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs!!)
    }


    private fun init(context: Context, attrs: AttributeSet) {
        val inflate =
            LayoutInflater.from(context).inflate(R.layout.view_common_search_toolbar, this)
        val llToolbar = inflate.findViewById<LinearLayout>(R.id.ll_container)
        llToolbar.setPadding(0, getStatusBarHeight(context), 0, 0)
        ivBack = inflate.findViewById(R.id.iv_back)
        ivAdd = inflate.findViewById(R.id.iv_add)
        etSearch = inflate.findViewById(R.id.et_search)
        tvRight = inflate.findViewById(R.id.tv_right)
        var sivClear = inflate.findViewById<ImageView>(R.id.siv_clear)


        val obtainStyledAttributes =
            context.obtainStyledAttributes(attrs, R.styleable.CommonToolBar)
        val leftImgVisible =
            obtainStyledAttributes.getBoolean(R.styleable.CommonToolBar_leftImgVisible, true)
        val leftImgSrc =
            obtainStyledAttributes.getResourceId(R.styleable.CommonToolBar_leftImgSrc, 0)

        val rightImgVisible =
            obtainStyledAttributes.getBoolean(R.styleable.CommonToolBar_rightImgVisible, false)
        val rightImgSrc =
            obtainStyledAttributes.getResourceId(R.styleable.CommonToolBar_rightImgSrc, 0)
        val toolbarBg = obtainStyledAttributes.getResourceId(
            R.styleable.CommonToolBar_toolbarBg, 0
        )
        val rightContent = obtainStyledAttributes.getString(
            R.styleable.CommonToolBar_rightContent
        )


        if (toolbarBg != 0) {
            llToolbar.setBackgroundResource(toolbarBg)
        }
        if (leftImgVisible) {
            ivBack.visibility = View.VISIBLE
        } else {
            ivBack.visibility = View.GONE
        }

        if (leftImgSrc != 0) {
            ivBack.setImageResource(leftImgSrc)
        }
        if (rightImgVisible) {
            ivAdd.visibility = View.VISIBLE
        } else {
            ivAdd.visibility = View.GONE
        }
        if (rightImgSrc != 0) {
            ivAdd.setImageResource(rightImgSrc)
        }

        if (!rightContent.isNullOrEmpty()){
            tvRight.text = rightContent
            tvRight.visibility = View.VISIBLE
            ivAdd.visibility = View.GONE
        }
        ivBack.clicks().subscribe {
            if (null != imageBackCallBack) {
                imageBackCallBack?.imageBackClickListener()
            } else {
                (context as Activity).finish()
            }
        }
        tvRight.clicks().subscribe {
            if (null != rightContentCallBack) {
                rightContentCallBack?.rightContentClickListener()
            } else {
                (context as Activity).finish()
            }
        }

        etSearch.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()){
                sivClear.visibility = View.GONE
            }else{
                sivClear.visibility = View.VISIBLE
            }
        }

        sivClear.setOnClickListener {
            etSearch.setText("")
        }

        ivAdd.clicks().subscribe {
            imageAddCallBack?.imageAddClickListener()
        }

        obtainStyledAttributes.recycle()

        etSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                val trim = etSearch.text.toString().trim()
//                if (TextUtils.isEmpty(trim)) {
//                    showToast("请输入搜索内容")
//                    return@setOnEditorActionListener false
//                }
                searchListener?.onSearch(trim)
                return@setOnEditorActionListener true
            }

            return@setOnEditorActionListener false
        }
    }

    fun canEdit(can: Boolean) {
        etSearch.isFocusable = can
        etSearch.isCursorVisible = can
        etSearch.isFocusableInTouchMode = can
    }

}