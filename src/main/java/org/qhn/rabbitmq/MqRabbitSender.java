package org.qhn.rabbitmq;

import org.qhn.utils.StringUtils;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class MqRabbitSender {

    public static final String ORIGINAL_EXCHANGE = "x-makalo-original-exchange";
    public static final String ORIGINAL_ROUTER_KEY = "x-makalo-original-routerkey";
    public static final String MESSAGE_BODY_CLASS = "x-makalo-message-body-class";
    public static final String MESSAGE_HANDLER = "x-makalo-message-handler";

//    protected static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    protected RabbitTemplate rabbitTemplate;

    public String getCorrelationID() {
        return "DEFAULT-CORRELATION-ID";
    }

//    protected void sendSimple(Object msgBody, java.lang.Class javaClass, String msgHandler) {
//        super.sendToQueue(this.getExchangeName(), this.getRouterKey(), msgBody, javaClass, msgHandler);
//    }

    protected boolean sendToQueue(String exchangeName, String routerKey, Object msgBody,
        Class javaClass, String msgHandler) {
        if (StringUtils.isEmpty(exchangeName) || StringUtils.isEmpty(routerKey)) {
            return false;
        }
        /*
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(payload);
        System.out.println(json);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
                Map<String, Object> properties = new HashMap<>();
        properties.put("send_time", simpleDateFormat.format(new Date()));
        MessageHeaders mhs = new MessageHeaders(properties);
                Message msg = MessageBuilder.createMessage(new HashMap<String, Object>() {{
            put("mobile", mobile);
            put("userId", userId);
            put("message", message);
        }}, mhs);
        Message msg = new Message(json.getBytes(), messageProperties);
        */
        MessageProperties mp = new MessageProperties();
        mp.setHeader(ORIGINAL_EXCHANGE, exchangeName);
        mp.setHeader(ORIGINAL_ROUTER_KEY, routerKey);
        mp.setHeader(MESSAGE_BODY_CLASS, javaClass.getName());
        if (!StringUtils.isEmpty(msgHandler)) {
            mp.setHeader(MESSAGE_HANDLER, msgHandler);
        }
        org.springframework.amqp.core.Message m = rabbitTemplate.getMessageConverter()
            .toMessage(msgBody, mp);
        rabbitTemplate.convertAndSend(exchangeName, routerKey, m,
            new CorrelationData(this.getCorrelationID()));
        return true;
    }

    protected boolean sendToQueue(String exchangeName, String routerKey, Object msgBody,
        Class javaClass, String msgHandler, String expiration) {
        if (StringUtils.isEmpty(exchangeName) || StringUtils.isEmpty(routerKey)) {
            return false;
        }
        MessageProperties mp = new MessageProperties();
        mp.setHeader(ORIGINAL_EXCHANGE, exchangeName);
        mp.setHeader(ORIGINAL_ROUTER_KEY, routerKey);
        mp.setHeader(MESSAGE_BODY_CLASS, javaClass.getName());
        mp.setExpiration(expiration);
        if (!StringUtils.isEmpty(msgHandler)) {
            mp.setHeader(MESSAGE_HANDLER, msgHandler);
        }
        org.springframework.amqp.core.Message m = rabbitTemplate.getMessageConverter()
            .toMessage(msgBody, mp);
        rabbitTemplate.convertAndSend(exchangeName, routerKey, m,
            new CorrelationData(this.getCorrelationID()));
        return true;
    }

}
