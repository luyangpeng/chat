package com.chat.pojo.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class GroupBO {
    /**
     * 群名
     */
    @NotBlank(message = "群名称不能为空")
    @ApiModelProperty(value="群名",example = "测试群组",required = true)
    private String name;

    /**
     * 群公告
     */
    @NotBlank(message = "群公告不能为空")
    @ApiModelProperty(value="群公告",example = "欢迎大家加入测试群组",required = true)
    private String announcement;

    /**
     * 红包有效时间（单位：分钟）
     */
    @NotNull(message = "红包有效时间不能为空")
    @Min(value = 1,message = "请输入正确的有效时间")
    @ApiModelProperty(value="红包有效时间（单位：分钟）",example = "10",required = true)
    private Integer validity;
}