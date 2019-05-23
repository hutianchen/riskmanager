package com.drools.risk.engine;


/**
 * 动态规则适配器
 * 用于将数据库中的规则注入drools引擎
 */
public class DynamicDroolsAdapter {

    public static String ruleDrl(String ruleDb) {

        StringBuilder result = new StringBuilder();

        /*package部分*/
        result.append("package com.drools;\r\n");
        result.append("\r\n");

        /*导包部分*/
        result.append("import com.drools.risk.bean.DroolsBrushDurationBean;\r\n");
        result.append("import com.drools.risk.bean.DroolsHandshakeBean;\r\n");
        result.append("\r\n");
        result.append("global java.util.List list;\r\n");

        /*规则申明部分*/
        result.append("rule \"htc.drools.rule.htc.drools.rule.group.handshake\"\r\n");

        /*规则属性部分*/
        result.append("salience 3 \r\n");
        result.append("no-loop true \r\n");
        result.append("lock-on-active true \r\n");

        /*规则条件部分*/
        result.append("\twhen\r\n");
        result.append("\t\t $p : DroolsHandshakeBean(name matches \"12.*\",handshakeNum > 80,names contains \"123\",name memberOf list)\r\n");

        /*规则结果部分*/
        result.append("\tthen\r\n");
        result.append("\t\t$p.setIsRiskEvent(1);\r\n");
        result.append("\t\tupdate($p)\r\n");
        result.append("\t\tSystem.out.println(\"规则1风险事件生成成功1111111，userid=\"+$p.getUserId());\r\n");

        /*规则结束*/
        result.append("end\r\n");
        return result.toString();
    }

    public static String rule2Drl(String ruleDb) {

        StringBuilder result = new StringBuilder();

        /*package部分*/
        result.append("package com.drools;\r\n");
        result.append("\r\n");

        /*导包部分*/
        result.append("import com.drools.risk.bean.DroolsBrushDurationBean;\r\n");
        result.append("import com.drools.risk.bean.DroolsHandshakeBean;\r\n");
        result.append("\r\n");
        result.append("global java.util.List list;\r\n");

        /*规则申明部分*/
        result.append("rule \"htc.drools.rule.htc.drools.rule.group.handshake\"\r\n");

        /*规则属性部分*/
        result.append("salience 3 \r\n");
        result.append("no-loop true \r\n");
        result.append("lock-on-active true \r\n");

        /*规则条件部分*/
        result.append("\twhen\r\n");
        result.append("\t\t $p : DroolsHandshakeBean(name matches \"12.*\",handshakeNum > 80,names contains \"123\",name memberOf list)\r\n");

        /*规则结果部分*/
        result.append("\tthen\r\n");
        result.append("\t\t$p.setIsRiskEvent(1);\r\n");
        result.append("\t\tupdate($p)\r\n");
        result.append("\t\tSystem.out.println(\"规则1风险事件生成成功，userid=\"+$p.getUserId());\r\n");

        /*规则结束*/
        result.append("end\r\n");
        return result.toString();
    }
}
