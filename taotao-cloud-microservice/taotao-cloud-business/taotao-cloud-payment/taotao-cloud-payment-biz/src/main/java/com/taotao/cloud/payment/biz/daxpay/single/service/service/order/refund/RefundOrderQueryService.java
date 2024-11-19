package com.taotao.cloud.payment.biz.daxpay.single.service.service.order.refund;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import com.taotao.cloud.payment.biz.daxpay.core.exception.TradeNotExistException;
import com.taotao.cloud.payment.biz.daxpay.core.param.trade.refund.QueryRefundParam;
import com.taotao.cloud.payment.biz.daxpay.core.result.trade.refund.RefundOrderResult;
import com.taotao.cloud.payment.biz.daxpay.service.convert.order.refund.RefundOrderConvert;
import com.taotao.cloud.payment.biz.daxpay.service.dao.order.refund.RefundOrderManager;
import com.taotao.cloud.payment.biz.daxpay.service.entity.order.refund.RefundOrder;
import com.taotao.cloud.payment.biz.daxpay.service.param.order.refund.RefundOrderQuery;
import com.taotao.cloud.payment.biz.daxpay.service.result.order.refund.RefundOrderVo;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * 退款查询接口
 * @author xxm
 * @since 2024/4/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundOrderQueryService {
    private final RefundOrderManager refundOrderManager;

    /**
     * 分页查询
     */
    public PageResult<RefundOrderVo> page(PageParam pageParam, RefundOrderQuery query) {
        Page<RefundOrder> page = refundOrderManager.page(pageParam, query);
        return MpUtil.toPageResult(page);
    }

    /**
     * 根据id查询
     */
    public RefundOrderVo findById(Long id) {
        return refundOrderManager.findById(id).map(RefundOrder::toResult)
                .orElseThrow(() -> new DataNotExistException("退款订单不存在"));
    }

    /**
     * 根据退款号查询
     */
    public RefundOrderVo findByRefundNo(String refundNo){
        return refundOrderManager.findByRefundNo(refundNo).map(RefundOrder::toResult)
                .orElseThrow(() -> new DataNotExistException("退款订单扩展信息不存在"));

    }

    /**
     * 根据退款号和商户退款号查询
     */
    public Optional<RefundOrder> findByBizOrRefundNo(String refundNo, String bizRefundNo, String appId) {
        if (StrUtil.isNotBlank(refundNo)){
            return refundOrderManager.findByRefundNo(refundNo);
        } else if (StrUtil.isNotBlank(bizRefundNo)){
            return refundOrderManager.findByBizRefundNo(bizRefundNo,appId);
        } else {
            return Optional.empty();
        }
    }

    /**
     * 查询退款订单
     */
    public RefundOrderResult queryRefundOrder(QueryRefundParam param) {
        // 校验参数
        if (StrUtil.isBlank(param.getRefundNo()) && Objects.isNull(param.getBizRefundNo())){
            throw new ValidationFailedException("退款号或商户退款号不能都为空");
        }
        // 查询退款单
        RefundOrder refundOrder = this.findByBizOrRefundNo(param.getRefundNo(), param.getBizRefundNo(), param.getAppId())
                .orElseThrow(() -> new TradeNotExistException("退款订单不存在"));

        return RefundOrderConvert.CONVERT.toResult(refundOrder);
    }

    /**
     * 查询退款总金额
     */
    public BigDecimal getTotalAmount(RefundOrderQuery param) {
        return refundOrderManager.getTotalAmount(param);
    }
}
