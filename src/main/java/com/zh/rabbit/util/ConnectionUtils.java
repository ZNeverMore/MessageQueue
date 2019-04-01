package com.zh.rabbit.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author zhangqiang
 * @date 2019-04-01
 */

public class ConnectionUtils {

    /**
     * 获取MQ连接
     *
     * @return
     */
    public static Connection getConnection() throws IOException, TimeoutException {

        //定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
//        设置服务地址
//        factory.setHost("127.0.0.1");
//        AMQP端口
//        factory.setPort(5672);
//        设置vhost
//        factory.setVirtualHost("/");
//        factory.setUsername("guest");
//        factory.setPassword("guest");

        return factory.newConnection();
    }

    public static Channel getChannel() throws IOException, TimeoutException {
        return getConnection().createChannel();
    }

    public static Channel getChannel(String queueName, String exchangeName, String[] routingKey) throws IOException, TimeoutException {
        Channel channel = getConnection().createChannel();
//       声明队列
        channel.queueDeclare(queueName, false, false, false, null);
//        绑定队列到转发器
        for (String s : routingKey) {
            channel.queueBind(queueName, exchangeName, s);
        }
//        保证每次只分发一个
        channel.basicQos(1);
        return channel;
    }

    public static Channel getChannel(String queueName, String exchangeName) throws IOException, TimeoutException {
        Channel channel = getConnection().createChannel();
//       声明队列
        channel.queueDeclare(queueName, false, false, false, null);
//        绑定队列到转发器
        channel.queueBind(queueName, exchangeName, "");
//        保证每次只分发一个
        channel.basicQos(1);
        return channel;
    }


}
