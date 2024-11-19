package com.taotao.cloud.payment.biz.daxpay.single.core.exception;

import com.taotao.cloud.payment.biz.daxpay.core.code.DaxPayErrorCode;

/**
 * 不支持该能力
 * @author xxm
 * @since 2024/6/17
 */
public class UnsupportedAbilityException extends PayFailureException{

    public UnsupportedAbilityException(String message) {
        super(DaxPayErrorCode.UNSUPPORTED_ABILITY,message);
    }

    public UnsupportedAbilityException() {
        super(DaxPayErrorCode.UNSUPPORTED_ABILITY,"不支持该能力");
    }
}
