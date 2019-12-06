package org.qhn.utils.sms;

/**
 * description: 短信模板code枚举
 *
 * @author ren
 * @date 2018/7/9 15:42
 */
public enum InformMessageEnum {

    /**
     * 登录短信验证码
     */
    LOGIN_SMS_CODE("0001", "您好，你的登录短信验证码是123456", "登录短信验证码"),

    /**
     * 下单成功待付款
     */
    ORDER_WAIT_PAY("0002", "您好，您已下单成功，请尽快支付，10分钟后将取消订单", "下单成功待付款");

    private String code;

    private String message;

    private String title;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "InformMessageEnum{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    InformMessageEnum(String code, String message, String title) {
        this.code = code;
        this.message = message;
        this.title = title;
    }

    public InformMessageEnum getByTitle(String title) {
        for (InformMessageEnum informMessageEnum : InformMessageEnum.values()) {
            if (informMessageEnum.getTitle().equals(title)) {
                return informMessageEnum;
            }
        }
        return null;
    }
}
