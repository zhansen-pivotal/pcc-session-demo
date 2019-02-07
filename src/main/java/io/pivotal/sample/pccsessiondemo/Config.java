package io.pivotal.sample.pccsessiondemo;

import io.pivotal.pcc.util.serializer.OAuthJSONAccessTokenResponseSerializer;
import io.pivotal.pcc.util.serializer.ZacMappingPdxSerializer;
import io.pivotal.pcc.util.serializer.ZacPdxSerializableSessionSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.geode.pdx.PdxSerializer;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.gemfire.config.annotation.EnableCachingDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnablePdx;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.session.data.gemfire.config.annotation.web.http.EnableGemFireHttpSession;
import org.springframework.session.data.gemfire.serialization.pdx.provider.PdxSerializableSessionSerializer;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.session.data.gemfire.AbstractGemFireOperationsSessionRepository.GemFireSession;

@Configuration
@EnableGemFireHttpSession(poolName = "DEFAULT",
        regionName = "Session", sessionSerializerBeanName = "zacPdxSerializableSessionSerializer")
@EnableCachingDefinedRegions
@EnableEntityDefinedRegions
@EnableGemfireRepositories
@EnablePdx(serializerBeanName = "zacMappingPdxSerializer")
@Slf4j
public class Config {
    @Bean
    ZacMappingPdxSerializer zacMappingPdxSerializer() {
        log.info("Config : Registering custom serializerBeanName [mappingPdxSerializer]");
        Map<Class, PdxSerializer> customPdxSerializers = new HashMap<>();
        customPdxSerializers.put(GemFireSession.class, new PdxSerializableSessionSerializer());
        customPdxSerializers.put(OAuthJSONAccessTokenResponse.class, new OAuthJSONAccessTokenResponseSerializer());
        ZacMappingPdxSerializer pdxSerializer = new ZacMappingPdxSerializer();
        pdxSerializer.setCustomPdxSerializers(customPdxSerializers);
        return pdxSerializer;
    }


    @Bean
    @DependsOn("zacMappingPdxSerializer")
    ZacPdxSerializableSessionSerializer zacPdxSerializableSessionSerializer() {
        log.info("Config : Registering custom serializerBeanName [mySessionSerializer]");
        return new ZacPdxSerializableSessionSerializer();
    }
}


