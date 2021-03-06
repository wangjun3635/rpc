package com.ynhuang;


import com.ynhuang.server.RPCServer;
import com.ynhuang.zk.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author ynhuang
 * @date 2019/4/11
 */
@Configuration
@EnableConfigurationProperties(RPCServerProperties.class)
@ConditionalOnMissingBean(RPCServer.class)
@Slf4j
public class RPCServerAutoConfiguration {

    @Autowired
    private RPCServerProperties properties;

    @Resource
    private ApplicationContext applicationContext;

    @Bean
    public RPCServer rpcServer() {

        log.info("开始初始化RPCServer");

        log.info("配置文件读取结果:{}",properties);

        ServiceRegistry registry = new ServiceRegistry(properties.getRegistryAddress());

        RPCServer rpcServer = new RPCServer(properties.getServiceBasePackage(), registry, applicationContext);
        rpcServer.run(properties.getExportAddress());

        return rpcServer;
    }
    
}
