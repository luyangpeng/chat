package com.chat.pojo;

import lombok.Data;

import java.util.Date;

/**
 * 手机验证码
 * @author ZhouHua
 * @date 2019/11/9
 */
@Data
public class VerificationCode {
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 验证码
     */
    private String code;
    /**
     * 失效时间
     */
    private Date expireTime;
}
