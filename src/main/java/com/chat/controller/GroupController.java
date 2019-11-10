package com.chat.controller;

import com.chat.pojo.po.GroupInfo;
import com.chat.pojo.po.UserInfo;
import com.chat.pojo.bo.GroupBO;
import com.chat.service.GroupService;
import com.chat.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;

@Api(value="群组controller",tags="群组控制器")
@RestController
@RequestMapping("group")
public class GroupController {
	
	@Autowired
	private GroupService groupService;

    @PostMapping("create")
    @ApiOperation(value="创建群组", notes="创建一个群组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "group", value = "群组信息", required = true,paramType = "body", dataType = "GroupBO")
    })
    public JSONResult create(@RequestBody @Valid GroupBO group, HttpServletRequest request){
        UserInfo userInfo = (UserInfo)request.getAttribute("user");
        GroupInfo groupInfo=new GroupInfo();
        groupInfo.setName(group.getName());
        groupInfo.setOwnerId(userInfo.getId());
        groupInfo.setAnnouncement(group.getAnnouncement());
        groupInfo.setClose(true);
        Date currDate=new Date();
        groupInfo.setCreateTime(currDate);
        groupInfo.setExpireTime(currDate);
        groupInfo.setValidity(0);
        groupInfo.setRedEnvelopeNum(0);
        groupInfo.setMinRedEnvelope(0);
        groupInfo.setMaxRedEnvelope(0);
        groupInfo.setRedEnvelopeRate(BigDecimal.valueOf(1.0));
        String displayName = groupService.getGroupDisplayName(groupInfo);
        groupInfo.setDisplayName(displayName);
        return groupService.saveGroup(groupInfo)?JSONResult.ok():JSONResult.errorMsg("创建失败");
    }
}
