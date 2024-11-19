package com.taotao.cloud.payment.biz.daxpay.single.service.convert.record;

import com.taotao.cloud.payment.biz.daxpay.service.entity.record.sync.TradeSyncRecord;
import com.taotao.cloud.payment.biz.daxpay.service.result.record.sync.TradeSyncRecordResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 支付同步记录同步
 * @author xxm
 * @since 2023/7/14
 */
@Mapper
public interface TradeSyncRecordConvert {
    TradeSyncRecordConvert CONVERT = Mappers.getMapper(TradeSyncRecordConvert.class);

    TradeSyncRecordResult convert(TradeSyncRecord in);

}
