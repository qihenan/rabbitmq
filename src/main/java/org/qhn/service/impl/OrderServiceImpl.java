package org.qhn.service.impl;

import java.util.Date;
import org.qhn.model.UserInfo;
import org.qhn.rabbitmq.MqDirectOrderSender;
import org.qhn.service.OrderService;
import org.qhn.utils.sms.InformMessageEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: qihenan
 * @Date: 2019/12/4 11:40
 * @Description:
 */
@Service
public class OrderServiceImpl implements OrderService {

    private Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

    @Autowired
    private MqDirectOrderSender mqDirectOrderSender;

    @Override
    public void createOrder(InformMessageEnum informMessageEnum,
        UserInfo loanCiPersonalInfor) {
        try {
            logger.info("userId为{}的用户创建订单，当前时间：{}", loanCiPersonalInfor.getUserId(), new Date());
            String messageContent = informMessageEnum.getMessage();
            mqDirectOrderSender.sendMessage(loanCiPersonalInfor.getUserId(), messageContent);
        } catch (Exception e) {
            logger.error("消息发送异常！", e);
        }
    }

    @Override
    public void payOrder(UserInfo loanCiPersonalInfor) {
        try {
            logger.info("userId为{}的用户付款成功，当前时间：{}", loanCiPersonalInfor.getUserId(), new Date());
            //根据OrderCode消费掉此条消息，删除支付成功的订单
        } catch (Exception e) {
            logger.error("付款异常！", e);
        }
    }

}
