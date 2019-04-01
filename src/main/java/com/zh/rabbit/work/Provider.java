package com.zh.rabbit.work;

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

    private static final String QUEUE_NAME = "test_work_queue";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
//        获取连接
        Connection connection = ConnectionUtils.getConnection();
//        获取channel
        Channel channel = connection.createChannel();
//        声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        for (int i = 0; i < 50; i++) {
            String message = "hello z: " + i;
            System.out.println("work queue publish message: " + message);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            Thread.sleep(i*20);
        }
        channel.close();
        connection.close();
    }

}
