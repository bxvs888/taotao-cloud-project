package com.taotao.cloud.payment.biz.daxpay.single.service.param.merchant;

import cn.bootx.platform.core.validation.ValidationGroup;
import com.taotao.cloud.payment.biz.daxpay.core.enums.SignTypeEnum;
import com.taotao.cloud.payment.biz.daxpay.core.enums.MerchantNotifyTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 商户应用
 * @author xxm
 * @since 2024/6/24
 */
@Data
@Accessors(chain = true)
@Schema(title = "商户应用")
public class MchAppParam {

    /** 主键 */
    @Schema(description = "主键")
    @NotNull(message = "主键ID不可为空", groups = ValidationGroup.edit.class)
    private Long id;

    /** 应用名称 */
    @Schema(description = "应用名称")
    @NotNull(message = "应用名称不可为空", groups = ValidationGroup.add.class)
    private String appName;

    /**
     * 签名方式
     * @see SignTypeEnum
     */
    @Schema(description = "签名方式")
    @NotNull(message = "签名方式不可为空", groups = ValidationGroup.add.class)
    private String signType;


    /** 签名秘钥 */
    @Schema(description = "签名秘钥")
    @NotNull(message = "签名秘钥不可为空", groups = ValidationGroup.add.class)
    private String signSecret;

    /** 是否对请求进行验签 */
    @Schema(description = "是否对请求进行验签")
    private boolean reqSign;

    /** 支付限额 */
    @Schema(description = "支付限额")
    @NotNull(message = "支付限额不可为空", groups = ValidationGroup.add.class)
    private BigDecimal limitAmount;

    /** 订单默认超时时间(分钟) */
    @Schema(description = "订单默认超时时间(分钟)")
    @NotNull(message = "订单默认超时时间(分钟)不可为空", groups = ValidationGroup.add.class)
    @Min(value = 5, message = "订单默认超时时间(分钟)不可小于5分钟")
    private Integer orderTimeout;

    /**
     * 异步消息通知类型, 当前只支持http方式
     * @see MerchantNotifyTypeEnum
     */
    @Schema(description = "异步消息通知类型")
    private String notifyType;

    /**
     * 通知地址, http/WebSocket 需要配置
     */
    @Schema(description = "通知地址")
    private String notifyUrl;
}
