package com.coding.sales.entity;

import com.coding.sales.input.OrderItemCommand;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 贵金属实体对象
 */
public class PreciousMetal {
    /**
     * 商品编号
     */
    private String productNo;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 单位
     */
    private String unit;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 支持活动列表
     */
    private List<Discount> dicountList;

    public PreciousMetal (String productNo, String productName, String unit, BigDecimal price) {
        this.productNo = productNo;
        this.productName = productName;
        this.unit = unit;
        this.price = price;
    }
    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<Discount> getDicountList() {
        return dicountList;
    }

    public void setDicountList(List<Discount> dicountList) {
        this.dicountList = dicountList;
    }

    /**
     * 增加支持优惠 初始化贵金属产品容器使用
     * @param dicount
     */
    public void addDicount(Discount dicount) {
        if(dicountList == null) {
            dicountList = new ArrayList<Discount>();
        }
        this.dicountList.add(dicount);
    }

    /**
     * 获取当前商品最大幅度优惠
     * @param orderItemCommand
     * @param discounts
     * @return
     */
    public Discount getBestDiscount(OrderItemCommand orderItemCommand, List<String> discounts) {
        if(dicountList != null && dicountList.size() > 0) {
            BigDecimal reduceAmount = BigDecimal.ZERO;
            Discount bestDiscount = null;
            for (int i = 0; i < dicountList.size(); i++) {
                Discount discount = dicountList.get(i);
                if(discount instanceof ZkDiscount) {
                    boolean ishave = false;
                    for (int j = 0; j < discounts.size(); j++) {
                        String discountStr = discounts.get(j);
                        if(discountStr.equals(discount.getDesc())) {
                            ishave = true;
                        }
                    }
                    if(!ishave) {
                        continue;
                    }
                }
                BigDecimal amount  = discount.getAmount(orderItemCommand);
                if(amount.compareTo(reduceAmount) > 0) {
                    bestDiscount = discount;
                    reduceAmount = amount;
                }
            }
            return bestDiscount;
        }
        return null;
    }
}
