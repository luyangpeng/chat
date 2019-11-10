package com.chat.pojo.po;

import javax.persistence.*;

@Table(name = "group_member")
public class GroupMember {
    @Id
    private Long id;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 群组ID
     */
    @Column(name = "group_id")
    private Long groupId;

    /**
     * 群昵称
     */
    @Column(name = "user_group_name")
    private String userGroupName;

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
     * 获取用户ID
     *
     * @return user_id - 用户ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
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
     * 获取群昵称
     *
     * @return user_group_name - 群昵称
     */
    public String getUserGroupName() {
        return userGroupName;
    }

    /**
     * 设置群昵称
     *
     * @param userGroupName 群昵称
     */
    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
    }
}