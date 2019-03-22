import com.DroolsApplication;
import com.drools.risk.bean.DroolsBrushDurationBean;
import com.drools.risk.bean.DroolsHandshakeBean;
import com.drools.risk.engine.DroolsEngine;
import com.drools.risk.engine.DynamicDroolsAdapter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.kie.internal.utils.KieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = DroolsApplication.class)
@RunWith(SpringRunner.class)
public class DroolsTest {

    @Autowired
    KieBase kieBase;
    @Autowired
    DroolsEngine droolsEngine;
    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void droolsAction(){
        List<String> names =  new ArrayList<>();
        names.add("123");
        //构建事实数据
        DroolsHandshakeBean handshakeBean = DroolsHandshakeBean.builder().handshakeNum(100).driverId(1).orderId(1).userId(1).names(names).name("123").build();
        DroolsBrushDurationBean droolsDuationBean = DroolsBrushDurationBean.builder().durationVal(100).userId(3).droolsHandshakeBean(handshakeBean).build();
        List<Object> facts = new ArrayList<>();
        facts.add(handshakeBean);
        facts.add(droolsDuationBean);
        //构建全局变量数据
        List<Map<String,Object>> globals = new ArrayList<>();
        Map<String,Object> map =  new HashMap<>();
        map.put("data",names);
        map.put("key","list");
        globals.add(map);
        //执行引擎
        droolsEngine.handle(facts,globals);

    }



    /**
     * 测试动态规则引擎
     * @param args
     */
    public static void main(String[] args) {

    }

    /**
     * 测试静态规则引擎
     * @param args
     */
    public static void main1(String[] args) {

        KieServices kss  = KieServices.Factory.get();
        //默认的hbase容器
        KieContainer kc = kss.getKieClasspathContainer();

        //1.构建有状态的kie会话
        KieSession ks = kc.newKieSession("ksession1");
        List<String> names =  new ArrayList<>();
        names.add("123");
        DroolsHandshakeBean handshakeBean = DroolsHandshakeBean.builder().handshakeNum(100).driverId(1).orderId(1).userId(1).names(names).name("123").build();
        DroolsBrushDurationBean droolsDuationBean = DroolsBrushDurationBean.builder().durationVal(100).userId(3).droolsHandshakeBean(handshakeBean).build();
        ks.insert(handshakeBean);
        ks.insert(droolsDuationBean);
        ks.setGlobal("list",names);
//        ks.getAgenda().getAgendaGroup("focus1").setFocus();
        int count = ks.fireAllRules();
        QueryResults ksQueryResults = ks.getQueryResults("isriskevent == 1");
        for(QueryResultsRow resultsRow : ksQueryResults){
            DroolsHandshakeBean droolsHandshakeBean = (DroolsHandshakeBean) resultsRow.get("droolsHandshakeBean");
            System.out.println("握手单数量："+droolsHandshakeBean.getHandshakeNum());
        }
        System.out.println("有状态会话共执行规则数量："+count+",修改对象之后结果,isRiskEvent="+droolsDuationBean.getIsRiskEvent());
        ks.dispose();

        //2.构建无状态的kie会话
//        StatelessKieSession ks2 = kc.newStatelessKieSession("ksession2");
//        DroolsBrushDurationBean droolsDemoBean2 = DroolsBrushDurationBean.builder().handshakeNum(200).build();
//        KieCommands commands = kss.getCommands();
//        commands.newInsert(droolsDemoBean2);
//        ks2.execute(commands);
//        System.out.println("无状态会话共执行规则数量："+",修改对象之后结果,isRiskEvent="+droolsDemoBean2.getIsRiskEvent());


    }
}
