package com.coding.sales.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户信息池
 * 初始化系统用户信息
 */
public class UserPool {
    private static Map<String, User> userMap = new HashMap<String, User>();
    static {
        User userm = new User("马丁", "普卡", "6236609999", 9860);
        userMap.put(userm.getCardNo(), userm);
        User userw = new User("王立", "金卡", "6630009999", 48860);
        userMap.put(userw.getCardNo(), userw);
        User userl = new User("李想", "白金卡", "8230009999", 98860);
        userMap.put(userl.getCardNo(), userl);
        User userz = new User("张三", "钻石卡", "9230009999", 198860);
        userMap.put(userz.getCardNo(), userz);
    }

    /**
     * 根据用户卡号获取用户信息
     * @param cardNo
     * @return
     */
    public static User getUserByCardNo(String cardNo) {
        return userMap.get(cardNo);
    }
}
