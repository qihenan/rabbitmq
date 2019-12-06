package org.qhn.handler;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.qhn.rabbitmq.MqRabbitSender;
import org.qhn.rabbitmq.RabbitExchangeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;


/**
 * 消息队列回调备注:
 * 消息队列处理类命名以queue结尾
 * 异常必须用try catch块包裹,ack/nack务必手动返回
 */
@Service
@Slf4j
public class RabbitDlqProcessor {

    private static final Logger logger = LoggerFactory.getLogger(RabbitDlqProcessor.class);

    @Autowired
    protected RabbitTemplate rabbitTemplate;

    /**
     * 死信队列处理
     */
    @RabbitListener(queues = {RabbitExchangeConfig.DEAD_LETTER_QUEUE})
    public void onMessage(@Payload org.springframework.amqp.core.Message amqpMessage,
        @Headers Map<String, Object> headers, Channel channel) throws Exception {
        String originalExchange = (String) headers.get(MqRabbitSender.ORIGINAL_EXCHANGE);
        String originalRouterkey = (String) headers.get(MqRabbitSender.ORIGINAL_ROUTER_KEY);
        String messageBodyClass = (String) headers.get(MqRabbitSender.MESSAGE_BODY_CLASS);
        String failedReason = (String) headers.get("x-first-death-reason");
        Long timestamp = (Long) headers.get("timestamp");
        String charsetName = (String) headers.get(AmqpHeaders.CONTENT_ENCODING);
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        String jsonString = new String(amqpMessage.getBody(), charsetName);
        try {
            //写入系统日志
            logger.info(
                "*********************************************************************************************************************************************************************");
            logger.info("消息内容:{}", jsonString);
            logger.info("消息类型:{}", messageBodyClass);
            logger.info("发送消息时间:{}", new Long(timestamp).toString());
            logger.info("交换机:{}", originalExchange);
            logger.info("路由:{}", originalRouterkey);
            logger.info("成为死信原因:{}", failedReason);
            logger.info(
                "*********************************************************************************************************************************************************************");
        } catch (Exception e) {
            logger.error("dead queue process error!", e);
        } finally {
            logger.info("进入死信队列!");
            //查询订单状态，如果未支付，则更新订单状态为取消，否则不作处理
            Map<String, Object> map = (Map<String, Object>) JSONObject.parse(jsonString);
            Integer userId = (Integer) map.get("userId");
            boolean flag = selectOrderPayStatusByUserId(userId);//true支付false未支付
            if (!flag) {
                //业务处理 取消订单
                logger.info("userId为{}的用户十分钟内未支付，取消订单!", userId);
            }
            channel.basicAck(deliveryTag, false);
        }

    }

    private boolean selectOrderPayStatusByUserId(Integer userId) {
        if (1 == userId) {
            return true;
        } else if (2 == userId) {
            return false;
        }
        return false;
    }

}
