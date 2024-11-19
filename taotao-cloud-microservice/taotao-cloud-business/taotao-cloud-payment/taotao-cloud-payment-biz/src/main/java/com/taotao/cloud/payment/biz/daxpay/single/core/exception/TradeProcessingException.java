package com.taotao.cloud.payment.biz.daxpay.single.core.exception;

import com.taotao.cloud.payment.biz.daxpay.core.code.DaxPayErrorCode;

/**
 * 交易处理中, 请勿重复操作
 * @author xxm
 * @since 2024/6/17
 */
public class TradeProcessingException extends PayFailureException{

    public TradeProcessingException(String message) {
        super(DaxPayErrorCode.TRADE_PROCESSING,message);
    }

    public TradeProcessingException() {
        super(DaxPayErrorCode.TRADE_PROCESSING,"交易处理中，请勿重复操作");
    }
}
