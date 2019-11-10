package com.chat.pojo.po;

import java.util.Date;
import javax.persistence.*;

@Table(name = "group_msg")
public class GroupMsg {
    @Id
    private Long id;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 群组ID
     */
    @Column(name = "group_id")
    private Long groupId;

    /**
     * 消息类型 0：红包 1：文字 2：图片
     */
    @Column(name = "msg_type")
    private Boolean msgType;

    /**
     * 消息发送时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取消息内容
     *
     * @return content - 消息内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置消息内容
     *
     * @param content 消息内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取群组ID
     *
     * @return group_id - 群组ID
     */
    public Long getGroupId() {
        return groupId;
    }

    /**
     * 设置群组ID
     *
     * @param groupId 群组ID
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    /**
     * 获取消息类型 0：红包 1：文字 2：图片
     *
     * @return msg_type - 消息类型 0：红包 1：文字 2：图片
     */
    public Boolean getMsgType() {
        return msgType;
    }

    /**
     * 设置消息类型 0：红包 1：文字 2：图片
     *
     * @param msgType 消息类型 0：红包 1：文字 2：图片
     */
    public void setMsgType(Boolean msgType) {
        this.msgType = msgType;
    }

    /**
     * 获取消息发送时间
     *
     * @return create_time - 消息发送时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置消息发送时间
     *
     * @param createTime 消息发送时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}