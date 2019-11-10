package com.chat.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author ZhouHua
 */
@Data
@Builder
@ApiModel(description= "返回响应数据")
public class JSONResult {

    @ApiModelProperty(value = "是否成功",example = "true")
    private boolean success=true;
    @ApiModelProperty(value = "返回对象")
    private Object data;
    @ApiModelProperty(value = "错误编号")
    private Integer errCode;
    @ApiModelProperty(value = "错误信息")
    private String msg;

    public static JSONResult ok(Object data) {
        return JSONResult.builder().success(true).data(data).build();
    }

    public static JSONResult ok(Object data,String msg) {
        return JSONResult.builder().success(true).data(data).msg(msg).build();
    }

    public static JSONResult ok() {
        return JSONResult.builder().success(true).build();
    }
    
    public static JSONResult errorMsg(String msg) {
        return JSONResult.builder().success(false).errCode(0).msg(msg).build();
    }

    public static JSONResult errorMsg(Integer errCode,String msg) {
        return JSONResult.builder().success(false).errCode(errCode).msg(msg).build();
    }
}
