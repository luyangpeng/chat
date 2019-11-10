package com.chat.service;

import com.chat.mapper.GroupInfoMapper;
import com.chat.pojo.po.GroupInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

    @Autowired
    private GroupInfoMapper groupInfoMapper;

    /**
     * 获得群组的展示名
     * @param groupInfo
     * @return
     */
    public String getGroupDisplayName(GroupInfo groupInfo){
        StringBuilder displayName = new StringBuilder();
        displayName.append(groupInfo.getName());
        displayName.append(groupInfo.getMinRedEnvelope());
        displayName.append("-");
        displayName.append(groupInfo.getMaxRedEnvelope());
        displayName.append("|");
        displayName.append(groupInfo.getRedEnvelopeNum());
        displayName.append("包");
        displayName.append(groupInfo.getRedEnvelopeRate());
        return displayName.toString();
    }
    /**
     * 保存群组信息
     * @param groupInfo
     * @return
     */
    public boolean saveGroup(GroupInfo groupInfo) {
        return groupInfoMapper.insert(groupInfo)>0;
    }
}
