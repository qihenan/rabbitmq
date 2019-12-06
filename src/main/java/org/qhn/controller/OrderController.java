package org.qhn.controller;

import io.swagger.annotations.ApiOperation;
import org.qhn.model.UserInfo;
import org.qhn.service.OrderService;
import org.qhn.utils.sms.InformMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: qihenan
 * @Date: 2019/10/29 11:03
 * @Description:
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation("创建订单")
    @GetMapping("/createOrder/{userId}")
    public void createOrder(@PathVariable("userId") Long userId) {
        try {
            UserInfo loanCiPersonalInfor = new UserInfo();
            loanCiPersonalInfor.setUserId(userId);
            orderService.createOrder(InformMessageEnum.ORDER_WAIT_PAY, loanCiPersonalInfor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("订单付款")
    @GetMapping("/pay/{userId}")
    public void pay(@PathVariable("userId") Long userId) {
        try {
            UserInfo loanCiPersonalInfor = new UserInfo();
            loanCiPersonalInfor.setUserId(userId);
            orderService.payOrder(loanCiPersonalInfor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
