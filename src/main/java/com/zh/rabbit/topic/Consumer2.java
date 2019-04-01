package com.zh.rabbit.topic;

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

    private static final String QUEUE_NAME = "test_queue_topic_2";
    private static final String EXCHANGE_NAME = "test_exchange_topic";

    public static void main(String[] args) throws IOException, TimeoutException {
//        获取连接
        Connection connection = ConnectionUtils.getConnection();
//        创建channel
        Channel channel = connection.createChannel();
//        队列声明
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//        #代表一个或多个， .代表一个
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "goods.#");
//        定义消费者
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            //            获取到达的消息
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println("topic mode consumer[2] receive message: " + message);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    System.out.println("topic mode consumer[2] processed");
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
//        监听队列
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, defaultConsumer);
    }

}
