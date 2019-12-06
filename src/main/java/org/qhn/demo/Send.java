package org.qhn.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @Auther: qihenan
 * @Date: 2019/10/12 17:42
 * @Description:
 */
public class Send {

    //1.channel.txSelect()声明启动事务模式；
    //2.channel.txComment()提交事务；
    //3.channel.txRollback()回滚事务；
    //说明：由于事务会严重影响效率，所以我们一般不使用，而是用发送方确认模式来保证数据的可靠性
    //发送方确认模式和事务区别:
    //比如发送10条数据
    //事务能保证在第5条发送错误的时候就回滚，RabbitMQ里面没有一条数据。
    //而发送者确认模式在第5条发送错误的时候会抛出一个异常，生产者就能抓住这个异常从而得知没有全部成功，但是已经发送的数据却已经存在于RabbitMQ了。
    //发送方确认有3种方式。2种同步，1种异步
    //方式一：channel.waitForConfirms()普通发送方确认模式；消息到达交换器，就会返回true。
    //方式二：channel.waitForConfirmsOrDie()批量确认模式；使用同步方式等所有的消息发送之后才会执行后面代码，只要有一个消息未到达交换器就会抛出IOException异常。
    //方式三：channel.addConfirmListener()异步监听发送方确认模式
    //【注意】必须要先启动发送者确认模式channel.confirmSelect();

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv)
        throws IOException, TimeoutException, InterruptedException {
        //创建连接工程
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        //创建连接
        Connection connection = factory.newConnection();

        //创建消息通道
        Channel channel = connection.createChannel();

        Map<String,Object> args = new HashMap<>();
        args.put("x-message-ttl",20000);
        //生成一个消息队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, args);

        // 创建失败通知
        channel.addReturnListener(
            (replyCode, replyText, exchange, routingKey, properties, body) -> {

                System.out.println("ERROR DATA:" + new String(body));
                System.out.println("replyText:" + replyText);//错误原因
                System.out.println("replyCode:" + replyCode);
                System.out.println("body:" + String.valueOf(body));//发送的消息内容
                System.out.println("================");
            });

        // 异步确认
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleNack(long deliveryTag, boolean multiple) {
                System.out.println("handleNack==>" + deliveryTag + "----" + multiple);
            }

            @Override
            public void handleAck(long deliveryTag, boolean multiple) {
                System.out.println("handleAck==>" + deliveryTag + "----" + multiple);
            }
        });

        // 启用发送确认模式
        channel.confirmSelect();

        //1.发送者确认模式只是告诉生产者是否发送成功，至于成功和失败后的具体处理还是代码自己实现。
        //2.同步单条确认channel.waitForConfirms()会返回是否成功。同步批量确认channel.waitForConfirmsOrDie()可通过异常来判断是否成功。
        //3.异步确认channel.addConfirmListener()。有两个参数（long deliveryTag, boolean multiple）。第一个参数表示消息的Id，第二个参数表示是否为批量。如果第二个参数为true，那么编号<=deliveryTag的所有消息都确认了。
        //4.发送者确认模式和失败通知可以一起使用。无论消息的路由键是否有队列绑定，都会返回Ack表示成功。而如果没有队列绑定路由键，同时又启用了失败通知，那么还会在调用channel.addConfirmListener()之前去调用channel.addReturnListener()

        for (int i = 0; i < 10; i++) {
            String message = "Hello World RabbitMQ count: " + i;

            //发布消息，第一个参数表示路由（Exchange名称），未""则表示使用默认消息路由
            //注意：RabbitMQ的消息类型只有一种，那就是byte[]
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

            System.out.println(" [x] Sent '" + message + "'");
        }

        //暂停2秒，以便接收回调。因为通道关闭后就不能接收到回调消息了
        Thread.sleep(2000);

        //关闭消息通道和连接
        channel.close();
        connection.close();
    }

}
