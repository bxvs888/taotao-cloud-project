package com.taotao.cloud.payment.biz.daxpay.channel.alipay.result.config;

import cn.bootx.platform.common.jackson.sensitive.SensitiveInfo;
import com.taotao.cloud.payment.biz.daxpay.channel.alipay.code.AliPayCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.*;
import lombok.experimental.*;

import java.math.BigDecimal;

/**
 * 支付宝配置
 * @author xxm
 * @since 2024/6/25
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付宝配置")
public class AlipayConfigResult {

    /** 主键 */
    @Schema(description = "主键")
    private Long id;

    /** 支付宝商户appId */
    @Schema(description = "支付宝商户appId")
    private String aliAppId;

    /** 是否启用 */
    @Schema(description = "是否启用")
    private Boolean enable;

    /** 支付限额 */
    @Schema(description = "支付限额")
    private BigDecimal limitAmount;

    /**
     * 认证类型 证书/公钥
     * @see AliPayCode#AUTH_TYPE_KEY
     */
    @Schema(description = "认证类型")
    private String authType;

    /** 签名类型 RSA2 */
    @Schema(description = "签名类型")
    public String signType;

    /**
     * 是商家与支付宝签约后，商家获得的支付宝商家唯一识别码，以 2088 开头的 16 位数字组成，在开放平台中账户中心获取
     */
    @Schema(description = "支付宝商家唯一识别码")
    private String alipayUserId;

    /** 支付宝公钥 */
    @Schema(description = "支付宝公钥")
    @SensitiveInfo
    public String alipayPublicKey;

    /** 应用私钥 */
    @Schema(description = "应用私钥")
    @SensitiveInfo
    private String privateKey;

    /** 应用公钥证书 */
    @Schema(description = "应用公钥证书")
    @SensitiveInfo
    private String appCert;

    /** 支付宝公钥证书 */
    @Schema(description = "支付宝公钥证书")
    @SensitiveInfo
    private String alipayCert;

    /** 支付宝CA根证书 */
    @Schema(description = "支付宝CA根证书")
    @SensitiveInfo
    private String alipayRootCert;

    /** 是否沙箱环境 */
    @Schema(description = "是否沙箱环境")
    private boolean sandbox;

    /** 商户AppId */
    @Schema(description = "商户AppId")
    private String appId;

}
