package com.coding.sales.entity;

import com.coding.sales.input.OrderItemCommand;

import java.math.BigDecimal;

/**
 * 优惠活动接口
 * 拥有2个实现满减 折扣
 */
public interface Discount {
    /**
     * 获取活动优惠金额
     * @param orderItemCommand
     * @return
     */
    BigDecimal getAmount(OrderItemCommand orderItemCommand);

    /**
     * 获取活动描述
     * @return
     */
    String getDesc ();
}
