import com.drools.bean.DroolsBrushDurationBean;
import com.drools.bean.DroolsHandshakeBean;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;

import java.util.ArrayList;
import java.util.List;

public class droolsTest {

    public static void main(String[] args) {

        KieServices kss  = KieServices.Factory.get();
        //默认的hbase容器
        KieContainer kc = kss.getKieClasspathContainer();

        //1.构建有状态的kie会话
        KieSession ks = kc.newKieSession("ksession1");
        List<String> names =  new ArrayList<>();
        names.add("123");
        names.add("456");
        DroolsHandshakeBean handshakeBean = DroolsHandshakeBean.builder().handshakeNum(100).driverId(1).orderId(1).userId(1).names(names).name("123").build();
        DroolsHandshakeBean handshakeBean2 = DroolsHandshakeBean.builder().handshakeNum(90).driverId(1).orderId(1).userId(1).names(names).name("123").build();
        DroolsBrushDurationBean droolsDuationBean = DroolsBrushDurationBean.builder().durationVal(100).userId(3).droolsHandshakeBean(handshakeBean).build();
        ks.insert(handshakeBean);
        ks.insert(handshakeBean2);
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
