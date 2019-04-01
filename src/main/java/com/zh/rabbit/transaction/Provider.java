package com.zh.rabbit.transaction;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zh.rabbit.util.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 * 事务模式会降低消息吞吐量
 *
 * @author zhangqiang
 * @date 2019-04-01
 */

public class Provider {

    private static final String QUEUE_NAME = "test_queue_transaction";

    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "hello transaction";
        try {
//           代表开启事务
            channel.txSelect();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
//            int a = 1/0;
            System.out.println("transaction mode provider publish message: " + message);
//            提交事务
            channel.txCommit();
        } catch (Exception e) {
            channel.txRollback();
            System.out.println("publish message rollback");
        }
        channel.close();
        connection.close();

    }

}
