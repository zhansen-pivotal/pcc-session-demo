package io.pivotal.sample.pccsessiondemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.config.annotation.EnableCachingDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnablePdx;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.session.data.gemfire.config.annotation.web.http.EnableGemFireHttpSession;

@Configuration
@EnableGemFireHttpSession(poolName = "DEFAULT", regionName = "Session")
@EnableCachingDefinedRegions
@EnableEntityDefinedRegions
@EnableGemfireRepositories
@EnablePdx
@Slf4j
public class Config {
    @Bean
    BeanPostProcessor customSerializerConfigurationBeanPostProcessor() {
        return new CacheFactoryBeanPostProcessor();
    }
}


