package com.taotao.cloud.message.biz.austin.cron.xxl.enums;

/**
 * 调度过期策略
 *
 * @author shuigedeng
 */
public enum MisfireStrategyEnum {

    /**
     * do nothing
     */
    DO_NOTHING,

    /**
     * fire once now
     */
    FIRE_ONCE_NOW;

    MisfireStrategyEnum() {
    }
}
