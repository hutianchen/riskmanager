package com.drools.risk.engine;

import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;

import java.util.List;
import java.util.Map;

/**
 * 动态规则引擎
 */
public class DroolsEngine {

    /**
     * 风险处理
     * @param objs fact风险对象（多个）
     * @param globals 全局变量（多个）
     */
    public static void handle(List<Object> objs,List<Map<String,Object>> globals) {
        KieHelper helper = new KieHelper();
        //动态添加风险规则，规则可以添加多个
        helper.addContent(DynamicDroolsAdapter.rule2Drl(null), ResourceType.DRL);
        KieSession kSession = helper.build().newKieSession();
        //注入事实数据
        objs.forEach(obj->kSession.insert(obj));
        //注入全局变量
        globals.forEach(global->kSession.setGlobal(global.get("key").toString(),global.get("data")));
        //引擎启动
        kSession.fireAllRules();
        kSession.dispose();
    }
}
