package org.qhn.rabbitmq;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.AlwaysRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.host}")
    private String addresses;
    @Value("${spring.rabbitmq.port}")
    private Integer port;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;
    @Value("${spring.rabbitmq.connection-timeout}")
    private Integer connectionTimeout = 15000;
    @Value("${spring.rabbitmq.publisher-confirms}")
    private boolean publisherConfirms = true;
    @Value("${spring.rabbitmq.publisher-returns}")
    private boolean publisherReturns = true;
    @Value("${spring.rabbitmq.template.mandatory}")
    private boolean templateMandatory = true;
    //    @Value("${spring.rabbitmq.template.retry.enabled}")
//    private boolean templateRetryEnabled;//未设置
    @Value("${spring.rabbitmq.template.retry.initial-interval}")
    private Integer templateRetryInitialInterval;
    //    @Value("${spring.rabbitmq.template.retry.max-attempts}")
//    private Integer templateRetryMaxAttempts ;//未设置
    @Value("${spring.rabbitmq.template.retry.max-interval}")
    private Integer templateRetryMaxInterval;
    @Value("${spring.rabbitmq.template.retry.multiplier}")
    private Double templateRetryMultiplier;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        /*
        自定义channel
//        Connection connection =connectionFactory.createConnection();
//        Channel channel = connection.createChannel(true);
//        channel.queueDeclare("queue.persistent.name", true, false, false, null);
        */
        connectionFactory.setAddresses(this.addresses);
        connectionFactory.setPort(this.port);
        connectionFactory.setUsername(this.username);
        connectionFactory.setPassword(this.password);
        connectionFactory.setVirtualHost(this.virtualHost);
        connectionFactory.setConnectionTimeout(connectionTimeout);
        connectionFactory.setPublisherConfirms(this.publisherConfirms);
        connectionFactory.setPublisherReturns(this.publisherReturns);
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(
            this.connectionFactory());//特别注意@Configuration类内@Bean注解是默认singleton
        rabbitTemplate.setMandatory(this.templateMandatory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        //这个RetryTemplate是发送端的重试（当发送消息错误时）
        RetryTemplate retryTemplate = new RetryTemplate();
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(this.templateRetryInitialInterval);
        backOffPolicy.setMultiplier(this.templateRetryMultiplier);
        backOffPolicy.setMaxInterval(this.templateRetryMaxInterval);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.setRetryPolicy(new AlwaysRetryPolicy());
        rabbitTemplate.setRetryTemplate(retryTemplate);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        return rabbitTemplate;
    }

    private final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            System.out.println("本次消息的唯一标识是:" + correlationData);
            System.out.println("是否存在消息拒绝接收？" + ack);
            if (!ack) {
                System.out.println("消息拒绝接收的原因是:" + cause);
                System.err.println("WRONG....queue-sms-simple");
            }
        }
    };

    private final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(org.springframework.amqp.core.Message message,
            int replyCode, String replyText, String exchange, String routingKey) {
            System.err.println("WRONG....queue-sms-simple");
            System.err.println("return exchange: " + exchange + ", routingKey: "
                + routingKey + ", replyCode: " + replyCode + ", replyText: " + replyText);
            System.out.println("err code :" + replyCode);
            System.out.println("错误消息的描述 :" + replyText);
            System.out.println("错误的交换机是 :" + exchange);
            System.out.println("错误的路由键是 :" + routingKey);
        }
    };

}
