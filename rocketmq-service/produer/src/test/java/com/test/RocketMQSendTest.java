package com.test;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

//发送消息
public class RocketMQSendTest {
    public static void main(String[] args) throws Exception {

    }


    @Test
    public void producer() throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        //1. 创建消息生产者, 指定生产者所属的组名
        DefaultMQProducer producer = new DefaultMQProducer("myproducer-group");
        //2. 指定Nameserver地址
        producer.setNamesrvAddr("127.0.0.1:9876");
        //3. 启动生产者
        producer.start();
        int messageCount = 100;
        for (int i = 0; i < messageCount; i++) {
            //4. 创建消息对象，指定主题、标签和消息体
            Message msg = new Message("myTopic", "t" + i, ("send Message" + i).getBytes());
            //5. 发送消息
            SendResult sendResult = producer.send(msg, 10000);
            System.out.println(sendResult);
        }

        System.out.println("====================================================================");

        CountDownLatch countDownLatch = new CountDownLatch(messageCount);

        for (int i = 0; i < messageCount; i++) {
            //4. 创建消息对象，指定主题、标签和消息体
            Message msg = new Message("myTopic-async", "t" + i, ("send Message" + i).getBytes());
            //5. 发送消息
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    countDownLatch.countDown();
                    System.out.println(sendResult);
                }

                @Override
                public void onException(Throwable e) {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        System.out.println("====================================================================");

        //6. 关闭生产者
        producer.shutdown();
    }
}