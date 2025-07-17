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

package com.taotao.cloud.cache.api;

/**
 * 拦截器接口
 *
 * （1）耗时统计
 * （2）监听器
 *
 * @author shuigedeng
 * @since 2024.06
 * @param <K> key
 * @param <V> value
 */
public interface ICacheInterceptor<K, V> {

    /**
     * 方法执行之前
     * @param context 上下文
     * @since 2024.06
     */
    void before(ICacheInterceptorContext<K, V> context);

    /**
     * 方法执行之后
     * @param context 上下文
     * @since 2024.06
     */
    void after(ICacheInterceptorContext<K, V> context);
}
