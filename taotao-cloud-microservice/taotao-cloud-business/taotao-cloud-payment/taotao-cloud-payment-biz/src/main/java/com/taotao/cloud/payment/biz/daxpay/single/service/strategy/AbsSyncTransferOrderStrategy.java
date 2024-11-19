package com.taotao.cloud.payment.biz.daxpay.single.service.strategy;

import com.taotao.cloud.payment.biz.daxpay.service.bo.sync.TransferSyncResultBo;
import com.taotao.cloud.payment.biz.daxpay.service.entity.order.transfer.TransferOrder;
import lombok.Getter;
import lombok.Setter;

/**
 * 转账同步策略
 * @author xxm
 * @since 2024/7/25
 */
@Getter
@Setter
public abstract class AbsSyncTransferOrderStrategy implements PaymentStrategy{

    private TransferOrder transferOrder;

    /**
     * 同步前处理, 主要是预防请求过于迅速, 支付网关没有处理完退款请求, 导致返回的状态不正确
     */
    public void doBeforeHandler(){}
    /**
     * 异步支付单与支付网关进行状态比对后的结果
     */
    public abstract TransferSyncResultBo doSync();
}
