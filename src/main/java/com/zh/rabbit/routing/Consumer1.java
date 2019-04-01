package com.zh.rabbit.routing;

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

    private static final String EXCHANGE_NAME = "test_exchange_direct";
    private static final String QUEUE_NAME = "test_queue_direct_1";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        final Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "error");
        channel.basicQos(1);
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            //消息到达触发这个方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println("routing mode consumer[1] receive message: " + message);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("routing mode consumer[1] processed");
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
