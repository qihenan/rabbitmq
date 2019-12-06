package org.qhn.controller;

import org.qhn.model.UserInfo;
import org.qhn.service.SmsService;
import org.qhn.utils.sms.InformMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: qihenan
 * @Date: 2019/10/29 11:03
 * @Description:
 */
@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @GetMapping("/send")
    public void sendMsg() {
        try {
            //发送短信
            UserInfo loanCiPersonalInfor = new UserInfo();
            loanCiPersonalInfor.setUserId(1L);
            loanCiPersonalInfor.setMobile("18410170565");
            smsService.sendNote(InformMessageEnum.LOGIN_SMS_CODE, loanCiPersonalInfor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
