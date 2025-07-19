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

package com.taotao.cloud.netty.atguigu.netty.codec;

import com.taotao.cloud.netty.grpc.code2.Student;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/*
说明
1. 我们自定义一个Handler 需要继续netty 规定好的某个HandlerAdapter(规范)
2. 这时我们自定义一个Handler , 才能称为一个handler
 */
// public class NettyServerHandler extends ChannelInboundHandlerAdapter {
public class NettyServerHandler extends SimpleChannelInboundHandler<Student> {

    // 读取数据实际(这里我们可以读取客户端发送的消息)
    /*
    1. ChannelHandlerContext ctx:上下文对象, 含有 管道pipeline , 通道channel, 地址
    2. Object msg: 就是客户端发送的数据 默认Object
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Student msg) throws Exception {

        // 读取从客户端发送的Student

        System.out.println("客户端发送的数据 id=" + msg.getId() + " 名字=" + msg.getName());
    }

    //    //读取数据实际(这里我们可以读取客户端发送的消息)
    //    /*
    //    1. ChannelHandlerContext ctx:上下文对象, 含有 管道pipeline , 通道channel, 地址
    //    2. Object msg: 就是客户端发送的数据 默认Object
    //     */
    //    @Override
    //    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    //
    //        //读取从客户端发送的Student
    //
    //        Student student = (Student) msg;
    //
    //        System.out.println("客户端发送的数据 id=" + student.getId() + " 名字=" + student.getName());
    //    }

    // 数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        // writeAndFlush 是 write + flush
        // 将数据写入到缓存，并刷新
        // 一般讲，我们对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~(>^ω^<)喵1", CharsetUtil.UTF_8));
    }

    // 处理异常, 一般是需要关闭通道

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
