package com.zh.rabbit.work;

import com.rabbitmq.client.*;
import com.zh.rabbit.util.ConnectionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author zhangqiang
 * @date 2019-04-01
 */

public class Consumer2 {

    private static final String QUEUE_NAME = "test_work_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
//        获取连接
        Connection connection = ConnectionUtils.getConnection();
//        获取channel
        Channel channel = connection.createChannel();
//        定义队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            //                        消息到达触发这个方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println("consumer[2] receive message: " + message);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("consumer[2] processed");
                }
            }
        };
        boolean autoAck = true;
        channel.basicConsume(QUEUE_NAME, autoAck, defaultConsumer);
    }

}
