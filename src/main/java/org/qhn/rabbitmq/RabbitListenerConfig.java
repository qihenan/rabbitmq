package org.qhn.rabbitmq;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@EnableRabbit
public class RabbitListenerConfig implements RabbitListenerConfigurer {

    @Value("${spring.rabbitmq.listener.simple.acknowledge-mode}")
    private AcknowledgeMode acknowledgeMode = AcknowledgeMode.MANUAL;
    @Value("${spring.rabbitmq.listener.simple.concurrency}")
    private Integer concurrency = 3;
    @Value("${spring.rabbitmq.listener.simple.max-concurrency}")
    private Integer maxConcurrency = 6;
    @Value("${spring.rabbitmq.listener.simple.prefetch}")
    private Integer prefetch = 1;
    @Value("${spring.rabbitmq.listener.simple.auto-startup}")
    private boolean autoStartup;
    @Value("${spring.rabbitmq.listener.simple.default-requeue-rejected}")
    private boolean defaultRequeueRejected;

    @Value("${spring.rabbitmq.listener.retry.initial-interval}")
    private Integer listenerRetryInitialInterval;
    @Value("${spring.rabbitmq.listener.retry.max-interval}")
    private Integer listenerRetryMaxInterval;
    @Value("${spring.rabbitmq.listener.retry.multiplier}")
    private Double listenerRetryMultiplier;
    @Value("${spring.rabbitmq.listener.retry.max-attempts}")
    private Integer listenerRetryMaxAttempts;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Bean
    public DefaultMessageHandlerMethodFactory handlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(new MappingJackson2MessageConverter());
        return factory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory listener = new SimpleRabbitListenerContainerFactory();
        listener.setConnectionFactory(this.connectionFactory);
        listener.setAcknowledgeMode(this.acknowledgeMode);
        listener.setConcurrentConsumers(this.concurrency);
        listener.setMaxConcurrentConsumers(this.maxConcurrency);
        listener.setPrefetchCount(this.prefetch);
        listener.setAutoStartup(this.autoStartup);
        listener.setDefaultRequeueRejected(this.defaultRequeueRejected);
        //这个RetryTemplate是接收端的重试(当无法消费者时，消费者抛出异常或nack不会重试)
        RetryTemplate retryTemplate = new RetryTemplate();
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(this.listenerRetryInitialInterval);
        backOffPolicy.setMultiplier(this.listenerRetryMultiplier);
        backOffPolicy.setMaxInterval(this.listenerRetryMaxInterval);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        SimpleRetryPolicy smp = new SimpleRetryPolicy();
        smp.setMaxAttempts(this.listenerRetryMaxAttempts);
        retryTemplate.setRetryPolicy(smp);
        listener.setRetryTemplate(retryTemplate);
        return listener;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(this.handlerMethodFactory());
    }
}
