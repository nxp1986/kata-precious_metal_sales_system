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
        List<OrderItemRepresentation> orderItems = new ArrayList<OrderItemRepresentation>();
        List<DiscountItemRepresentation> discounts = new ArrayList<DiscountItemRepresentation>();
        List<PaymentRepresentation> payments = new ArrayList<PaymentRepresentation>();
        List<String> discountCards = new ArrayList<String>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal totalDiscountPrice = BigDecimal.ZERO;
        if(orderItemCommandList != null && orderItemCommandList.size() > 0) {
            for (int i = 0; i < orderItemCommandList.size(); i++) {
                OrderItemCommand orderItemCommand = orderItemCommandList.get(i);
                PreciousMetal preciousMetal = ProductsStore.getPreciousMetalByNo(orderItemCommand.getProduct());
                BigDecimal itemPrice = preciousMetal.getPrice().multiply(orderItemCommand.getAmount());
                totalPrice = totalPrice.add(itemPrice);
                OrderItemRepresentation  orderItemRepresentation = new OrderItemRepresentation(preciousMetal.getProductNo(), preciousMetal.getProductName(), preciousMetal.getPrice(), orderItemCommand.getAmount(), itemPrice);
                orderItems.add(orderItemRepresentation);
                Discount bestDiscount = preciousMetal.getBestDiscount(orderItemCommand, indiscounts);
                if(bestDiscount != null) {
                    BigDecimal discountPrice = bestDiscount.getAmount(orderItemCommand);
                    DiscountItemRepresentation discountItemRepresentation = new DiscountItemRepresentation(preciousMetal.getProductNo(), preciousMetal.getProductName(), discountPrice);
                    discounts.add(discountItemRepresentation);
                    totalDiscountPrice = totalDiscountPrice.add(discountPrice);
                    //1.1 计算用户订单中是否有打折券
                    if(bestDiscount instanceof ZkDiscount) {
                        discountCards.add(bestDiscount.getDesc());
                    }
                    //1.2 计算用户订单中是否包含开门红活动
                }
            }
        }
        if(inpayments!=null && inpayments.size() > 0) {
            for (int i = 0; i < inpayments.size(); i++) {
                PaymentCommand paymentCommand = inpayments.get(i);
                PaymentRepresentation paymentRepresentation = new PaymentRepresentation(paymentCommand.getType(), paymentCommand.getAmount());
                payments.add(paymentRepresentation);
            }
        }
        BigDecimal receivables = totalPrice.subtract(totalDiscountPrice);
        User user = UserPool.getUserByCardNo(command.getMemberId());
        String oldMemberType = user.getLevel();
        int memberPointsIncreased = user.updateIntegral(receivables);
        String newMemberType = user.getLevel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            result = new OrderRepresentation(command.getOrderId(), dateFormat.parse(command.getCreateTime()), command.getMemberId(), user.getName(), oldMemberType, newMemberType, memberPointsIncreased, user.getIntegral(), orderItems, totalPrice, discounts, totalDiscountPrice, receivables, payments, discountCards);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
