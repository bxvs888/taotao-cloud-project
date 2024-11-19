package com.taotao.cloud.payment.biz.daxpay.channel.alipay.entity.config;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.core.util.JsonUtil;
import com.taotao.cloud.payment.biz.daxpay.channel.alipay.code.AliPayCode;
import com.taotao.cloud.payment.biz.daxpay.channel.alipay.convert.config.AlipayConfigConvert;
import com.taotao.cloud.payment.biz.daxpay.channel.alipay.result.config.AlipayConfigResult;
import com.taotao.cloud.payment.biz.daxpay.core.enums.ChannelEnum;
import com.taotao.cloud.payment.biz.daxpay.service.entity.config.ChannelConfig;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 支付宝配置
 * @author xxm
 * @since 2024/6/25
 */
@Data
@Accessors(chain = true)
public class AliPayConfig implements ToResult<AlipayConfigResult> {

    /** 主键 */
    private Long id;

    /** 支付宝商户appId */
    private String aliAppId;

    /** 是否启用 */
    private Boolean enable;

    /** 支付限额 */
    private BigDecimal limitAmount;

    /**
     * 认证类型 证书/公钥
     * @see AliPayCode.AuthType
     */
    private String authType;

    /** 签名类型 RSA2 */
    public String signType;

    /**
     * 是商家与支付宝签约后，商家获得的支付宝商家唯一识别码，以 2088 开头的 16 位数字组成，在开放平台中账户中心获取
     */
    private String alipayUserId;

    /** 支付宝公钥 */
    public String alipayPublicKey;

    /** 应用私钥 */
    private String privateKey;

    /** 应用公钥证书 */
    private String appCert;

    /** 支付宝公钥证书 */
    private String alipayCert;

    /** 支付宝CA根证书 */
    private String alipayRootCert;

    /** 是否沙箱环境 */
    private boolean sandbox;

    /** 商户AppId */
    private String appId;

    /**
     * 转换为通道配置
     */
    public ChannelConfig toChannelConfig() {
        ChannelConfig channelConfig = new ChannelConfig();
        channelConfig.setId(this.getId());
        channelConfig.setOutAppId(this.getAliAppId());
        channelConfig.setAppId(this.getAppId());
        channelConfig.setEnable(this.getEnable());
        channelConfig.setChannel(ChannelEnum.ALI.getCode());
        AliPayConfig copy = AlipayConfigConvert.CONVERT.copy(this);
        // 清空不需要序列化的字段
        copy.setId(null).setAppId(null).setEnable(null).setAliAppId(null).setAppId(null);
        String jsonStr = JsonUtil.toJsonStr(copy);
        channelConfig.setExt(jsonStr);
        return channelConfig;
    }

    /**
     * 从通道配置转换为支付宝配置
     */
    public static AliPayConfig convertConfig(ChannelConfig channelConfig) {
        AliPayConfig config = JsonUtil.toBean(channelConfig.getExt(), AliPayConfig.class);

        config.setId(channelConfig.getId())
                .setAliAppId(channelConfig.getOutAppId())
                .setEnable(channelConfig.isEnable());
        return config;
    }

    @Override
    public AlipayConfigResult toResult() {
        return AlipayConfigConvert.CONVERT.toResult(this);
    }
}
