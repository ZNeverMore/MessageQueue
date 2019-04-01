package com.zh.rabbit.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.zh.rabbit.util.ConnectionUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

/**
 * @author zhangqiang
 * @date 2019-04-01
 */

public class ProviderAsyn {

    private static final String QUEUE_NAME = "test_queue_confirm_asyn";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
//        将channel设置为confirm模式
        channel.confirmSelect();
//        存储的是未确认的消息标识
        final SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());
//        通道添加监听
        channel.addConfirmListener(new ConfirmListener() {
//            回执没有问题
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                if (multiple) {
                    System.out.println("----------->handleAck multiple  true");
                    confirmSet.headSet(deliveryTag + 1).clear();
                } else {
                    System.out.println("----------->handleAck multiple false");
                    confirmSet.remove(deliveryTag);
                }
            }
//            回执有问题
            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                if (multiple) {
                    System.out.println("---------->handleNack multiple true");
                    confirmSet.headSet(deliveryTag + 1).clear();
                } else {
                    System.out.println("---------->handleNack multiple false");
                    confirmSet.remove(deliveryTag);
                }

            }
        });
        String messageStr = "many message";
        while (true) {
            long messageNo = channel.getNextPublishSeqNo();
            channel.basicPublish("", QUEUE_NAME, null, messageStr.getBytes());
            confirmSet.add(messageNo);
        }


    }

}
