package com.taotao.cloud.payment.biz.daxpay.single.core.exception;

import com.taotao.cloud.payment.biz.daxpay.core.code.DaxPayErrorCode;

/**
 * 支付通道未启用
 * @author xxm
 * @since 2024/6/17
 */
public class ChannelNotEnableException extends PayFailureException{

    public ChannelNotEnableException(String message) {
        super(DaxPayErrorCode.CHANNEL_NOT_ENABLE,message);
    }

    public ChannelNotEnableException() {
        super(DaxPayErrorCode.CHANNEL_NOT_ENABLE,"支付通道未启用");
    }
}
