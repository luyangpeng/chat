package com.chat.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.chat.pojo.po.UserInfo;

/**
 * @author ZhouHua
 * @date 2019/11/4
 */
public class TokenUtil {
    public static String getToken(UserInfo user) {
        return JWT.create().withAudience(user.getId().toString())
                .sign(Algorithm.HMAC256(user.getPassword()));
    }
}
