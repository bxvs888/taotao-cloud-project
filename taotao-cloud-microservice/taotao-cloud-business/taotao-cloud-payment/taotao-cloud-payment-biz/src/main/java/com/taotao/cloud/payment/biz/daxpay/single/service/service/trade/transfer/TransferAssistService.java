package com.taotao.cloud.payment.biz.daxpay.single.service.service.trade.transfer;

import com.taotao.cloud.payment.biz.daxpay.core.enums.TransferStatusEnum;
import com.taotao.cloud.payment.biz.daxpay.core.param.trade.transfer.TransferParam;
import com.taotao.cloud.payment.biz.daxpay.core.result.trade.transfer.TransferResult;
import com.taotao.cloud.payment.biz.daxpay.core.util.TradeNoGenerateUtil;
import com.taotao.cloud.payment.biz.daxpay.service.dao.order.transfer.TransferOrderManager;
import com.taotao.cloud.payment.biz.daxpay.service.entity.order.transfer.TransferOrder;
import com.taotao.cloud.payment.biz.daxpay.service.service.notice.MerchantNoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 转账辅助服务
 * @author xxm
 * @since 2024/6/6
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransferAssistService {

    private final TransferOrderManager transferOrderManager;
    private final MerchantNoticeService merchantNoticeService;

    /**
     * 创建转账订单
     */
    @Transactional(rollbackFor = Exception.class)
    public TransferOrder createOrder(TransferParam param) {
        // 创建转账订单
        TransferOrder transferOrder = new TransferOrder()
                .setTransferNo(TradeNoGenerateUtil.transfer())
                .setAmount(param.getAmount())
                .setBizTransferNo(param.getBizTransferNo())
                .setChannel(param.getChannel())
                .setPayeeName(param.getPayeeName())
                .setPayeeAccount(param.getPayeeAccount())
                .setPayeeType(param.getPayeeType())
                .setReason(param.getReason())
                .setStatus(TransferStatusEnum.PROGRESS.getCode())
                .setTitle(param.getTitle())
                .setNotifyUrl(param.getNotifyUrl())
                .setAttach(param.getAttach())
                .setClientIp(param.getClientIp());
        transferOrderManager.save(transferOrder);
        return transferOrder;
    }

    /**
     *  转账关闭
     */
    public void close(TransferOrder order, LocalDateTime finishTime) {
        // 执行策略的关闭方法
        order.setStatus(TransferStatusEnum.CLOSE.getCode())
                .setFinishTime(Optional.ofNullable(finishTime).orElse(LocalDateTime.now()));
        transferOrderManager.updateById(order);
        merchantNoticeService.registerTransferNotice(order);
    }

    /**
     * 构造
     */
    public TransferResult buildResult(TransferOrder order) {
        return new TransferResult()
                .setTransferNo(order.getTransferNo())
                .setBizTransferNo(order.getBizTransferNo())
                .setStatus(order.getStatus())
                .setErrorMsg(order.getErrorMsg());
    }
}
