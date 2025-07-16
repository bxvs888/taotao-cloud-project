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

package com.taotao.cloud.ccsr.client.starter;

import com.taotao.cloud.ccsr.client.starter.annotation.EnableCcsrClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class EnableCcsrCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        try {
            for (String beanName : context.getRegistry().getBeanDefinitionNames()) {
                if (context.getBeanFactory() == null) {
                    continue;
                }

                Object bean = context.getBeanFactory().getBean(beanName);
                Class<?> mainClass = bean.getClass();
                if (mainClass.getAnnotation(SpringBootApplication.class) == null) {
                    continue;
                }

                // 检查主启动类是否有目标注解
                EnableCcsrClient annotation = mainClass.getAnnotation(EnableCcsrClient.class);
                if (annotation != null && annotation.enable()) {
                    return true;
                }
            }
        } catch (Exception ignored) {
        }
        return false;
    }
}
