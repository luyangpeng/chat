package com.chat.pojo.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserBO {
    @ApiModelProperty(value="昵称",example = "测试账号")
    private String nickname;
    @ApiModelProperty(value="性别",example = "1")
    private Integer sex;
    @ApiModelProperty(value="个性签名",example = "这是我的签名")
    private String signature;
}
