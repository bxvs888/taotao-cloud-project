package com.taotao.cloud.payment.biz.daxpay.channel.wechat.service.sync.refund;

import com.taotao.cloud.payment.biz.daxpay.channel.wechat.entity.config.WechatPayConfig;
import com.taotao.cloud.payment.biz.daxpay.channel.wechat.service.config.WechatPayConfigService;
import com.taotao.cloud.payment.biz.daxpay.channel.wechat.util.WechatPayUtil;
import com.taotao.cloud.payment.biz.daxpay.core.enums.RefundStatusEnum;
import com.taotao.cloud.payment.biz.daxpay.core.util.PayUtil;
import com.taotao.cloud.payment.biz.daxpay.service.bo.sync.RefundSyncResultBo;
import com.taotao.cloud.payment.biz.daxpay.service.entity.order.refund.RefundOrder;
import cn.hutool.core.collection.CollUtil;
import com.github.binarywang.wxpay.bean.request.WxPayRefundQueryRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants.RefundStatus;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 微信退款订单信息同步
 * @author xxm
 * @since 2024/7/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatRefundSyncV2Service {
    private final WechatPayConfigService wechatPayConfigService;

    /**
     * 退款信息查询
     */
    public RefundSyncResultBo sync(RefundOrder refundOrder, WechatPayConfig weChatPayConfig){
        RefundSyncResultBo syncResult = new RefundSyncResultBo();
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(weChatPayConfig);
        // 使用退款单号查询, 只返回当前这条, 如果使用支付订单号查询,返回所有相关的
        var request = new WxPayRefundQueryRequest();
        request.setOutRefundNo(refundOrder.getRefundNo());
        try {
            var result = wxPayService.refundQuery(request);
            // 网关退款号
            syncResult.setOutRefundNo(result.getTransactionId())

                    .setAmount(PayUtil.conversionAmount(result.getRefundFee()));
            // 交易不存在
            if (CollUtil.isEmpty(result.getRefundRecords())){
                syncResult.setRefundStatus(RefundStatusEnum.FAIL).setTradeErrorMsg("交易不存在");
            }
            var record = result.getRefundRecords().getFirst();
            // 退款状态 - 成功
            if (Objects.equals(RefundStatus.SUCCESS, record.getRefundStatus())){
                syncResult.setRefundStatus(RefundStatusEnum.SUCCESS)
                        .setFinishTime(WechatPayUtil.parseV2(record.getRefundSuccessTime()));
            }
            // 退款状态 - 退款关闭
            if (Objects.equals(RefundStatus.REFUND_CLOSE, record.getRefundStatus())){
                syncResult.setRefundStatus(RefundStatusEnum.CLOSE);
            }
            // 退款状态 - 失败
            if (Objects.equals(RefundStatus.CHANGE, record.getRefundStatus())){
                syncResult.setRefundStatus(RefundStatusEnum.FAIL);
            }
        } catch (WxPayException e) {
            log.error("微信退款订单查询V3失败", e);
            syncResult.setSyncErrorMsg(e.getCustomErrorMsg()).setSyncSuccess(false);
        }
        return syncResult;
    }
}
