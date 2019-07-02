package com.coding.sales.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class User {
    private String name;
    private String level;
    private String cardNo;
    private int integral;

    public User(String name, String level, String cardNo, int integral) {
        this.name = name;
        this.level = level;
        this.cardNo = cardNo;
        this.integral = integral;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public int updateIntegral(BigDecimal amount) {
        int newIntegral = integral;
        if(integral < 10000) {
            newIntegral = newIntegral + amount.setScale(0, RoundingMode.FLOOR).intValue();
        } else if(integral < 50000) {
            newIntegral = newIntegral + amount.multiply(new BigDecimal(1.5)).setScale(0, RoundingMode.FLOOR).intValue();
        } else if(integral < 100000){
            newIntegral = newIntegral + amount.multiply(new BigDecimal(1.8)).setScale(0, RoundingMode.FLOOR).intValue();
        } else {
            newIntegral = newIntegral + amount.multiply(new BigDecimal(2)).setScale(0, RoundingMode.FLOOR).intValue();
        }
        int add = newIntegral - integral;
        this.integral = newIntegral;
        updateLevel(newIntegral);
        return add;
    }

    private void updateLevel(int newIntegral) {
        if(newIntegral < 10000) {
            this.level = "普卡";
        } else if(newIntegral < 50000) {
            this.level = "金卡";
        } else if(newIntegral < 100000){
            this.level = "白金卡";
        } else {
            this.level = "钻石卡";
        }
    }
}
