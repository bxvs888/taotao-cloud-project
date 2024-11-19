package com.taotao.cloud.payment.biz.daxpay.channel.wechat.service.callback;

import com.taotao.cloud.payment.biz.daxpay.channel.wechat.code.WechatPayCode;
import com.taotao.cloud.payment.biz.daxpay.channel.wechat.entity.config.WechatPayConfig;
import com.taotao.cloud.payment.biz.daxpay.channel.wechat.service.config.WechatPayConfigService;
import com.taotao.cloud.payment.biz.daxpay.channel.wechat.util.WechatPayUtil;
import com.taotao.cloud.payment.biz.daxpay.core.enums.CallbackStatusEnum;
import com.taotao.cloud.payment.biz.daxpay.core.enums.ChannelEnum;
import com.taotao.cloud.payment.biz.daxpay.core.enums.RefundStatusEnum;
import com.taotao.cloud.payment.biz.daxpay.core.enums.TradeTypeEnum;
import com.taotao.cloud.payment.biz.daxpay.core.util.PayUtil;
import com.taotao.cloud.payment.biz.daxpay.service.common.context.CallbackLocal;
import com.taotao.cloud.payment.biz.daxpay.service.common.local.PaymentContextLocal;
import com.taotao.cloud.payment.biz.daxpay.service.service.record.callback.TradeCallbackRecordService;
import com.taotao.cloud.payment.biz.daxpay.service.service.trade.refund.RefundCallbackService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyV3Result;
import com.github.binarywang.wxpay.constant.WxPayConstants.RefundStatus;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * 微信退款回调
 * @author xxm
 * @since 2024/7/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatRefundCallbackService {
    private final WechatPayConfigService wechatPayConfigService;
    private final RefundCallbackService refundCallbackService;
    private final TradeCallbackRecordService tradeCallbackRecordService;

    /**
     * 退款回调处理
     */
    public String refundHandle(HttpServletRequest request){
        // 解析数据
        if (this.resolve(request)){
            // 执行回调业务处理
            refundCallbackService.refundCallback();
            // 保存记录
            tradeCallbackRecordService.saveCallbackRecord();
            return WxPayNotifyResponse.success("OK");
        } else {
            // 保存记录
            tradeCallbackRecordService.saveCallbackRecord();
            return WxPayNotifyResponse.fail("FAIL");
        }
    }

    /**
     * 退款回调处理, 解析数据
     */
    public boolean resolve(HttpServletRequest request){
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();

        // 设置类型和通道
        callbackInfo.setCallbackType(TradeTypeEnum.REFUND).setChannel(ChannelEnum.WECHAT.getCode());

        WechatPayConfig config = wechatPayConfigService.getAndCheckConfig();
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(config);
        // v2 或 v3
        if (Objects.equals(config.getApiVersion(), WechatPayCode.API_V2)) {
            // V2 回调接收处理
            String xml = WechatPayUtil.readV2Data(request);
            callbackInfo.setRawData(xml);
            try {
                // 转换请求
                var result = wxPayService.parseRefundNotifyResult(xml);
                // 解析数据
                resolveV2Data(result);
                return true;
            } catch (WxPayException e) {
                callbackInfo.setCallbackStatus(CallbackStatusEnum.FAIL);
                log.error("微信退款V2回调处理失败", e);
                return false;
            }
        } else {
            // V3 回调接收处理
            String body = JakartaServletUtil.getBody(request);
            Map<String, String> headerMap = JakartaServletUtil.getHeaderMap(request);
            SignatureHeader signatureHeader = new SignatureHeader();
            signatureHeader.setNonce(headerMap.get(WechatPayCode.WECHAT_PAY_NONCE.toLowerCase()));
            signatureHeader.setTimeStamp(headerMap.get(WechatPayCode.WECHAT_PAY_TIMESTAMP.toLowerCase()));
            signatureHeader.setSerial(headerMap.get(WechatPayCode.WECHAT_PAY_SERIAL.toLowerCase()));
            signatureHeader.setSignature(headerMap.get(WechatPayCode.WECHAT_PAY_SIGNATURE.toLowerCase()));
            callbackInfo.setRawData(body);
            try {
                // 转换请求
                var result = wxPayService.parseRefundNotifyV3Result(body, signatureHeader);
                // 解析数据
                this.resolveV3Data(result);
                return true;
            } catch (WxPayException e) {
                callbackInfo.setCallbackStatus(CallbackStatusEnum.FAIL);
                log.error("微信退款V3回调处理失败", e);
                return false;
            }
        }
    }

    /**
     * 解析数据 v2
     */
    public void resolveV2Data(WxPayRefundNotifyResult notifyResult){
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        // 解密的数据
        var result = notifyResult.getReqInfo();

        // 回调数据
        callbackInfo.setCallbackData(BeanUtil.beanToMap(result));
        // 网关退款号
        callbackInfo.setOutTradeNo(result.getTransactionId());
        // 退款号
        callbackInfo.setTradeNo(result.getRefundId());
        // 退款状态 - 成功
        if (Objects.equals(RefundStatus.SUCCESS, result.getRefundStatus())){
            callbackInfo.setTradeStatus(RefundStatusEnum.SUCCESS.getCode());
        }
        // 退款状态 - 退款关闭
        if (Objects.equals(result.getRefundStatus(), RefundStatus.REFUND_CLOSE)){
            callbackInfo.setTradeStatus(RefundStatusEnum.CLOSE.getCode());
        }
        // 退款状态 - 失败
        if (Objects.equals(RefundStatus.CHANGE, result.getRefundStatus())){
            callbackInfo.setTradeStatus(RefundStatusEnum.FAIL.getCode());
        }
        // 退款金额和时间
        callbackInfo.setAmount(PayUtil.conversionAmount(result.getRefundFee()));
        String timeEnd = result.getSuccessTime();
        if (StrUtil.isNotBlank(timeEnd)){
            callbackInfo.setFinishTime(WechatPayUtil.parseV2(timeEnd));
        }

    }
    /**
     * 解析数据 v3
     */
    public void resolveV3Data(WxPayRefundNotifyV3Result v3Result){
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        var result = v3Result.getResult();
        // 回调数据
        callbackInfo.setCallbackData(BeanUtil.beanToMap(result));
        // 网关退款号
        callbackInfo.setOutTradeNo(result.getRefundId());
        // 退款号
        callbackInfo.setTradeNo(result.getOutRefundNo());
        // 退款状态 - 成功
        if (Objects.equals(RefundStatus.SUCCESS, result.getRefundStatus())){
            callbackInfo.setTradeStatus(RefundStatusEnum.SUCCESS.getCode());
        }
        // 退款状态 - 退款关闭
        if (Objects.equals(result.getRefundStatus(), RefundStatus.CLOSED)){
            callbackInfo.setTradeStatus(RefundStatusEnum.CLOSE.getCode());
        }
        // 退款状态 - 失败
        if (Objects.equals(RefundStatus.ABNORMAL, result.getRefundStatus())){
            callbackInfo.setTradeStatus(RefundStatusEnum.FAIL.getCode());
        }
        // 退款金额
        callbackInfo.setAmount(PayUtil.conversionAmount(result.getAmount().getTotal()));
        callbackInfo.setFinishTime(WechatPayUtil.parseV3(result.getSuccessTime()));
    }
}
