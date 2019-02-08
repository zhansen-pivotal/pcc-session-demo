package io.pivotal.sample.pccsessiondemo;

import io.pivotal.pcc.util.serializer.OAuthJSONAccessTokenResponseSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.geode.pdx.PdxSerializer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.data.gemfire.CacheFactoryBean;
import org.springframework.session.data.gemfire.serialization.pdx.support.ComposablePdxSerializer;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CacheFactoryBeanPostProcessor implements BeanPostProcessor, Ordered {
    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof CacheFactoryBean) {
            CacheFactoryBean cacheFactoryBean = (CacheFactoryBean) bean;
            if (cacheFactoryBean.getPdxSerializer() instanceof ComposablePdxSerializer) {
                ComposablePdxSerializer composablePdxSerializer = (ComposablePdxSerializer) cacheFactoryBean.getPdxSerializer();
                List<PdxSerializer> serializers = new ArrayList<>();
                serializers.add(new OAuthJSONAccessTokenResponseSerializer());
                composablePdxSerializer.iterator().forEachRemaining(s -> {
                    log.info("serializer classname: " + s.getClass().getName());
                    serializers.add(s);
                });
                cacheFactoryBean.setPdxSerializer(ComposablePdxSerializer.compose(serializers));
            }
            log.info("CacheFactoryBean found: " + beanName);
        }
        return bean;
    }
}
