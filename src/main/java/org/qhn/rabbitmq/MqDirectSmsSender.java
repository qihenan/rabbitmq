package org.qhn.rabbitmq;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class MqDirectSmsSender extends MqRabbitSender implements IMqSender {

    public static final String QUEUENAME = "queue-direct-sms";
    public static final String ROUTERKEY = "key.sms.simple";

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
        return "SMS-CORRELATION";
    }

    public void sendMessage(String mobile, Long userId, String message, boolean isUrgeSms) {

        Map<String, Object> msgBody = new HashMap<String, Object>() {{
            put("mobile", mobile);
            put("userId", userId);
            put("message", message);
            put("isUrgeSms", isUrgeSms);
        }};
        this.sendToQueue(this.getExchangeName(), this.getRouterKey(),
                msgBody, Map.class, null);
    }

}
