package org.qhn.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * <p>
 * ci_个人用户基础信息表
 * </p>
 *
 * @author sp
 * @since 2018-06-26
 */
@Data
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户手机号码
     */
    private String mobile;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 密码类型
     */
    private Integer pwtype;
    /**
     * 证件号码
     */
    private String cardId;
    /**
     * 证件类型 1身份证
     */
    private Integer cardType;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 学历 1：在读 2已毕业
     */
    private Integer degree;
    /**
     * 家庭电话
     */
    private String homePhone;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * GPS定位信息
     */
    private String gps;
    /**
     * 婚姻状态
     */
    private Integer isMarriaged;
    /**
     * 民族
     */
    private String userNative;
    /**
     * 芝麻订单号
     */
    private String zhimaOrderNo;
    /**
     * 芝麻分
     */
    private String zhimaScore;
    /**
     * 住址
     */
    private String address;
    /**
     * 性别 1男 2女
     */
    private Integer sex;
    /**
     * 添加时间
     */
    private Date addTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 个人信息更新时间
     */
    private Date infoUpdateTime;
    /**
     * 个人推荐码
     */
    private String precommendedCode;
    /**
     * 微信推荐码
     */
    private String wxcommendedUrl;
    /**
     * 企业推荐码
     */
    private String crecommendedCode;
    /**
     * 微信被推荐码
     */
    private String wxcommendUrl;
    /**
     * 用户应用状态  0初始化 1基础信息未完善 2认证未完善 3认证审核中 4认证审核失败 5黑名单 6灰名单 7审核通过
     */
    private Integer applicationState;
    /**
     * 用户状态  1 有效 0 无效
     */
    private Integer crmState;
    /**
     * 个人被推荐码
     */
    private String precommendCode;
    /**
     * 企业被推荐码
     */
    private String crecommendCode;
    /**
     * 加密随机码
     */
    private String randomCode;
    /**
     * 客户端唯一标识
     */
    private String deviceId;
    /**
     * 用户来源标识
     */
    private String sourceId;
    /**
     * 应用版本
     */
    private String softType;
    /**
     * 版本
     */
    private String clientVersion;
    /**
     * ext_5
     */
    private String netJoinDate;
    /**
     * ext_1
     */
    private String userServiceProtocol;

}
