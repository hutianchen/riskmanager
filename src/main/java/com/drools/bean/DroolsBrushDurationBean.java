package com.drools.bean;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * drools规则引擎中的fact对象，可以在workingmemory中被引用，并且通过drls中的宏对象操作
 * drools可以import类，也可以引用静态方法
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DroolsBrushDurationBean implements Serializable {

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
     * 时长
     */
    private Integer durationVal;

    /**
     * 是否是风险事件
     */
    private Integer isRiskEvent;

    /**
     * 握手单bean
     */
    DroolsHandshakeBean droolsHandshakeBean;


    public static void testDrools(){

    }
}
