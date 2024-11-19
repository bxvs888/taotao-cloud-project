package com.taotao.cloud.message.biz.austin.handler.handler;

import com.taotao.cloud.message.biz.austin.common.domain.RecallTaskInfo;
import com.taotao.cloud.message.biz.austin.common.domain.TaskInfo;

/**
 * @author shuigedeng
 * 消息处理器
 */
public interface Handler {

    /**
     * 处理器
     *
     * @param taskInfo
     */
    void doHandler(TaskInfo taskInfo);

    /**
     * 撤回消息
     *
     * @param recallTaskInfo
     * @return
     */
    void recall(RecallTaskInfo recallTaskInfo);

}
