package org.qhn.rabbitmq;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class MqDirectOrderSender extends MqRabbitSender implements IMqSender {

    public static final String QUEUENAME = "queue-direct-order";
    public static final String ROUTERKEY = "key.order.simple";

    @Override
    public String getExchangeName() {
        return RabbitExchangeConfig.EXCHANGE_DIRECT_NAME;
    }

    @Override
    public String getRouterKey() {
        return ROUTERKEY;
    }

    @Override
    public String getCorrelationID() {
        return "ORDER-CORRELATION";
    }

    public void sendMessage(Long userId, String message) {
        Map<String, Object> msgBody = new HashMap<String, Object>() {{
            put("userId", userId);
            put("message", message);
        }};
        this.sendToQueue(this.getExchangeName(), this.getRouterKey(),
                msgBody, Map.class, null);
    }

}
