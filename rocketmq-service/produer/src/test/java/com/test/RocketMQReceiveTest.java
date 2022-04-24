package com.test;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

//接收消息
public class RocketMQReceiveTest {
    public static void main(String[] args) throws MQClientException {
        //1. 创建消息消费者, 指定消费者所属的组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("myconsumer-group");
//        consumer.setMessageModel(MessageModel.BROADCASTING);
        //2. 指定Nameserver地址
        consumer.setNamesrvAddr("127.0.0.1:9876");consumer.setMessageModel(MessageModel.BROADCASTING);
        //3. 指定消费者订阅的主题和标签
        consumer.subscribe("myTopic", "*");
        //4. 设置回调函数，编写处理消息的方法
        consumer.registerMessageListener((MessageListenerOrderly) (msgs, context) -> {
            msgs.forEach(messageExt -> {
                String tags = messageExt.getTags();
                byte[] body = messageExt.getBody();
                System.out.println(Thread.currentThread().getId() + "\t" + tags + "\t" + new String(body));
            });
            //返回消费状态
            return ConsumeOrderlyStatus.SUCCESS;
        });
        //5. 启动消息消费者
        consumer.start();
        System.out.println("Consumer Started.");
    }
}