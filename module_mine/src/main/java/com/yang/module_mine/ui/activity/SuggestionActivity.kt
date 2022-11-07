package com.yang.module_mine.ui.activity

import androidx.core.widget.doOnTextChanged
import com.alibaba.android.arouter.facade.annotation.Route
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.util.buildARouter
import com.yang.lib_common.util.clicks
import com.yang.lib_common.util.showShort
import com.yang.module_mine.databinding.ActAboutBinding
import com.yang.module_mine.databinding.ActSuggestionBinding

/**
 * @ClassName: AboutActivity
 * @Description:
 * @Author: yxy
 * @Date: 2022/8/4 13:36
 */
@Route(path = AppConstant.RoutePath.MINE_SUGGESTION_ACTIVITY)
class SuggestionActivity : BaseActivity<ActSuggestionBinding>() {

    override fun initViewBinding(): ActSuggestionBinding {
        return bind(ActSuggestionBinding::inflate)
    }

    override fun initData() {

    }

    override fun initView() {
        mViewBinding.apply {
            stvCommit.clicks().subscribe {
                showShort("感谢您的反馈")
                finish()
            }
            setSuggestion.doOnTextChanged { text, start, before, count ->
                val length = setSuggestion.text.toString().length
                stvCount.text = "$length/150"
                stvCommit.isEnabled = length > 0
            }
        }


    }

    override fun initViewModel() {

    }
}