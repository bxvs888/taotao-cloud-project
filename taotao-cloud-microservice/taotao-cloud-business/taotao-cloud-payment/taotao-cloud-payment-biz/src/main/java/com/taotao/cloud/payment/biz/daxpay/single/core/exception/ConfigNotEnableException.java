package com.taotao.cloud.payment.biz.daxpay.single.core.exception;

import com.taotao.cloud.payment.biz.daxpay.core.code.DaxPayErrorCode;

/**
 * 配置未启用
 * @author xxm
 * @since 2024/6/17
 */
public class ConfigNotEnableException extends PayFailureException{

    public ConfigNotEnableException(String message) {
        super(DaxPayErrorCode.CONFIG_NOT_ENABLE,message);
    }

    public ConfigNotEnableException() {
        super(DaxPayErrorCode.CONFIG_NOT_ENABLE,"配置未启用");
    }
}
