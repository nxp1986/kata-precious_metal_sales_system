package com.coding.sales.entity;

import com.coding.sales.input.OrderItemCommand;

import java.math.BigDecimal;

public class MjDiscount implements Discount{
    private BigDecimal amount;
    private BigDecimal subtractAmount;
    private String desc;
    public MjDiscount(BigDecimal amount, BigDecimal subtractAmount, String desc) {
        this.amount = amount;
        this.subtractAmount = subtractAmount;
        this.desc = desc;
    }
    @Override
    public BigDecimal getAmount(OrderItemCommand orderItemCommand) {
        PreciousMetal preciousMetal = ProductsStore.getPreciousMetalByNo(orderItemCommand.getProduct());
        BigDecimal totalAmount = preciousMetal.getPrice().multiply(orderItemCommand.getAmount());
        if(totalAmount.compareTo(amount) >= 0) {
            return subtractAmount;
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
