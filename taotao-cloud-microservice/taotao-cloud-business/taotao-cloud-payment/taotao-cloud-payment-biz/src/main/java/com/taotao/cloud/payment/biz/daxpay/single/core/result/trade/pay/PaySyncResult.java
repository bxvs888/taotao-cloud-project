package com.taotao.cloud.payment.biz.daxpay.single.core.result.trade.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 支付同步结果
 * @author xxm
 * @since 2023/12/27
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付同步结果")
public class PaySyncResult  {

    /**
     * 退款订单同步后的状态状态
     * @see com.taotao.cloud.payment.biz.daxpay.core.enums.PayStatusEnum
     */
    @Schema(description = "同步状态")
    private String orderStatus;

    /**
     * 是否触发了调整
     */
    @Schema(description = "是否触发了调整")
    private boolean adjust;

}
