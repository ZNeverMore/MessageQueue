package com.zh.rabbit.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zh.rabbit.util.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 简单队列的不足：
 *  耦合性高，生产者一一对应消费者（如果有多个消费者消费队列中消息，就不行了）
 *
 * @author zhangqiang
 * @date 2019-04-01
 */

public class Provider {

    private static final String QUEUE_NAME = "test_simple_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
//        获取一个连接
        Connection connection = ConnectionUtils.getConnection();
//      从连接中获取通道
        Channel channel = connection.createChannel();
//        创建队列
       channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//       创建消息
        String message = "hello z";
//        发送消息
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

        System.out.println("provider publish message: " + message);

//        关闭连接
        channel.close();

        connection.close();

    }

}
