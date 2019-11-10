package com.chat.pojo.po;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "red_envelope")
public class RedEnvelope {
    @Id
    private Long id;

    /**
     * 红包金额
     */
    private BigDecimal money;

    /**
     * 红包数量
     */
    private Integer number;

    /**
     * 雷号
     */
    @Column(name = "mine_num")
    private Integer mineNum;

    /**
     * 剩余金额
     */
    @Column(name = "remain_money")
    private Long remainMoney;

    /**
     * 剩余红包数
     */
    @Column(name = "remain_num")
    private Integer remainNum;

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
     * 获取红包金额
     *
     * @return money - 红包金额
     */
    public BigDecimal getMoney() {
        return money;
    }

    /**
     * 设置红包金额
     *
     * @param money 红包金额
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    /**
     * 获取红包数量
     *
     * @return number - 红包数量
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * 设置红包数量
     *
     * @param number 红包数量
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * 获取雷号
     *
     * @return mine_num - 雷号
     */
    public Integer getMineNum() {
        return mineNum;
    }

    /**
     * 设置雷号
     *
     * @param mineNum 雷号
     */
    public void setMineNum(Integer mineNum) {
        this.mineNum = mineNum;
    }

    /**
     * 获取剩余金额
     *
     * @return remain_money - 剩余金额
     */
    public Long getRemainMoney() {
        return remainMoney;
    }

    /**
     * 设置剩余金额
     *
     * @param remainMoney 剩余金额
     */
    public void setRemainMoney(Long remainMoney) {
        this.remainMoney = remainMoney;
    }

    /**
     * 获取剩余红包数
     *
     * @return remain_num - 剩余红包数
     */
    public Integer getRemainNum() {
        return remainNum;
    }

    /**
     * 设置剩余红包数
     *
     * @param remainNum 剩余红包数
     */
    public void setRemainNum(Integer remainNum) {
        this.remainNum = remainNum;
    }
}