package org.qhn.rabbitmq;

import java.util.HashMap;
import java.util.Map;
import org.qhn.utils.StringUtils;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitExchangeConfig {

    @Autowired
    @Qualifier("rabbitAdmin")
    private RabbitAdmin rabbitAdmin;

    // 死信的exchange name
    public static final String DEAD_LETTER_EXCHANGE = "makalo-exchange-dead";
    // 死信的key name
    public static final String DEAD_LETTER_ROUTING_KEY = "makalo-routingkey-dead";

    public static final String DEAD_LETTER_QUEUE = "makalo-queue-dead";

    public static final String EXCHANGE_DIRECT_NAME = "makalo-exchange-direct";

    public static final String EXCHANGE_STATISTIC_NAME = "makalo-exchange-statistic";

    //创建死信交换机
    @Bean
    DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE, true, false);
    }

    //创建死信队列
    @Bean
    Queue deadLetterQueue() {
        return new Queue(DEAD_LETTER_QUEUE);
    }

    @Bean
    Binding deadLetterBindding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange())
            .with(DEAD_LETTER_ROUTING_KEY);
    }

    @Bean
    TopicExchange contractStatisticExchangeDurable() {
        TopicExchange contractTopicExchange = new TopicExchange(EXCHANGE_STATISTIC_NAME, true,
            false);
        this.rabbitAdmin.declareExchange(contractTopicExchange);
        return contractTopicExchange;
    }

    @Bean
    DirectExchange contractDirectExchangeDurable() {
        DirectExchange contractDirectExchange = new DirectExchange(EXCHANGE_DIRECT_NAME, true,
            false);
        this.rabbitAdmin.declareExchange(contractDirectExchange);
        return contractDirectExchange;
    }

    private Queue operateQueueWithDLX(String queueName) {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        args.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY);
        Queue queue = new Queue(StringUtils.isEmpty(queueName) ? "" : queueName, true, false, false,
            args);
        this.rabbitAdmin.declareQueue(queue);
        return queue;
    }

    private Queue operateQueueWithTTL(String queueName, Integer time) {
        Map<String, Object> args = new HashMap<>();
        // x-dead-letter-exchange    这里声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        // x-dead-letter-routing-key  这里声明当前队列的死信路由key
        args.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY);
        // x-message-ttl  声明队列的TTL
        //消息的TTL就是消息的存活时间。RabbitMQ可以对队列和消息分别设置TTL
        //队列TTL与消息TTL的区别：
        //队列TTL：一旦消息过期，就会立即从队列中抹去（因为过期的消息肯定处于队列的头部）
        //消息TTL：即使消息过期，也不会马上从队列中抹去，因为每条消息是否过期是在即将投递到消费者之前判定的
        args.put("x-message-ttl", time);
        Queue queue = new Queue(StringUtils.isEmpty(queueName) ? "" : queueName, true, false, false,
            args);
        this.rabbitAdmin.declareQueue(queue);
        return queue;
    }

    /***************************************************************************************************/
    /******************************************Direct队列***********************************************/
    /***************************************************************************************************/

    @Bean
    Queue sms_queue() {
        return operateQueueWithDLX(MqDirectSmsSender.QUEUENAME);
    }

    @Bean
    Binding sms_queue_binding() {
        return BindingBuilder.bind(sms_queue()).to(contractDirectExchangeDurable())
            .with(MqDirectSmsSender.ROUTERKEY);
    }

    /************************************************/
    //Time To Live(TTL)、Dead Letter Exchanges（DLX）
    @Bean
    Queue order_queue() {
        return operateQueueWithTTL(MqDirectOrderSender.QUEUENAME, 60000);
    }

    @Bean
    Binding order_queue_binding() {
        return BindingBuilder.bind(order_queue()).to(contractDirectExchangeDurable())
            .with(MqDirectOrderSender.ROUTERKEY);
    }
}
