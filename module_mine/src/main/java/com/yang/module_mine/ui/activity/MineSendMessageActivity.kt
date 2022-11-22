package com.yang.module_mine.ui.activity

import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding4.view.clicks
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.lib_common.base.ui.activity.BaseActivity
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.UserInfoHold.userId
import com.yang.lib_common.proxy.InjectViewModelProxy
import com.yang.lib_common.util.ViewLayoutChangeUtil
import com.yang.lib_common.util.showShort
import com.yang.lib_common.util.toJson
import com.yang.module_mine.adapter.SendMessageAdapter
import com.yang.module_mine.databinding.ActMineSendMessageBinding
import com.yang.module_mine.viewmodel.MineViewModel
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongCoreClient
import io.rong.imlib.RongIMClient
import io.rong.imlib.listener.OnReceiveMessageWrapperListener
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import io.rong.imlib.model.ReceivedProfile
import io.rong.message.TextMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

/**
 * @ClassName: MineSendMessageActivity
 * @Description:
 * @Author: yxy
 * @Date: 2022/11/22 10:44
 */
@Route(path = AppConstant.RoutePath.MINE_SEND_MESSAGE_ACTIVITY)
class MineSendMessageActivity : BaseActivity<ActMineSendMessageBinding>() {

    @InjectViewModel
    lateinit var mineViewModel: MineViewModel

    private lateinit var mSendMessageAdapter: SendMessageAdapter

    private var isFirst = true

//    var userId = "222222"
//    var rimToken = "vJ4LJVJ12Prl4UzX+k7XertTs5KC1FdiMUJM4xRPAEM=@hr57.cn.rongnav.com;hr57.cn.rongcfg.com"

    var userId = "111111"
    var rimToken = "fesDNxPWA+PXoQoBJaSVX7tTs5KC1Fdir792t8e0Kxw=@hr57.cn.rongnav.com;hr57.cn.rongcfg.com"

    private var oldestMessageId = -1

    private val maxMessageSize = 20

    override fun initViewBinding(): ActMineSendMessageBinding {
        return bind(ActMineSendMessageBinding::inflate)
    }

    override fun initData() {

//        userId = intent.getStringExtra(AppConstant.Constant.ID) ?: userId

        RongIMClient.connect(rimToken,
            object : RongIMClient.ConnectCallback() {
                override fun onSuccess(p0: String) {
//                    Log.e(TAG, "onSuccess: $p0")
                }

                override fun onError(p0: RongIMClient.ConnectionErrorCode?) {
                    Log.e(TAG, "onError: $p0")

                }

                override fun onDatabaseOpened(p0: RongIMClient.DatabaseOpenStatus?) {
//                    Log.e(TAG, "onDatabaseOpened: $p0")
                }

            })



        RongIMClient.setConnectionStatusListener {

            Log.e(TAG, "initData: ${it.toJson()}")
        }

        RongCoreClient.addOnReceiveMessageListener(object : OnReceiveMessageWrapperListener() {
            override fun onReceivedMessage(message: Message, p1: ReceivedProfile) {
//                Log.i(TAG, "initData:收到 ${message.toJson()}  ${p1.toJson()}")
                //showShort("收到${message.toJson()}    ${p1.toJson()} ")
                lifecycleScope.launch {
                    mSendMessageAdapter.addData(message)
                    mViewBinding.recyclerView.scrollToPosition(mSendMessageAdapter.data.size - 1)
                }

            }

        })


    }

    override fun initView() {
        ViewLayoutChangeUtil().add(findViewById(android.R.id.content)){
            if (it && mSendMessageAdapter.data.size > 0){
                mViewBinding.recyclerView.scrollToPosition(mSendMessageAdapter.data.size - 1)
            }
        }
        initRecyclerView()
        mViewBinding.apply {
            stvSendMessage.setOnClickListener {
                if (etMessage.text.isNullOrEmpty()){
                    showShort("不能发送空白消息")
                    return@setOnClickListener
                }
                val textMessage = TextMessage.obtain(etMessage.text.toString())
                val message =
                    Message.obtain(userId, Conversation.ConversationType.PRIVATE, textMessage)
                RongIMClient.getInstance()
                    .sendMessage(message, null, null, object : IRongCallback.ISendMessageCallback {
                        override fun onAttached(p0: Message?) {
                        }

                        override fun onSuccess(message: Message) {
                            mSendMessageAdapter.addData(message)
                            etMessage.setText("")
                            recyclerView.scrollToPosition(mSendMessageAdapter.data.size - 1)
                        }

                        override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {
                            Log.e(TAG, "onError:发送失败 $p0  $p1")
                        }
                    })
            }
        }
    }

    private fun initRecyclerView() {

        mSendMessageAdapter = SendMessageAdapter()
        mSendMessageAdapter.isUpFetchEnable = true
        mViewBinding.recyclerView.itemAnimator = null
        mViewBinding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(this@MineSendMessageActivity).resumeRequests()
                } else {
                    Glide.with(this@MineSendMessageActivity).pauseRequests()
                }
            }
        })
        mViewBinding.recyclerView.adapter = mSendMessageAdapter


        mSendMessageAdapter.setOnItemChildClickListener { adapter, view, position ->
            val item = mSendMessageAdapter.getItem(position)
            item?.let {

            }

        }



        mSendMessageAdapter.setOnItemClickListener { adapter, view, position ->
            val item = mSendMessageAdapter.getItem(position)
            item?.let {

            }
        }

        mSendMessageAdapter.setUpFetchListener {
            mSendMessageAdapter.isUpFetching = true
            getHistoryMessage { list ->
                if (list.isNullOrEmpty()) {
                    mSendMessageAdapter.isUpFetchEnable = false
                } else {
                    if (list.size < maxMessageSize) {
                        mSendMessageAdapter.isUpFetchEnable = false
                    }
                    oldestMessageId = list[0].messageId
                    mSendMessageAdapter.addData(0, list)
                    if (isFirst){
                        mViewBinding.recyclerView.scrollToPosition(mSendMessageAdapter.data.size - 1)
                        isFirst = false
                    }
                    mSendMessageAdapter.isUpFetching = false
                }
            }
        }


        lifecycleScope.launch {
            delay(300)
            getHistoryMessage {
                if (!it.isNullOrEmpty()) {
                    oldestMessageId = it[0].messageId
                    mSendMessageAdapter.replaceData(it)
                }
            }

        }

    }

    private fun getHistoryMessage(onSuccess: (list: MutableList<Message>?) -> Unit) {
        RongIMClient.getInstance().getHistoryMessages(
            Conversation.ConversationType.PRIVATE,
            userId,
            oldestMessageId,
            maxMessageSize,
            object : RongIMClient.ResultCallback<MutableList<Message>>() {
                override fun onSuccess(list: MutableList<Message>?) {
                    Log.e(TAG, "onError:加载数据失败   ${list?.toJson()}")
                    list?.reverse()
                    onSuccess(list)
                }

                override fun onError(p0: RongIMClient.ErrorCode) {
                    Log.e(TAG, "onError:加载数据失败   ${p0.toJson()}")
                }
            })

    }


    override fun initViewModel() {

        InjectViewModelProxy.inject(this)
    }
}