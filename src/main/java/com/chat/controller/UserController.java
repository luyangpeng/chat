package com.chat.controller;

import com.chat.annotation.AuthToken;
import com.chat.pojo.bo.UserBO;
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
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;

@AuthToken
@Api(value="用户controller",tags="用户控制器")
@RestController
@RequestMapping("user")
public class UserController {
	
	@Autowired
	private UserService userService;

	@Value("${avatar.url}")
    private String avatarUrl;

    @Value("${avatar.dir}")
    private String avatarDir;

    @PostMapping("update")
    @ApiOperation(value="修改用户信息", notes="修改昵称、性别、签名等用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true,paramType = "header", dataType = "string"),
            @ApiImplicitParam(name = "user", value = "新的用户信息", required = true,paramType = "body", dataType = "UserBO")
    })
    public JSONResult update(@RequestBody UserBO user,HttpServletRequest request){
        UserInfo userInfo = (UserInfo)request.getAttribute("user");
        if(StringUtils.isNotBlank(user.getNickname())){
            userInfo.setNickname(user.getNickname());
        }
        if(user.getSex()!=null){
            userInfo.setSex(user.getSex());
        }
        if(StringUtils.isNotBlank(user.getSignature())){
            userInfo.setSignature(user.getSignature());
        }
        if(!userService.updateUserInfo(userInfo)){
            return JSONResult.errorMsg("修改失败");
        }
        UserVO userVO=new UserVO();
        BeanUtils.copyProperties(userInfo,userVO);
        userVO.setToken(TokenUtil.getToken(userInfo));
        return JSONResult.ok(userVO,"修改成功");
    }


    @ApiOperation(value="修改头像", notes="修改用户头像")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true,paramType = "header", dataType = "string"),
            @ApiImplicitParam(name = "image", value = "新头像", required = true,paramType = "form", dataType = "file")
    })
    @PostMapping(value = "uploadAvatar")
    public JSONResult uploadImage(@NotNull(message = "上传的图片不能为空") MultipartFile image,HttpServletRequest request){
        if(!image.getContentType().contains("image")){
            return JSONResult.errorMsg("文件格式错误");
        }
        //上传文件
        String path = System.currentTimeMillis()+image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
        try {
            FileUtils.writeByteArrayToFile(new File(avatarDir,path),image.getBytes());
        } catch (IOException e) {
            return JSONResult.errorMsg("修改失败");
        }
        String url = avatarUrl+path;
        UserInfo userInfo = (UserInfo)request.getAttribute("user");
        userInfo.setAvatar(url);
        if(!userService.updateUserInfo(userInfo)){
            return JSONResult.errorMsg("修改失败");
        }
        UserVO userVO=new UserVO();
        BeanUtils.copyProperties(userInfo,userVO);
        userVO.setToken(TokenUtil.getToken(userInfo));
        return JSONResult.ok(userVO,"修改成功");
    }

    @PostMapping("updatePassword")
    @ApiOperation(value="修改登录密码", notes="修改登录密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true,paramType = "header", dataType = "string"),
            @ApiImplicitParam(name = "mobile", value = "手机号", required = true,paramType = "query", dataType = "string",defaultValue = "13579246810"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true,paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "password", value = "登录密码", required = true,paramType = "query", dataType = "string",defaultValue = "123456")
    })
    public JSONResult updatePassword(@NotBlank(message = "手机号不能为空")
                                     @Length(min = 11,max = 11,message = "手机号应为11位数")String mobile,
                                     @NotBlank(message = "验证码不能为空")
                                     @Length(min = 6,max = 6,message = "验证码应为6位数")String code,
                                     @NotBlank(message = "密码不能为空")
                                         @Length(min = 6,message = "密码不能少于6位") String password,
                                   HttpServletRequest request){
        UserInfo userInfo = (UserInfo)request.getAttribute("user");
        if(StringUtils.isBlank(userInfo.getMobile())){
            return JSONResult.errorMsg("请先绑定手机号");
        }
        if(!mobile.startsWith("1")||!NumberUtils.isNumber(mobile)){
            return JSONResult.errorMsg("请输入正确的手机号码");
        }
        boolean auth=VerificationCodeUtil.authVerificationCode(mobile,code);
        if(!auth){
            return JSONResult.errorMsg("验证码输入错误");
        }
        userInfo.setPassword(password);
        if(!userService.updateUserInfo(userInfo)){
            return JSONResult.errorMsg("修改失败");
        }
        UserVO userVO=new UserVO();
        BeanUtils.copyProperties(userInfo,userVO);
        userVO.setToken(TokenUtil.getToken(userInfo));
        return JSONResult.ok(userVO,"修改成功");
    }

    @PostMapping("updateMobile")
    @ApiOperation(value="修改手机号", notes="修改手机号码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true,paramType = "header", dataType = "string"),
            @ApiImplicitParam(name = "mobile", value = "新手机号", required = true,paramType = "query", dataType = "string",defaultValue = "13579246810"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true,paramType = "query", dataType = "string")
    })
    public JSONResult updateMobile(@NotBlank(message = "手机号不能为空")
                                   @Length(min = 11,max = 11,message = "手机号应为11位数")String mobile,
                                   @NotBlank(message = "验证码不能为空")
                                   @Length(min = 6,max = 6,message = "验证码应为6位数")String code,
                                   HttpServletRequest request){
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
        userInfo = (UserInfo)request.getAttribute("user");
        userInfo.setMobile(mobile);
        if(!userService.updateUserInfo(userInfo)){
            return JSONResult.errorMsg("修改失败");
        }
        UserVO userVO=new UserVO();
        BeanUtils.copyProperties(userInfo,userVO);
        userVO.setToken(TokenUtil.getToken(userInfo));
        return JSONResult.ok(userVO,"修改成功");
    }

    @PostMapping("updatePaymentCode")
    @ApiOperation(value="修改支付密码", notes="修改支付密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true,paramType = "header", dataType = "string"),
            @ApiImplicitParam(name = "mobile", value = "手机号", required = true,paramType = "query", dataType = "string",defaultValue = "13579246810"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true,paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "paymentCode", value = "支付密码", required = true,paramType = "query", dataType = "int",defaultValue = "123456")
    })
    public JSONResult updatePaymentCode(@NotBlank(message = "手机号不能为空")
                                     @Length(min = 11,max = 11,message = "手机号应为11位数")String mobile,
                                     @NotBlank(message = "验证码不能为空")
                                     @Length(min = 6,max = 6,message = "验证码应为6位数")String code,
                                     @NotNull(message = "支付密码不能为空")
                                     @Range(min = 100000,max = 999999,message = "支付密码应为6位数") Integer paymentCode,
                                     HttpServletRequest request){
        UserInfo userInfo = (UserInfo)request.getAttribute("user");
        if(StringUtils.isBlank(userInfo.getMobile())){
            return JSONResult.errorMsg("请先绑定手机号");
        }
        if(!mobile.startsWith("1")||!NumberUtils.isNumber(mobile)){
            return JSONResult.errorMsg("请输入正确的手机号码");
        }
        boolean auth=VerificationCodeUtil.authVerificationCode(mobile,code);
        if(!auth){
            return JSONResult.errorMsg("验证码输入错误");
        }
        userInfo.setPaymentCode(paymentCode);
        if(!userService.updateUserInfo(userInfo)){
            return JSONResult.errorMsg("修改失败");
        }
        UserVO userVO=new UserVO();
        BeanUtils.copyProperties(userInfo,userVO);
        userVO.setToken(TokenUtil.getToken(userInfo));
        return JSONResult.ok(userVO,"修改成功");
    }

    @PostMapping("donateDiamond")
    @ApiOperation(value="赠送钻石", notes="给其他用户赠送钻石")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true,paramType = "header", dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "领取人ID", required = true,paramType = "query", dataType = "long",defaultValue = "1"),
            @ApiImplicitParam(name = "amount", value = "赠送数量", required = true,paramType = "query", dataType = "int",defaultValue = "100")
    })
    public JSONResult donateDiamond(@NotBlank(message = "请选择赠送人") Long userId,
                                    @NotBlank(message = "请输入赠送数量")
                                        @Min(value = 0,message = "赠送钻石数量必须大于0") Integer amount,
                                        HttpServletRequest request){
        UserInfo userInfo = (UserInfo)request.getAttribute("user");
        if(userInfo.getDiamond()<amount){
            return JSONResult.errorMsg("您的钻石不足");
        }
        UserInfo receiverUser = userService.queryUserById(userId);
        if(receiverUser==null){
            return JSONResult.errorMsg("用户不存在");
        }
        boolean succ=userService.donateDiamond(userInfo,receiverUser,amount);
        if(!succ){
            return JSONResult.errorMsg("赠送失败");
        }
        UserVO userVO=new UserVO();
        BeanUtils.copyProperties(userInfo,userVO);
        userVO.setToken(TokenUtil.getToken(userInfo));
        return JSONResult.ok(userVO,"赠送成功");
    }
}
