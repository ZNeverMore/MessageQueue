package com.zh.rabbit.publish;

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

    private static final String EXCHANGE_NAME = "test_exchange_fanout";

    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
//        声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String message = "hello e";
//        发送消息
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
        System.out.println("provider publish message to exchange");
        channel.close();
        connection.close();

    }

}
