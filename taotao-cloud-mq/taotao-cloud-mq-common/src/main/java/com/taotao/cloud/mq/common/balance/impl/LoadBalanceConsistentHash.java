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

package com.taotao.cloud.mq.common.balance.impl;

import com.taotao.cloud.mq.common.balance.ILoadBalanceContext;
import com.taotao.cloud.mq.common.balance.IServer;
import com.taotao.boot.common.support.hash.ConsistentHashingBs;
import com.taotao.boot.common.support.hash.IConsistentHashing;
import com.taotao.boot.common.support.hash.api.IHashCode;

public class LoadBalanceConsistentHash<T extends IServer> extends AbstractLoadBalanceHash<T> {
    public LoadBalanceConsistentHash(IHashCode hashCode) {
        super(hashCode);
    }

    protected T doSelect(ILoadBalanceContext<T> context) {
        IConsistentHashing<T> consistentHashing =
                ConsistentHashingBs.<T>newInstance()
                        .hashCode(this.hashCode)
                        .nodes(context.servers())
                        .build();
        String hashKey = context.hashKey();
        return (T) (consistentHashing.get(hashKey));
    }
}
