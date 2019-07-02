package com.coding.sales.entity;

import com.coding.sales.input.OrderItemCommand;

import java.math.BigDecimal;

public interface Discount {
    /**
     * 获取活动优惠金额
     * @param orderItemCommand
     * @return
     */
    BigDecimal getAmount(OrderItemCommand orderItemCommand);
    String getDesc ();
}
