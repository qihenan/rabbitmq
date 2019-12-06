package org.qhn.service;

import org.qhn.model.UserInfo;
import org.qhn.utils.sms.InformMessageEnum;

/**
 * @Auther: qihenan
 * @Date: 2019/12/4 11:40
 * @Description:
 */
public interface OrderService {

    void createOrder(InformMessageEnum informMessageEnum,
        UserInfo loanCiPersonalInfor);

    void payOrder(UserInfo loanCiPersonalInfor);

}
