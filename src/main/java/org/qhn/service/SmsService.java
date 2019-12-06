package org.qhn.service;

import java.util.List;
import org.qhn.model.UserInfo;
import org.qhn.utils.sms.InformMessageEnum;

/**
 * 短信发送服务
 *
 * @author wwh
 * @date 2018/11/23
 */
public interface SmsService {

    /**
     * 发送短信 带参数列表
     *
     * @param message
     * @param sMSMessageCode
     * @param loanCiPersonalInfor
     * @author wwh
     * @date 2018/11/23
     */
    boolean sendNote(List<Object> message, InformMessageEnum sMSMessageCode,
        UserInfo loanCiPersonalInfor);

    /**
     * 发送短信
     * 功能描述
     *
     * @param sMSMessageCode
     * @param loanCiPersonalInfor
     * @author wwh
     * @date 2018/11/23
     */
    boolean sendNote(InformMessageEnum sMSMessageCode,
        UserInfo loanCiPersonalInfor);
}
