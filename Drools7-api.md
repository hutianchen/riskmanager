## Drools7应用



### 动态规则 推荐使用方法2

  数据准备：
    public String rule2Drl(Rule rule) {
                     StringBuilder result = new StringBuilder();
                     /*package部分*/
                     result.append("package rule1;\r\n");
                     result.append("\r\n");
            
                     /*导包部分*/
                     result.append("import com.winning.rules.engine.model.Fact;\r\n");
                     result.append("import com.winning.rules.engine.model.FactProperty;\r\n");
                     result.append("import java.util.List;\r\n");
                     result.append("\r\n");
              
                     /*规则申明部分*/
                     result.append("rule \"32353242\"\r\n");
              
                     /*规则属性部分*/
              
                     /*规则条件部分*/
                     result.append("\twhen\r\n");
                     result.append("\t\teval(true)\r\n");
              
                     /*规则结果部分*/
                     result.append("\tthen\r\n");
                     result.append("\t\tSystem.out.println(\"动态加载的规则被触发了\");\r\n");
              
                     /*规则结束*/
                     result.append("end\r\n");
                     return result.toString();
                 }
    方法1：
    public void handle() {
            StatefulKnowledgeSession kSession = null;
            try {
                RuleHandle ruleHandle = new RuleHandle();
                KieHelper helper = new KieHelper();
                helper.addContent(ruleHandle.rule2Drl(null), ResourceType.DRL);
                KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();
                //装入规则，可以装入多个
                kb.add(ResourceFactory.newByteArrayResource(ruleHandle.rule2Drl(null).getBytes("utf-8")), ResourceType.DRL);
     
                KnowledgeBuilderErrors errors = kb.getErrors();
                for (KnowledgeBuilderError error : errors) {
                    System.out.println(error);
                }
                KnowledgeBase kBase = KnowledgeBaseFactory.newKnowledgeBase();
                kBase.addKnowledgePackages(kb.getKnowledgePackages());
     
     
     
                kSession = kBase.newStatefulKnowledgeSession();
                kSession.insert(new Object());
                kSession.fireAllRules();
                kSession.dispose();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
     
        }
    方法2：
    public void handle() {
            RuleHandle ruleHandle = new RuleHandle();
            KieHelper helper = new KieHelper();
            helper.addContent(ruleHandle.rule2Drl(null), ResourceType.DRL);
            KieSession kSession = helper.build().newKieSession();
            kSession.insert(new Object());
            kSession.fireAllRules();
            kSession.dispose();
        }

### 普通规则

### kmodule方式
       首先创建文件
       /resources/META-INF/kmodule.xml
      然后在resources下建立drl文件（注意包路径与kmodule.xml中的packages一致），drl中的逻辑路径与
      javabean保持一致(用处不大)

#### 动态获取KieSession，需要指定一个drl
```
    public KieSession getKieSession(String rules) {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();
        kfs.write("src/main/resources/rules/rules.drl", rules.getBytes());
        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
        Results results = kieBuilder.getResults();
        if (results.hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
            System.out.println(results.getMessages());
            throw new BusinessException(300003,results.getMessages().toString(),4);
        }
        KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        KieBase kieBase = kieContainer.getKieBase();

        return kieBase.newKieSession();
    }
```
#### 激活规则
```
    KieSession kieSession = rulesService.getKieSession(rule);
    Gson gson = new Gson();
    Person person = gson.fromJson(json, Person.class);
    kieSession.insert(person);
    kieSession.fireAllRules();
    kieSession.dispose();
```

### 决策表

#### 将文件翻译为drl文件
```
    public String getRuleTable() {
        //把excel翻译成drl文件
        SpreadsheetCompiler compiler = new SpreadsheetCompiler();
        String rules = compiler.compile(ResourceFactory.newClassPathResource(RULES_PATH + File.separator + "rule.xlsx", "UTF-8"), "rule-table");
        System.out.println(rules);
        return rules;
    }
```
#### 将文件流翻译为drl文件
```
    public String getRuleTable(InputStream inputStream) {
        //把excel翻译成drl文件
        SpreadsheetCompiler compiler = new SpreadsheetCompiler();
        String rules = compiler.compile(ResourceFactory.newInputStreamResource(inputStream, "UTF-8"), "rule-table");
        logger.info("get rule from xls:" + rules);
        return rules;
    }
```