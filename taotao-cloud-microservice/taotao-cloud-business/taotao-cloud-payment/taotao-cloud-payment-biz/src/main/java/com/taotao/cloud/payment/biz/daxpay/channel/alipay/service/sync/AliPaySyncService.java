package com.taotao.cloud.payment.biz.daxpay.channel.alipay.service.sync;

import cn.bootx.platform.core.util.JsonUtil;
import com.taotao.cloud.payment.biz.daxpay.channel.alipay.code.AliPayCode;
import com.taotao.cloud.payment.biz.daxpay.channel.alipay.code.AliPayCode.PayStatus;
import com.taotao.cloud.payment.biz.daxpay.channel.alipay.service.config.AliPayConfigService;
import com.taotao.cloud.payment.biz.daxpay.core.enums.PayStatusEnum;
import com.taotao.cloud.payment.biz.daxpay.service.bo.sync.PaySyncResultBo;
import com.taotao.cloud.payment.biz.daxpay.service.entity.order.pay.PayOrder;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;


/**
 * 支付宝支付同步
 *
 * @author xxm
 * @since 2021/5/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPaySyncService {
    private final AliPayConfigService aliPayConfigService;

    /**
     * 与支付宝网关同步状态, 退款状态有
     * 1 远程支付成功
     * 2 交易创建，等待买家付款
     * 3 超时关闭
     * 4 查询不到
     * 5 查询失败
     */
    public PaySyncResultBo syncPayStatus(PayOrder payOrder){
        PaySyncResultBo syncResult = new PaySyncResultBo();
        // 查询
        try {
            AlipayTradeQueryModel model = new AlipayTradeQueryModel();
            model.setOutTradeNo(payOrder.getOrderNo());
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            request.setBizModel(model);
            AlipayTradeQueryResponse response = aliPayConfigService.execute(request);
            String tradeStatus = response.getTradeStatus();
            syncResult.setSyncData(JsonUtil.toJsonStr(response));
            // 设置网关订单号
            syncResult.setOutOrderNo(response.getTradeNo());
            // 支付完成  部分退款无法进行区分, 需要借助对账进行处理
            if (Objects.equals(tradeStatus, PayStatus.TRADE_SUCCESS) || Objects.equals(tradeStatus, PayStatus.TRADE_FINISHED)) {
                // 支付完成时间
                LocalDateTime payTime = LocalDateTimeUtil.of(response.getSendPayDate());
                return syncResult.setPayStatus(PayStatusEnum.SUCCESS).setFinishTime(payTime);
            }
            // 待支付
            if (Objects.equals(tradeStatus, PayStatus.WAIT_BUYER_PAY)) {
                return syncResult.setPayStatus(PayStatusEnum.PROGRESS);
            }
            // 已关闭或支付完成后全额退款
            if (Objects.equals(tradeStatus, PayStatus.TRADE_CLOSED)) {
                // 判断是否有支付时间, 有支付时间说明是退款
                if (Objects.nonNull(response.getSendPayDate())){
                    return syncResult.setPayStatus(PayStatusEnum.SUCCESS);
                } else {
                    return syncResult.setPayStatus(PayStatusEnum.CLOSE);
                }
            }
            // 支付宝支付后, 客户未进行操作将不会创建出订单, 所以交易不存在约等于未查询订单
            if (Objects.equals(response.getSubCode(), AliPayCode.ResponseCode.ACQ_TRADE_NOT_EXIST)) {
                return syncResult.setPayStatus(PayStatusEnum.PROGRESS);
            }
            // 查询失败
            if (!Objects.equals(AliPayCode.ResponseCode.SUCCESS, response.getCode())) {
                return syncResult.setSyncSuccess(false)
                        .setSyncErrorCode(response.getSubCode())
                        .setSyncErrorMsg(response.getSubMsg());
            }
        }
        catch (AlipayApiException e) {
            log.error("支付订单同步失败:", e);
            syncResult.setSyncErrorMsg(e.getErrMsg()).setSyncSuccess(false);
        }
        return syncResult;
    }

}
