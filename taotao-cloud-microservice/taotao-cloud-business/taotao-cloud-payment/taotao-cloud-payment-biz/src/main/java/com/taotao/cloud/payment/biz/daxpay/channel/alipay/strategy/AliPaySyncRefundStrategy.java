package com.taotao.cloud.payment.biz.daxpay.channel.alipay.strategy;

import lombok.RequiredArgsConstructor;
import com.taotao.cloud.payment.biz.daxpay.channel.alipay.service.sync.AliPayRefundSyncService;
import com.taotao.cloud.payment.biz.daxpay.core.enums.ChannelEnum;
import com.taotao.cloud.payment.biz.daxpay.core.exception.OperationFailException;
import com.taotao.cloud.payment.biz.daxpay.service.bo.sync.RefundSyncResultBo;
import com.taotao.cloud.payment.biz.daxpay.service.entity.order.refund.RefundOrder;
import com.taotao.cloud.payment.biz.daxpay.service.strategy.AbsSyncRefundOrderStrategy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝退款订单查询
 * @author xxm
 * @since 2024/7/25
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AliPaySyncRefundStrategy extends AbsSyncRefundOrderStrategy {

    private final AliPayRefundSyncService alipaySyncService;

    /**
     * 策略标识, 可以自行进行扩展
     *
     * @see ChannelEnum
     */
    @Override
    public String getChannel() {
        return ChannelEnum.ALI.getCode();
    }


    /**
     * 间隔10秒以上再发起退款查询请求。小于10s可能会导致查询到的状态不正确, 因为支付宝没有转账失败的状态
     */
    @Override
    public void doBeforeHandler() {
        RefundOrder refundOrder = this.getRefundOrder();
        if (refundOrder.getReqTime().plusSeconds(10).isAfter(LocalDateTime.now())) {
            throw new OperationFailException("间隔10秒以上再发起退款查询请求");
        }
    }

    /**
     * 异步支付单与支付网关进行状态比对后的结果
     *
     */
    @Override
    public RefundSyncResultBo doSync() {
        return alipaySyncService.syncRefundStatus(getRefundOrder());
    }

}
