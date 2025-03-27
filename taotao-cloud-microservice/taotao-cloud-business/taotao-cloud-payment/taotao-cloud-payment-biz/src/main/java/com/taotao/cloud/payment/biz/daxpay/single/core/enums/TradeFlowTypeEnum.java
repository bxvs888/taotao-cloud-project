package com.taotao.cloud.payment.biz.daxpay.single.core.enums;

import lombok.*;
import lombok.Getter;

/**
 * 交易流水记录类型
 * 字典: trade_flow_type
 * @author xxm
 * @since 2024/2/20
 */
@Getter
@AllArgsConstructor
public enum TradeFlowTypeEnum {

    /** 支付 */
    PAY("pay", "支付"),
    /** 退款 */
    REFUND("refund", "退款"),
    /** 转账 */
    TRANSFER("transfer", "转账"),
    ;

    private final String code;
    private final String name;
}
