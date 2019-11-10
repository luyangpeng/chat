package com.chat.pojo.vo;

import lombok.Data;

@Data
public class UserVO {
    /**
     * 用户ID
     */
    private Long id;
    /**
     * 用户token
     */
    private String token;
    /**
     * 账号
     */
    private String username;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 钻石数量
     */
    private Integer diamond;

    /**
     * 性别 0表示未知，1表示男性，2表示女性
     */
    private Integer sex;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 个性签名
     */
    private String signature;
}