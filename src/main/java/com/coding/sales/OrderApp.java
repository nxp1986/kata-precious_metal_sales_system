package com.coding.sales;

import com.coding.sales.entity.*;
import com.coding.sales.input.OrderCommand;
import com.coding.sales.input.OrderItemCommand;
import com.coding.sales.input.PaymentCommand;
import com.coding.sales.output.DiscountItemRepresentation;
import com.coding.sales.output.OrderItemRepresentation;
import com.coding.sales.output.OrderRepresentation;
import com.coding.sales.output.PaymentRepresentation;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 销售系统的主入口
 * 用于打印销售凭证
 */
public class OrderApp {

    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("参数不正确。参数1为销售订单的JSON文件名，参数2为待打印销售凭证的文本文件名.");
        }

        String jsonFileName = args[0];
        String txtFileName = args[1];

        String orderCommand = FileUtils.readFromFile(jsonFileName);
        OrderApp app = new OrderApp();
        String result = app.checkout(orderCommand);
        FileUtils.writeToFile(result, txtFileName);
    }

    public String checkout(String orderCommand) {
        OrderCommand command = OrderCommand.from(orderCommand);
        OrderRepresentation result = checkout(command);
        
        return result.toString();
    }

    OrderRepresentation checkout(OrderCommand command) {
        OrderRepresentation result = null;
        //TODO: 请完成需求指定的功能
        //1、计算用户实际支付金额
        List<OrderItemCommand> orderItemCommandList =  command.getItems();
        List<String> indiscounts = command.getDiscounts();
        List<PaymentCommand> inpayments = command.getPayments();
        //订单明细
        List<OrderItemRepresentation> orderItems = new ArrayList<OrderItemRepresentation>();
        //优惠明细
        List<DiscountItemRepresentation> discounts = new ArrayList<DiscountItemRepresentation>();
        //付款记录
        List<PaymentRepresentation> payments = new ArrayList<PaymentRepresentation>();
        //付款使用的打折券
        List<String> discountCards = new ArrayList<String>();
        //订单总金额
        BigDecimal totalPrice = BigDecimal.ZERO;
        //优惠总金额
        BigDecimal totalDiscountPrice = BigDecimal.ZERO;
        if(orderItemCommandList != null && orderItemCommandList.size() > 0) {
            for (int i = 0; i < orderItemCommandList.size(); i++) {
                OrderItemCommand orderItemCommand = orderItemCommandList.get(i);
                //获取商品信息
                PreciousMetal preciousMetal = ProductsStore.getPreciousMetalByNo(orderItemCommand.getProduct());
                //计算本条目商品总价
                BigDecimal itemPrice = preciousMetal.getPrice().multiply(orderItemCommand.getAmount());
                //累加订单总金额
                totalPrice = totalPrice.add(itemPrice);
                //生成订单明细条目及保存
                OrderItemRepresentation  orderItemRepresentation = new OrderItemRepresentation(preciousMetal.getProductNo(), preciousMetal.getProductName(), preciousMetal.getPrice(), orderItemCommand.getAmount(), itemPrice);
                orderItems.add(orderItemRepresentation);
                //获取当前条目享受最大幅度优惠的活动
                Discount bestDiscount = preciousMetal.getBestDiscount(orderItemCommand, indiscounts);
                if(bestDiscount != null) {
                    //获取优惠金额
                    BigDecimal discountPrice = bestDiscount.getAmount(orderItemCommand);
                    //生成优惠明细条目及保存
                    DiscountItemRepresentation discountItemRepresentation = new DiscountItemRepresentation(preciousMetal.getProductNo(), preciousMetal.getProductName(), discountPrice);
                    discounts.add(discountItemRepresentation);
                    //累计总优惠金额
                    totalDiscountPrice = totalDiscountPrice.add(discountPrice);
                    //计算用户订单中是否有打折券
                    if(bestDiscount instanceof ZkDiscount) {
                        discountCards.add(bestDiscount.getDesc());
                    }
                }
            }
        }
        //生成付款方式清单
        if(inpayments!=null && inpayments.size() > 0) {
            for (int i = 0; i < inpayments.size(); i++) {
                PaymentCommand paymentCommand = inpayments.get(i);
                PaymentRepresentation paymentRepresentation = new PaymentRepresentation(paymentCommand.getType(), paymentCommand.getAmount());
                payments.add(paymentRepresentation);
            }
        }
        //计算应付金额
        BigDecimal receivables = totalPrice.subtract(totalDiscountPrice);
        //更新用户积分及等级
        User user = UserPool.getUserByCardNo(command.getMemberId());
        String oldMemberType = user.getLevel();
        int memberPointsIncreased = user.updateIntegral(receivables);
        String newMemberType = user.getLevel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            //生成输出订单
            result = new OrderRepresentation(command.getOrderId(), dateFormat.parse(command.getCreateTime()), command.getMemberId(), user.getName(), oldMemberType, newMemberType, memberPointsIncreased, user.getIntegral(), orderItems, totalPrice, discounts, totalDiscountPrice, receivables, payments, discountCards);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
