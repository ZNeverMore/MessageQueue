package com.zh.rabbit.transaction;

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

    private static final String QUEUE_NAME = "test_queue_transaction";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false,false,null
        );
        channel.basicConsume(QUEUE_NAME, true, new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("transaction mode consumer receive message: " + new String(body, StandardCharsets.UTF_8));
            }
        });


    }

}
