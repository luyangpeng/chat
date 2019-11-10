package com.chat.controller;

import com.chat.pojo.po.UserInfo;
import com.chat.pojo.vo.UserVO;
import com.chat.service.UserService;
import com.chat.utils.JSONResult;
import com.chat.utils.TokenUtil;
import com.chat.utils.VerificationCodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Api(value="通用controller",tags="通用控制器")
@RestController
@Validated
public class IndexController {
	
	@Autowired
	private UserService userService;

    @Value("${avatar.default}")
    private String defaultAvatar;



    @ApiOperation(value="用户登录", notes="根据账号密码进行登录，账号可以是用户名也可以是手机号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名/手机号", required = true, dataType = "string",paramType = "query",defaultValue = "13579246810"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "string",paramType = "query",defaultValue = "123456")
    })
	@PostMapping("login")
	public JSONResult login(@NotBlank(message = "用户名不能为空") String username,
                            @NotBlank(message = "密码不能为空")
                            @Length(min = 6,message = "密码不能少于6位") String password) {
        UserInfo userInfo=userService.queryUserForLogin(username,password);
        if(userInfo==null){
            return JSONResult.errorMsg("用户名或密码错误");
        }else{
            Date currDate=new Date();
            userInfo.setLastLoginTime(currDate);
            userService.updateUserInfo(userInfo);
        }
        UserVO userVO=new UserVO();
        BeanUtils.copyProperties(userInfo,userVO);
        userVO.setToken(TokenUtil.getToken(userInfo));
        return JSONResult.ok(userVO);
	}

    @ApiOperation(value="微信登录", notes="根据微信返回的用户信息进行登录")
    @ApiImplicitParam(name = "user", value = "用户实体user", required = true, dataType = "UserInfo")
    @PostMapping("wxLogin")
    public JSONResult wxLogin(@RequestBody @Valid UserInfo user){
        UserInfo userInfo = null;
        if(StringUtils.isNotBlank(user.getUnionid())){
            userInfo=userService.queryUserByUnionid(user.getUnionid());
        }
        if(userInfo==null){
            userInfo=userService.queryUserByOpenid(user.getOpenid());
        }
        if(userInfo==null){
            String username = userService.generateUsername();
            user.setUsername(username);
            user.setPassword("123456");
            user.setDiamond(0);
            user.setPaymentCode(123456);
            Date currDate=new Date();
            user.setCreateTime(currDate);
            user.setLastLoginTime(currDate);
            if(userService.saveUser(user)){
                userInfo=userService.queryUserByOpenid(user.getOpenid());
            }
        }else{
            Date currDate=new Date();
            user.setLastLoginTime(currDate);
            userService.updateUserInfo(user);
        }
        if(userInfo==null){
            return JSONResult.errorMsg("登录失败");
        }
        UserVO userVO=new UserVO();
        BeanUtils.copyProperties(userInfo,userVO);
        userVO.setToken(TokenUtil.getToken(userInfo));
        return JSONResult.ok(userVO,"登录成功");
    }

    @ApiOperation(value="用户注册", notes="利用手机号注册用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "string",paramType = "query",defaultValue = "13579246810"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "string",paramType = "query",defaultValue = "123456")
    })
    @PostMapping("register")
    public JSONResult register(@NotBlank(message = "手机号不能为空")
                                   @Length(min = 11,max = 11,message = "手机号应为11位数")String mobile,
                               @NotBlank(message = "验证码不能为空")
                               @Length(min = 6,max = 6,message = "验证码应为6位数")String code,
                            @NotBlank(message = "密码不能为空")
                            @Length(min = 6,message = "密码不能少于6位") String password) {
        if(!mobile.startsWith("1")||!NumberUtils.isNumber(mobile)){
            return JSONResult.errorMsg("请输入正确的手机号码");
        }
        UserInfo userInfo=userService.queryUserByMobile(mobile);
        if(userInfo!=null){
            return JSONResult.errorMsg("手机号已被注册");
        }
        boolean auth=VerificationCodeUtil.authVerificationCode(mobile,code);
        if(!auth){
            return JSONResult.errorMsg("验证码输入错误");
        }
        UserInfo user = new UserInfo();
        String username = userService.generateUsername();
        user.setUsername(username);
        user.setPassword(password);
        user.setMobile(mobile);
        user.setNickname(mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
        user.setAvatar(defaultAvatar);
        user.setDiamond(0);
        user.setPaymentCode(123456);
        user.setSex(0);
        Date currDate=new Date();
        user.setCreateTime(currDate);
        user.setLastLoginTime(currDate);
        if(userService.saveUser(user)){
            userInfo=userService.queryUserByMobile(mobile);
            UserVO userVO=new UserVO();
            BeanUtils.copyProperties(userInfo,userVO);
            userVO.setToken(TokenUtil.getToken(userInfo));
            return JSONResult.ok(userVO,"注册成功");
        }else{
            return JSONResult.errorMsg("注册失败");
        }
    }

    @PostMapping("sendCode")
    @ApiOperation(value="发送验证码", notes="发送手机验证码")
    @ApiImplicitParam(name = "moblie", value = "手机号", required = true,paramType = "query", dataType = "string",defaultValue = "13579246810")
    public JSONResult sendVerificationCode(String moblie){
        String code = userService.generateVerificationCode(moblie);
        return JSONResult.ok("您好，您此次验证码为"+code+"，请不要把验证码泄露给其他人！（有效期5分钟）","验证码发送成功");
    }

    @ApiOperation(value="搜索用户", notes="用于砖石赠送时搜索用户")
    @ApiImplicitParam(name = "keyword", value = "关键字", required = true,paramType = "query", dataType = "string")
    @PostMapping(value = "searchUser")
    public JSONResult searchUser(@NotBlank(message = "关键字不能为空") String keyword){
        List<UserInfo> userInfoList = userService.queryUsersForSearch(keyword);
        List<Map<String,Object>> list = new ArrayList<>();
        for (UserInfo userInfo : userInfoList) {
            Map<String,Object> map=new HashMap<>();
            map.put("id",userInfo.getId());
            map.put("username",userInfo.getUsername());
            map.put("nickname",userInfo.getNickname());
            map.put("avatar",userInfo.getAvatar());
            list.add(map);
        }
        return JSONResult.ok(list);
    }
}
