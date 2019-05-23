package com.drools.risk.config;

import com.drools.risk.engine.DynamicDroolsAdapter;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.kie.api.KieBase;
import org.kie.api.io.ResourceType;
import org.kie.internal.utils.KieHelper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.concurrent.ListenableFutureTask;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CaffenieConfig {

    public static final int DEFAULT_MAXSIZE = 50000;
    public static final int DEFAULT_TTL = 10;
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    /**
     * 定义cache名称、超时时长（秒）、最大容量
     * 每个cache缺省：10秒超时、最多缓存50000条数据，需要修改可以在构造方法的参数中指定。
     */
    public enum Caches{
        kieBase(10,2), //有效期5秒
        getSomething, //缺省10秒
        getOtherthing(300, 1000), //5分钟，最大容量1000
        ;

        Caches() {
        }

        Caches(int ttl) {
            this.ttl = ttl;
        }

        Caches(int ttl, int maxSize) {
            this.ttl = ttl;
            this.maxSize = maxSize;
        }

        private int maxSize=DEFAULT_MAXSIZE;	//最大數量
        private int ttl=DEFAULT_TTL;		//过期时间（秒）

        public int getMaxSize() {
            return maxSize;
        }
        public int getTtl() {
            return ttl;
        }
    }

    /**
     * 必须要指定这个Bean，refreshAfterWrite=5s这个配置属性才生效
     *
     * @return
     */
    @Bean
    public CacheLoader<Object, Object> cacheLoader() throws Exception {

        CacheLoader<Object, Object> cacheLoader = new CacheLoader<Object, Object>() {

            @Override
            public Object load(Object key) throws Exception {
                System.out.println("load方法被执行");
                Thread.sleep(5000);
                KieHelper helper = new KieHelper();
                //动态添加风险规则，规则可以添加多个
                helper.addContent(DynamicDroolsAdapter.rule2Drl(null), ResourceType.DRL);
                KieBase kieBase = helper.build();
                return kieBase;
            }

            // 重写这个方法将oldValue值返回回去，进而刷新缓存
            @Override
            public Object reload(Object key, Object oldValue) throws Exception {
                System.out.println("reload方法被执行");
                Thread.sleep(50);
                ListenableFutureTask<KieBase> futureTask = new ListenableFutureTask<KieBase>(()->{
                    KieHelper helper = new KieHelper();
                    //动态添加风险规则，规则可以添加多个
                    helper.addContent(DynamicDroolsAdapter.rule2Drl(null), ResourceType.DRL);
                    KieBase kieBase = helper.build();
                    return kieBase;
                });
                executorService.execute(futureTask);
                return futureTask.get();
            }
        };
        return cacheLoader;
    }


        /**
         * 创建基于Caffeine的Cache Manager
         * @return
         */
        @Bean
        @Primary
        public CacheManager caffeineCacheManager() throws Exception {
            SimpleCacheManager cacheManager = new SimpleCacheManager();
            ArrayList<CaffeineCache> caches = new ArrayList<CaffeineCache>();
            for(Caches c : Caches.values()){
                caches.add(new CaffeineCache(c.name(),
                        Caffeine.newBuilder().recordStats()
                                .initialCapacity(1)
//                                .expireAfterWrite(c.getTtl(), TimeUnit.SECONDS)
                                .refreshAfterWrite(c.getTtl(),TimeUnit.SECONDS)
                                .maximumSize(1)
                                .build(cacheLoader())
                ));
            }
            cacheManager.setCaches(caches);
            return cacheManager;
        }

}
