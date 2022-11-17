package com.yang.module_mine.wxapi

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.yang.lib_common.app.BaseApplication
import com.yang.lib_common.bus.event.LiveDataBus
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.util.showShort


/**
 * @ClassName: WXEntryActivity
 * @Description:
 * @Author: yxy
 * @Date: 2022/11/17 14:59
 */
class WXEntryActivity: AppCompatActivity(), IWXAPIEventHandler {



    private val weChatApi = BaseApplication.weChatApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weChatApi.handleIntent(intent,this)
    }



    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        weChatApi.handleIntent(intent, this)
    }


    //微信发送的请求将回调到 onReq 方法
    override fun onReq(req: BaseReq) {
        when(req.type) {
            ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX -> {
//                goToGetMsg();
            }
            ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX -> {
//                goToShowMsg((ShowMessageFromWX.Req) req);
            }
        }
        finish()
    }

    //发送到微信请求的响应结果将回调到 onResp 方法
    override fun onResp(resp: BaseResp) {
        when(resp.errCode){
            BaseResp.ErrCode.ERR_OK ->{
//                p0.errCode
//                val code = (p0 as SendAuth.Resp).code
//                Log.e("Code++", code)
//                val wxEventBean = WXEventBean()
//                wxEventBean.setCode(code)

                when(resp.type){
                    ConstantsAPI.COMMAND_SUBSCRIBE_MESSAGE ->{
//                        SubscribeMessage.Resp subscribeMsgResp = (SubscribeMessage.Resp) resp;
//                        String text = String.format("openid=%s\ntemplate_id=%s\nscene=%d\naction=%s\nreserved=%s",
//                        subscribeMsgResp.openId, subscribeMsgResp.templateID, subscribeMsgResp.scene, subscribeMsgResp.action, subscribeMsgResp.reserved);
//
//                        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                    }
                    ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM ->{
//                        WXLaunchMiniProgram.Resp launchMiniProgramResp = (WXLaunchMiniProgram.Resp) resp;
//                        String text = String.format("openid=%s\nextMsg=%s\nerrStr=%s",
//                        launchMiniProgramResp.openId, launchMiniProgramResp.extMsg,launchMiniProgramResp.errStr);
//
//                        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                    }
                    ConstantsAPI.COMMAND_OPEN_BUSINESS_VIEW ->{
//                        WXOpenBusinessView.Resp launchMiniProgramResp = (WXOpenBusinessView.Resp) resp;
//                        String text = String.format("openid=%s\nextMsg=%s\nerrStr=%s\nbusinessType=%s",
//                        launchMiniProgramResp.openId, launchMiniProgramResp.extMsg,launchMiniProgramResp.errStr,launchMiniProgramResp.businessType);
//
//                        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                    }
                    ConstantsAPI.COMMAND_OPEN_BUSINESS_WEBVIEW ->{
//                        WXOpenBusinessWebview.Resp response = (WXOpenBusinessWebview.Resp) resp;
//                        String text = String.format("businessType=%d\nresultInfo=%s\nret=%d",response.businessType,response.resultInfo,response.errCode);
//
//                        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                    }
                    ConstantsAPI.COMMAND_SENDAUTH ->{
                        //登录获取token
                        val authResp = resp as SendAuth.Resp
                        val code = authResp.code
                        LiveDataBus.instance.with(AppConstant.WeChatConstant.CODE).postValue(code)
//                        NetworkUtil.sendWxAPI(
//                            handler, String.format(
//                                "https://api.weixin.qq.com/sns/oauth2/access_token?" +
//                                        "appid=%s&secret=%s&code=%s&grant_type=authorization_code",
//                                "wxd930ea5d5a258f4f",
//                                "1d6d1d57a3dd063b36d917bc0b44d964",
//                                code
//                            ), NetworkUtil.GET_TOKEN
//                        )
                    }
                }


            }
            BaseResp.ErrCode.ERR_AUTH_DENIED ->{
                showShort("用户拒绝授权")
            }
            BaseResp.ErrCode.ERR_USER_CANCEL ->{
                showShort("用户取消登录")
            }

        }
        finish()

    }


//    private void goToGetMsg() {
//        Intent intent = new Intent(this, GetFromWXActivity.class);
//        intent.putExtras(getIntent());
//        startActivity(intent);
//        finish();
//    }
//
//    private void goToShowMsg(ShowMessageFromWX.Req showReq) {
//        WXMediaMessage wxMsg = showReq.message;
//        WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;
//
//        StringBuffer msg = new StringBuffer();
//        msg.append("description: ");
//        msg.append(wxMsg.description);
//        msg.append("\n");
//        msg.append("extInfo: ");
//        msg.append(obj.extInfo);
//        msg.append("\n");
//        msg.append("filePath: ");
//        msg.append(obj.filePath);
//
//        Intent intent = new Intent(this, ShowFromWXActivity.class);
//        intent.putExtra(Constants.ShowMsgActivity.STitle, wxMsg.title);
//        intent.putExtra(Constants.ShowMsgActivity.SMessage, msg.toString());
//        intent.putExtra(Constants.ShowMsgActivity.BAThumbData, wxMsg.thumbData);
//        startActivity(intent);
//        finish();
//    }

}