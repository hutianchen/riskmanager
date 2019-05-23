## riskmanager —— 风控系统规则引擎drools
### 什么是drools
      Drools 是一款基于 Java 的开源规则引擎，底层基于rete、phreak算法，以将复杂多变的规则从硬编码中解放
    出来，以规则脚本的形式存放在文件中，使得规则的变更不需要修正代码重启机器就可以立即在线上环境生效
    规则引擎由推理引擎发展而来，是一种嵌入在应用程序中的组件，实现了将业务决策从应用程序代码 中分离
    出来，并使用预定义的语义模块编写业务决策。接受数据输入，解释业务规则，并根据业务规则做 出业务决
    策,从而给编程带来了极大的方便。
    
### Drools 的基本工作过程
      之前我们一般的做法都是使用一个接口进行业务的工作，首先要传进去参数，其次要获 取到接口的实现执行
    完毕后的结果。其实 Drools 也大相径庭，我们需要传递进去数据，用于规则的检查，调用外部接口，同时还
    可能需要获取到规则执行完毕后得到的结果。在 drools中，这个传递数据进去的对象，术语叫 Fact 对象。
    Fact 对象是一个普通的 javabean，规则中可以对当前对象进行任何的读写操作，调用该对象提供的 方法，
    当一个 java bean 插入到 working Memory(内存储存)中，规则使用的是原有对象的 引用，规则通过对 
    fact 对象的读写，实现对应用数据的读写，对于其中的属性，需要提供 getter setter 访问器，规则中，
    可以动态的往当前 working Memory 中插入删除新的 fact 对象。
### Drools的优势
      1.规则配置逻辑与业务逻辑解耦，简化规则与业务逻辑的匹配过程的开发
      2.相同模式的节点共享，避免重复计算
      3.过程状态被保存，可在短时间内实现反复计算
      4.支持事件流、滑动窗口，解决复杂事件处理过程
### Drools的问题
      1.内存占用随着事实对象的增大成指数增长
               

#### [maven依赖] https://github.com/hfq-hutianchen/riskmanager/blob/master/Drools7-依赖.md
#### [基础语法]   https://github.com/hfq-hutianchen/riskmanager/blob/master/Drools7-基础语法.md
#### [api用法]   https://github.com/hfq-hutianchen/riskmanager/blob/master/Drools7-api.md

#### 参考资料：http://ksoong.org/drools-examples/content/

