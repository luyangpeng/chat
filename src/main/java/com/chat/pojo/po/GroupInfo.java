package com.chat.pojo.po;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "group_info")
public class GroupInfo {
    @Id
    private Long id;

    /**
     * 群名
     */
    @NotBlank(message = "群名称不能为空")
    @ApiModelProperty(value="群名",example = "测试群组",required = true)
    private String name;

    /**
     * 界面显示的群名称
     */
    @ApiModelProperty(value="显示名",example = "测试群组0-0|0包/1.0")
    @Column(name = "`display_name`")
    private String displayName;

    /**
     * 群公告
     */
    @NotBlank(message = "群公告不能为空")
    @ApiModelProperty(value="群公告",example = "欢迎大家加入测试群组",required = true)
    private String announcement;

    /**
     * 红包有效时间（单位：分钟）
     */
    @NotBlank(message = "红包有效时间不能为空")
    @Min(value = 1,message = "请输入正确的有效时间")
    @ApiModelProperty(value="红包有效时间（单位：分钟）",example = "10",required = true)
    private Integer validity;

    /**
     * 最小红包
     */
    @Column(name = "`min_ red_envelope`")
    private Integer minRedEnvelope;

    /**
     * 最大红包
     */
    @Column(name = "`max_ red_envelope`")
    private Integer maxRedEnvelope;

    /**
     * 红包数量
     */
    @Column(name = "red_envelope_num")
    private Integer redEnvelopeNum;

    /**
     * 红包倍率
     */
    @Column(name = "red_envelope_rate")
    private BigDecimal redEnvelopeRate;

    /**
     * 群主ID
     */
    @Column(name = "owner_id")
    private Long ownerId;

    /**
     * 群聊创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 群组过期时间
     */
    @Column(name = "expire_time")
    private Date expireTime;
    /**
     * 群组是否已关闭
     */
    @Column(name = "is_close")
    private Boolean close;
}