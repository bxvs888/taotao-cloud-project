package com.taotao.cloud.payment.biz.daxpay.single.core.exception;

import com.taotao.cloud.payment.biz.daxpay.core.code.DaxPayErrorCode;

/**
 * 金额超过限额
 * @author xxm
 * @since 2024/6/17
 */
public class AmountExceedLimitException extends PayFailureException{

    public AmountExceedLimitException(String message) {
        super(DaxPayErrorCode.AMOUNT_EXCEED_LIMIT,message);
    }

    public AmountExceedLimitException() {
        super(DaxPayErrorCode.AMOUNT_EXCEED_LIMIT,"金额超过限额");
    }
}
