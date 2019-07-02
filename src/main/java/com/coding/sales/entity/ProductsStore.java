package com.coding.sales.entity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ProductsStore {
    private static Map<String, PreciousMetal> productMap = new HashMap<String, PreciousMetal>();
    static {
        PreciousMetal preciousMetal001001 = new PreciousMetal("001001","世园会五十国钱币册", "册", new BigDecimal(998));
        productMap.put("001001", preciousMetal001001);
        PreciousMetal preciousMetal001002 = new PreciousMetal("001002","2019北京世园会纪念银章大全40g", "盒", new BigDecimal(1380));
        preciousMetal001002.addDicount(new ZkDiscount(new BigDecimal(0.9), "9折券"));
        productMap.put("001002", preciousMetal001002);
        PreciousMetal preciousMeta003001 = new PreciousMetal("003001","招财进宝", "条", new BigDecimal(1580));
        preciousMeta003001.addDicount(new ZkDiscount(new BigDecimal(0.95), "95折券"));
        productMap.put("003001", preciousMeta003001);
        PreciousMetal preciousMeta003002 = new PreciousMetal("003002","水晶之恋", "条", new BigDecimal(980));
        preciousMeta003002.addDicount(new MjDiscount(preciousMeta003002.getPrice().multiply(new BigDecimal(3)), preciousMeta003002.getPrice().multiply(new BigDecimal(0.5)), "第3件半价"));
        preciousMeta003002.addDicount(new MjDiscount(preciousMeta003002.getPrice().multiply(new BigDecimal(4)), preciousMeta003002.getPrice(), "满3送1"));
        productMap.put("003002", preciousMeta003002);
        PreciousMetal preciousMeta002002 = new PreciousMetal("002002","中国经典钱币套装", "套", new BigDecimal(998));
        preciousMeta002002.addDicount(new MjDiscount(new BigDecimal(2000), new BigDecimal(30), "每满2000元减30"));
        preciousMeta002002.addDicount(new MjDiscount(new BigDecimal(1000), new BigDecimal(10), "每满1000元减10"));
        productMap.put("002002", preciousMeta002002);
        PreciousMetal preciousMeta002001 = new PreciousMetal("002001","守扩之羽比翼双飞4.8g", "条", new BigDecimal(1080));
        preciousMeta002001.addDicount(new MjDiscount(preciousMeta002001.getPrice().multiply(new BigDecimal(3)), preciousMeta002001.getPrice().multiply(new BigDecimal(0.5)), "第3件半价"));
        preciousMeta002001.addDicount(new MjDiscount(preciousMeta002001.getPrice().multiply(new BigDecimal(4)), preciousMeta002001.getPrice(), "满3送1"));
        preciousMeta002001.addDicount(new ZkDiscount(new BigDecimal(0.95), "95折券"));
        productMap.put("002001", preciousMeta002001);
        PreciousMetal preciousMeta002003 = new PreciousMetal("002003","中国银象棋12g", "套", new BigDecimal(698));
        preciousMeta002003.addDicount(new MjDiscount(new BigDecimal(3000), new BigDecimal(350), "每满3000元减350"));
        preciousMeta002003.addDicount(new MjDiscount(new BigDecimal(2000), new BigDecimal(30), "每满2000元减30"));
        preciousMeta002003.addDicount(new MjDiscount(new BigDecimal(1000), new BigDecimal(10), "每满1000元减10"));
        preciousMeta002003.addDicount(new ZkDiscount(new BigDecimal(0.9), "9折券"));
        productMap.put("002003", preciousMeta002003);
    }

    public static PreciousMetal getPreciousMetalByNo (String productNo) {
        return productMap.get(productNo);
    }

}
