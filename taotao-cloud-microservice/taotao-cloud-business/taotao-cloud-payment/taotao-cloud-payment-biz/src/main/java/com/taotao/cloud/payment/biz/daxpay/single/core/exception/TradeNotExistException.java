package com.taotao.cloud.payment.biz.daxpay.single.core.exception;

import com.taotao.cloud.payment.biz.daxpay.core.code.DaxPayErrorCode;

/**
 * 交易不存在
 * @author xxm
 * @since 2024/6/17
 */
public class TradeNotExistException extends PayFailureException{

    public TradeNotExistException(String message) {
        super(DaxPayErrorCode.TRADE_NOT_EXIST,message);
    }

    public TradeNotExistException() {
        super(DaxPayErrorCode.TRADE_NOT_EXIST,"交易不存在");
    }
}
