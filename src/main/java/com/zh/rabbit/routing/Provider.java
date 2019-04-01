package com.zh.rabbit.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zh.rabbit.util.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author zhangqiang
 * @date 2019-04-01
 */

public class Provider {

    private static final String EXCHANGE_NAME = "test_exchange_direct";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String message = "hello direct";
//        error  info  warning
        String routingKey = "info";
        channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
        System.out.println("routing mode publish message: " + message);
        channel.close();
        connection.close();

    }

}
