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

package com.taotao.cloud.auth.biz.jpa.generator;

import com.taotao.boot.data.jpa.hibernate.identifier.AbstractUuidGenerator;
import com.taotao.cloud.auth.biz.jpa.entity.TtcRegisteredClient;
import java.lang.reflect.Member;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.factory.spi.CustomIdGeneratorCreationContext;

/**
 * <p>OAuth2RegisteredClient Id 生成器 </p>
 * <p>
 * 指定ID生成器，解决实体ID无法手动设置问题。
 *
 */
public class TtcRegisteredClientUuidGeneratorType extends AbstractUuidGenerator {

    public TtcRegisteredClientUuidGeneratorType(
            TtcRegisteredClientUuidGenerator config,
            Member idMember,
            CustomIdGeneratorCreationContext creationContext) {
        super(idMember);
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object)
            throws HibernateException {
        if (ObjectUtils.isEmpty(object)) {
            throw new HibernateException(new NullPointerException());
        }

        TtcRegisteredClient ttcRegisteredClient = (TtcRegisteredClient) object;

        if (StringUtils.isEmpty(ttcRegisteredClient.getId())) {
            return super.generate(session, object);
        } else {
            return ttcRegisteredClient.getId();
        }
    }
}
