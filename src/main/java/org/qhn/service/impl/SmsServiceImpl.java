package org.qhn.service.impl;

import com.rabbitmq.client.Channel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.qhn.model.UserInfo;
import org.qhn.rabbitmq.MqDirectSmsSender;
import org.qhn.service.SmsService;
import org.qhn.utils.sms.ChuangLanSmsUtil;
import org.qhn.utils.sms.InformMessageEnum;
import org.qhn.utils.sms.SmsSendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * 短信发送服务
 *
 * @author qihena
 * @date 2019/12/03
 */
@Service
public class SmsServiceImpl implements SmsService {

    private Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

    @Autowired
    private MqDirectSmsSender mqDirectSmsSender;

    private static final Map<String, String[]> URGE_MESSAGE_MAP = new HashMap<>();

    static {
        URGE_MESSAGE_MAP.put("miaodi", new String[]{});
        URGE_MESSAGE_MAP.put("sudun", new String[]{InformMessageEnum.LOGIN_SMS_CODE.getCode(),
            InformMessageEnum.LOGIN_SMS_CODE.getCode()});
        URGE_MESSAGE_MAP.put("chuanglan", new String[]{});
    }

    @Override
    public boolean sendNote(List<Object> message, InformMessageEnum informMessageEnum,
        UserInfo loanCiPersonalInfor) {
        try {
            String messageContent = informMessageEnum.getMessage();
            for (int i = 0; i < message.size(); i++) {
                messageContent = messageContent.replace("{" + i + "}", message.get(i).toString());
            }
            mqDirectSmsSender
                .sendMessage(loanCiPersonalInfor.getMobile(), loanCiPersonalInfor.getUserId(),
                    messageContent, false);
        } catch (Exception e) {
            logger.error("短信发送异常！", e);
            return false;
        }
        return true;
    }


    @Override
    public boolean sendNote(InformMessageEnum informMessageEnum,
        UserInfo loanCiPersonalInfor) {
        try {
            String messageContent = informMessageEnum.getMessage();
            mqDirectSmsSender
                .sendMessage(loanCiPersonalInfor.getMobile(), loanCiPersonalInfor.getUserId(),
                    messageContent, false);
        } catch (Exception e) {
            logger.error("短信发送异常！", e);
            return false;
        }
        return true;
    }

    @RabbitListener(queues = {MqDirectSmsSender.QUEUENAME})
    public void onMessage(@Payload Map<String, Object> body, @Headers Map<String, Object> headers,
        Channel channel) throws Exception {
        boolean judge = true;
        String errorInfo = null;
        String tempMessage = null;
        Long userId = 0L;
        try {
            tempMessage = (String) body.get("message");
            String mobile = (String) body.get("mobile");
            userId = Long.parseLong(body.get("userId").toString());
            if (mobile == null || "".equals(mobile) || userId == null || "".equals(userId)) {
                throw new Exception("短信消息队列-未获取到用户|手机号");
            }
            SmsSendResponse smsSendResponse = ChuangLanSmsUtil
                .sendSingleMsg(tempMessage, mobile);
            if (!"0".equals(smsSendResponse.getCode())) {
                judge = false;
                errorInfo = smsSendResponse.getErrorMsg();
                logger.error("发送短信失败:error msg: ", errorInfo);
            }
        } catch (Exception e) {
            logger.error("发送短信失败..", e);
        } finally {
            Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
            if (judge) {
                channel.basicAck(deliveryTag, false);
                System.out.println("短信发送成功！");
            } else {
                channel.basicNack(deliveryTag, false, false);//重置回死信队列
                System.out.println("重置回死信队列！");
            }
            //记录短信日志
        }
    }

}
