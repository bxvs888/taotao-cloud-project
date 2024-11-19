package com.taotao.cloud.payment.biz.daxpay.channel.wechat.strategy;

import cn.bootx.platform.core.exception.ValidationFailedException;
import com.taotao.cloud.payment.biz.daxpay.channel.wechat.code.WechatPayCode;
import com.taotao.cloud.payment.biz.daxpay.channel.wechat.entity.config.WechatPayConfig;
import com.taotao.cloud.payment.biz.daxpay.channel.wechat.service.close.WechatPayCloseV2Service;
import com.taotao.cloud.payment.biz.daxpay.channel.wechat.service.close.WechatPayCloseV3Service;
import com.taotao.cloud.payment.biz.daxpay.channel.wechat.service.config.WechatPayConfigService;
import com.taotao.cloud.payment.biz.daxpay.core.enums.ChannelEnum;
import com.taotao.cloud.payment.biz.daxpay.core.enums.CloseTypeEnum;
import com.taotao.cloud.payment.biz.daxpay.core.enums.PayMethodEnum;
import com.taotao.cloud.payment.biz.daxpay.service.entity.order.pay.PayOrder;
import com.taotao.cloud.payment.biz.daxpay.service.strategy.AbsPayCloseStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信支付关闭策略
 * @author xxm
 * @since 2023/12/30
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class WechatPayCloseStrategy extends AbsPayCloseStrategy {

    private final WechatPayConfigService wechatPayConfigService;
    private final WechatPayCloseV2Service payCloseV2Service;
    private final WechatPayCloseV3Service payCloseV3Service;

    private WechatPayConfig config;

    @Override
    public String getChannel() {
        return ChannelEnum.WECHAT.getCode();
    }

    /**
     * 关闭前的处理方式
     */
    @Override
    public void doBeforeCloseHandler() {
        PayOrder order = this.getOrder();
        // 只有付款码可以撤销支付
        if (this.isUseCancel() && !order.getMethod().equals(PayMethodEnum.BARCODE.getCode())){
            throw new ValidationFailedException("该订单不支持撤销操作");
        }
        this.config = wechatPayConfigService.getAndCheckConfig();
    }

    /**
     * 关闭操作
     */
    @Override
    public CloseTypeEnum doCloseHandler() {
        PayOrder order = this.getOrder();
        // 只有付款码可以撤销支付
        if (this.isUseCancel() && order.getMethod().equals(PayMethodEnum.BARCODE.getCode())){
            if (Objects.equals(config.getApiVersion(), WechatPayCode.API_V2)){
                payCloseV2Service.cancel(order,config);
            } else {
                payCloseV3Service.cancel(order,config);
            }
            return CloseTypeEnum.CANCEL;
        }
        // 判断接口是v2还是v3
        if (Objects.equals(config.getApiVersion(), WechatPayCode.API_V2)){
            payCloseV2Service.close(order,config);
        } else {
            payCloseV3Service.close(order,config);
        }
        return CloseTypeEnum.CLOSE;
    }
}
