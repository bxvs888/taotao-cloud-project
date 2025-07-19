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

package com.taotao.cloud.netty.itcast.source;

import com.google.gson.*;
import com.taotao.cloud.netty.itcast.protocol.Serializer;

public class TestGson {
    public static void main(String[] args) {
        Gson gson =
                new GsonBuilder()
                        .registerTypeAdapter(Class.class, new Serializer.ClassCodec())
                        .create();
        System.out.println(gson.toJson(String.class));
    }
}
