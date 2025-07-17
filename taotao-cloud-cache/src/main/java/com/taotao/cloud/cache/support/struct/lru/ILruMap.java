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

package com.taotao.cloud.cache.support.struct.lru;

import com.taotao.cloud.cache.api.ICacheEntry;

/**
 * LRU map 接口
 * @author shuigedeng
 * @since 2024.06
 */
public interface ILruMap<K, V> {

    /**
     * 移除最老的元素
     * @return 移除的明细
     * @since 2024.06
     */
    ICacheEntry<K, V> removeEldest();

    /**
     * 更新 key 的信息
     * @param key key
     * @since 2024.06
     */
    void updateKey(final K key);

    /**
     * 移除对应的 key 信息
     * @param key key
     * @since 2024.06
     */
    void removeKey(final K key);

    /**
     * 是否为空
     * @return 是否
     * @since 2024.06
     */
    boolean isEmpty();

    /**
     * 是否包含元素
     * @param key 元素
     * @return 结果
     * @since 2024.06
     */
    boolean contains(final K key);
}
