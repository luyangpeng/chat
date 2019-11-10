package com.chat.pojo.po;

import javax.persistence.*;

@Table(name = "group_apply")
public class GroupApply {
    @Id
    private Long id;

    /**
     * 申请人
     */
    @Column(name = "apply_id")
    private Long applyId;

    /**
     * 审核者ID
     */
    @Column(name = "check_id")
    private Long checkId;

    /**
     * 推荐码
     */
    @Column(name = "referral_code")
    private String referralCode;

    /**
     * 申请状态 0：未处理 1：审核通过 2：拒绝
     */
    private Integer status;
}