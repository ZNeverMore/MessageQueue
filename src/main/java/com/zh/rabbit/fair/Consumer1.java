package com.zh.rabbit.fair;

import com.rabbitmq.client.*;
import com.zh.rabbit.util.ConnectionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author zhangqiang
 * @date 2019-04-01
 */

public class Consumer1 {

    private static final String QUEUE_NAME = "test_work_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
//        获取连接
        Connection connection = ConnectionUtils.getConnection();
//        获取channel
        final Channel channel = connection.createChannel();
//        定义队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//        保证每次之分发一个
        channel.basicQos(1);
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
//                        消息到达触发这个方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println("consumer[1] fair receive message: " + message);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("consumer[1] fair processed");
//                    手动发送确认消息
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
//        自动应答
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, defaultConsumer);
    }

}
