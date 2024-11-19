package com.taotao.cloud.message.biz.austin.cron.xxl.enums;

/**
 * 调度类型
 *
 * @author shuigedeng
 */
public enum ScheduleTypeEnum {

    /**
     * NONE
     */
    NONE,
    /**
     * schedule by cron
     */
    CRON,

    /**
     * schedule by fixed rate (in seconds)
     */
    FIX_RATE;

    ScheduleTypeEnum() {
    }

}
