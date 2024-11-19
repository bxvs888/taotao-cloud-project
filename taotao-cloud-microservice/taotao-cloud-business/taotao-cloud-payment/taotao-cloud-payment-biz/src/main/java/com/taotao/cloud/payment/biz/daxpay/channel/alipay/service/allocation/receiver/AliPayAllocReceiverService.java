package com.taotao.cloud.payment.biz.daxpay.channel.alipay.service.allocation.receiver;

import cn.hutool.core.util.StrUtil;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.AlipayTradeRoyaltyRelationBindModel;
import com.alipay.api.domain.AlipayTradeRoyaltyRelationUnbindModel;
import com.alipay.api.domain.RoyaltyEntity;
import com.alipay.api.request.AlipayTradeRoyaltyRelationBindRequest;
import com.alipay.api.request.AlipayTradeRoyaltyRelationUnbindRequest;
import com.alipay.api.response.AlipayTradeRoyaltyRelationBindResponse;
import com.alipay.api.response.AlipayTradeRoyaltyRelationUnbindResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import com.taotao.cloud.payment.biz.daxpay.channel.alipay.code.AliPayCode;
import com.taotao.cloud.payment.biz.daxpay.channel.alipay.service.config.AliPayConfigService;
import com.taotao.cloud.payment.biz.daxpay.core.enums.AllocReceiverTypeEnum;
import com.taotao.cloud.payment.biz.daxpay.core.exception.ConfigErrorException;
import com.taotao.cloud.payment.biz.daxpay.core.exception.TradeStatusErrorException;
import com.taotao.cloud.payment.biz.daxpay.service.entity.allocation.receiver.AllocReceiver;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.taotao.cloud.payment.biz.daxpay.core.enums.AllocReceiverTypeEnum.*;


/**
 * 支付宝分账
 * @author xxm
 * @since 2024/3/28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayAllocReceiverService {
    private final AliPayConfigService aliPayConfigService;

    /**
     * 校验
     */
    public boolean validation(AllocReceiver allocReceiver){
        List<String> list = Arrays.asList(USER_ID.getCode(), OPEN_ID.getCode(), LOGIN_NAME.getCode());
        String receiverType = allocReceiver.getReceiverType();
        return list.contains(receiverType);
    }

    /**
     * 绑定关系
     */
    @SneakyThrows
    public void bind(AllocReceiver allocReceiver){
        AlipayClient alipayClient = aliPayConfigService.getAlipayClient();
        AlipayTradeRoyaltyRelationBindModel model = new AlipayTradeRoyaltyRelationBindModel();

        RoyaltyEntity entity = new RoyaltyEntity();
        AllocReceiverTypeEnum receiverTypeEnum = AllocReceiverTypeEnum.findByCode(allocReceiver.getReceiverType());
        entity.setType(this.getReceiverType(receiverTypeEnum));
        entity.setAccount(allocReceiver.getReceiverAccount());
        entity.setName(allocReceiver.getReceiverName());
        entity.setMemo(allocReceiver.getRelationName());

        // 不报错视为同步成功
        model.setReceiverList(Collections.singletonList(entity));
        AlipayTradeRoyaltyRelationBindRequest request = new AlipayTradeRoyaltyRelationBindRequest();
        model.setOutRequestNo(String.valueOf(allocReceiver.getId()));
        request.setBizModel(model);
        AlipayTradeRoyaltyRelationBindResponse response = alipayClient.execute(request);
        this.verifyErrorMsg(response);
    }

    /**
     * 解绑关系
     */
    @SneakyThrows
    public void unbind(AllocReceiver allocReceiver){
        AlipayClient alipayClient = aliPayConfigService.getAlipayClient();
        AlipayTradeRoyaltyRelationUnbindModel model = new AlipayTradeRoyaltyRelationUnbindModel();
        model.setOutRequestNo(String.valueOf(allocReceiver.getId()));

        RoyaltyEntity entity = new RoyaltyEntity();
        AllocReceiverTypeEnum receiverTypeEnum = findByCode(allocReceiver.getReceiverType());
        entity.setType(this.getReceiverType(receiverTypeEnum));
        entity.setAccount(allocReceiver.getReceiverAccount());

        model.setReceiverList(Collections.singletonList(entity));
        AlipayTradeRoyaltyRelationUnbindRequest request = new AlipayTradeRoyaltyRelationUnbindRequest();
        request.setBizModel(model);
        AlipayTradeRoyaltyRelationUnbindResponse response =  alipayClient.execute(request);
        System.out.println(response);
        // 如果出现分账方不存在也视为成功
        if (Objects.equals(response.getSubCode(), AliPayCode.USER_NOT_EXIST)) {
            return;
        }
        this.verifyErrorMsg(response);
    }

    /**
     * 验证错误信息
     */
    private void verifyErrorMsg(AlipayResponse alipayResponse) {
        if (!Objects.equals(alipayResponse.getCode(), AliPayCode.ResponseCode.SUCCESS)) {
            String errorMsg = alipayResponse.getSubMsg();
            if (StrUtil.isBlank(errorMsg)) {
                errorMsg = alipayResponse.getMsg();
            }
            log.error("分账接收方处理失败 {}", errorMsg);
            throw new TradeStatusErrorException(errorMsg);
        }
    }

    /**
     * 获取分账接收方类型编码
     */
    private String getReceiverType(AllocReceiverTypeEnum receiverTypeEnum){
        if (receiverTypeEnum == USER_ID){
            return "userId";
        }
        if (receiverTypeEnum == OPEN_ID){
            return "openId";
        }
        if (receiverTypeEnum == LOGIN_NAME){
            return "loginName";
        }
        throw new ConfigErrorException("分账接收方类型错误");
    }
}
