package com.drools.risk.bean;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * drools规则引擎中的fact对象，可以在workingmemory中被引用，并且通过drls中的宏对象操作
 * drools可以import类，也可以引用静态方法
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DroolsHandshakeBean implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 订单id
     */
    private Integer orderId;
    /**
     * 司机id
     */
    private Integer driverId;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 握手单数量
     */
    private Integer handshakeNum;

    private List<String> names;

    private String name;


    /**
     * 是否是风险事件
     */
    private Integer isRiskEvent;


    public static boolean testDroolsFunction(String param){

        System.out.println("条件达成 "+param);
        return true;

    }
}
