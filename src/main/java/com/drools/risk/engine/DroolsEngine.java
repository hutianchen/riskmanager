package com.drools.risk.engine;

import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 动态规则引擎
 */
@Component
public class DroolsEngine {

    @Autowired
    KieBase kieBase;

    /**
     * 风险处理
     * @param objs fact风险对象（多个）
     * @param globals 全局变量（多个）
     */
    public void handle(List<Object> objs,List<Map<String,Object>> globals) {
        //动态添加风险规则，规则可以添加多个
        KieSession kSession = kieBase.newKieSession();
        //注入事实数据
        objs.forEach(obj->kSession.insert(obj));
        //注入全局变量
        globals.forEach(global->kSession.setGlobal(global.get("key").toString(),global.get("data")));
        //引擎启动
        kSession.fireAllRules();
        kSession.dispose();
    }


}