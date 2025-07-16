/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.mq.client.consumer.api;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public interface IMqConsumer {

    /**
     * 订阅
     * @param topicName topic 名称
     * @param tagRegex 标签正则
     */
    void subscribe(String topicName, String tagRegex);

    /**
     * 取消订阅
     * @param topicName topic 名称
     * @param tagRegex 标签正则
     */
    void unSubscribe(String topicName, String tagRegex);

    /**
     * 注册监听器
     * @param listener 监听器
     */
    void registerListener(final IMqConsumerListener listener);
}
