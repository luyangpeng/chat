package com.chat.pojo.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@ApiModel(description="用户对象UserInfo")
@Table(name = "user_info")
@Data
public class UserInfo {
    @Id
    @ApiModelProperty(hidden = true)
    private Long id;

    /**
     * 账号
     */
    @ApiModelProperty(hidden = true)
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty(hidden = true)
    private String password;

    /**
     * 头像
     */
    @NotBlank(message = "用户头像不能为空")
    @ApiModelProperty(value="头像",example = "http://thirdwx.qlogo.cn/mmopen/vi_32/gvxk7L4IuN7Hm1QnG3MicMCfwWm6qa8SNnzKXrRbeehutncyI0TWwM7QZW3Gicy2E6icB167ZibhLot5JMY0Xvomjw/132?1554462840",required = true)
    private String avatar;

    /**
     * 昵称
     */
    @NotBlank(message = "用户昵称不能为空")
    @ApiModelProperty(value="头像",example = "测试账号",required = true)
    private String nickname;

    /**
     * 钻石数量
     */
    @ApiModelProperty(hidden = true)
    private Integer diamond;

    /**
     * 性别 0表示未知，1表示男性，2表示女性
     */
    @Range(min = 0,max = 2,message = "性别错误")
    @ApiModelProperty(value="性别",example = "1")
    private Integer sex;

    /**
     * 手机号
     */
    @ApiModelProperty(hidden = true)
    private String mobile;

    /**
     * 个性签名
     */
    @ApiModelProperty(hidden = true)
    private String signature;

    @NotBlank(message = "openid不能为空")
    @ApiModelProperty(value="微信openid",example = "oUCee0cPBURY6t0kqJsGwyLVmf3M",required = true)
    private String openid;

    @ApiModelProperty(value="微信unionid",example = "ofKqE0z6fTN6rRN0ctCimuHNaE0A")
    private String unionid;

    /**
     * 省份
     */
    @ApiModelProperty(value="省",example = "广东")
    private String province;

    /**
     * 城市
     */
    @ApiModelProperty(value="市",example = "深圳")
    private String city;

    /**
     * 支付密码
     */
    @ApiModelProperty(hidden = true)
    @Column(name = "payment_code")
    private Integer paymentCode;
    /**
     * 用户创建时间
     */
    @Column(name = "create_time")
    private Date createTime;
    /**
     * 最后登录时间
     */
    @Column(name = "last_login_time")
    private Date lastLoginTime;
}