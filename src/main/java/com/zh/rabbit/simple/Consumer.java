package com.zh.rabbit.simple;

import com.rabbitmq.client.*;
import com.zh.rabbit.util.ConnectionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * 消费者获取信息
 *
 * @author zhangqiang
 * @date 2019-04-01
 */

public class Consumer {

    private static final String QUEUE_NAME = "test_simple_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
//        获取连接
        Connection connection = ConnectionUtils.getConnection();
//        创建channel
        Channel channel = connection.createChannel();
//        队列声明
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//        定义消费者
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
//            获取到达的消息
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println("consumer receive message: " + message);
            }
        };
//        监听队列
        channel.basicConsume(QUEUE_NAME, true, defaultConsumer);
    }

}
