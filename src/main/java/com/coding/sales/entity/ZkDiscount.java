package com.coding.sales.entity;

import com.coding.sales.input.OrderItemCommand;

import java.math.BigDecimal;

/**
 * 折扣优惠活动实现类
 */
public class ZkDiscount  implements Discount {
    /**
     * 折扣
     */
    private BigDecimal rebate;
    /**
     * 描述
     */
    private String desc;
    public ZkDiscount (BigDecimal rebate, String desc) {
        this.rebate = rebate;
        this.desc = desc;
    }
    @Override
    public BigDecimal getAmount(OrderItemCommand orderItemCommand) {
        PreciousMetal preciousMetal = ProductsStore.getPreciousMetalByNo(orderItemCommand.getProduct());
        BigDecimal reduceAmount = preciousMetal.getPrice().multiply(orderItemCommand.getAmount()).multiply(BigDecimal.ONE.subtract(rebate));
        return reduceAmount;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
