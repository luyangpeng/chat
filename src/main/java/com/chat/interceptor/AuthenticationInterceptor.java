package com.chat.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.chat.annotation.AuthToken;
import com.chat.pojo.po.UserInfo;
import com.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ZhouHua
 * @date 2019/11/4
 */
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object header) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(header instanceof HandlerMethod)) {
            return true;
        }
        // 从 http 请求头中取出 token
        String token = request.getHeader("token");
        HandlerMethod handlerMethod = (HandlerMethod) header;
        //检查有没有需要用户权限的注解
        if(handlerMethod.getBeanType().isAnnotationPresent(AuthToken.class)){
            AuthToken authToken = handlerMethod.getBeanType().getAnnotation(AuthToken.class);
            if(authToken.required()){
                // 执行认证
                if (token == null) {
                    throw new RuntimeException("请先登录");
                }
                // 获取 token 中的 user id
                Long userId;
                try {
                    userId = Long.valueOf(JWT.decode(token).getAudience().get(0));
                } catch (JWTDecodeException j) {
                    throw new RuntimeException("token解析失败");
                }
                UserInfo user = userService.queryUserById(userId);
                if (user == null) {
                    throw new RuntimeException("用户不存在，请重新登录");
                }
                // 验证 token
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
                try {
                    jwtVerifier.verify(token);
                } catch (JWTVerificationException e) {
                    throw new RuntimeException("token验证失败，请重新登录");
                }
                request.setAttribute("user",user);
                return true;
            }
        }
        return true;
    }
}
