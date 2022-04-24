package com.test;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;

//发送消息
public class RocketMQSendTest {
    public static void main(String[] args) throws Exception {
        //1. 创建消息生产者, 指定生产者所属的组名
        DefaultMQProducer producer = new DefaultMQProducer("myproducer-group");
        //2. 指定Nameserver地址
        producer.setNamesrvAddr("127.0.0.1:9876");
        //3. 启动生产者
        producer.start();
        for (int i = 0; i < 100; i++) {
            //4. 创建消息对象，指定主题、标签和消息体
            Message msg = new Message("myTopic", "t" + i, ("send Message" + i).getBytes());
            //5. 发送消息
            SendResult sendResult = producer.send(msg, 10000);
            SendStatus sendStatus = sendResult.getSendStatus();
            System.out.println(sendStatus);
        }

        //6. 关闭生产者
        producer.shutdown();
    }
}