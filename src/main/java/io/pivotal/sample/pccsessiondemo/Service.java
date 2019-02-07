package io.pivotal.sample.pccsessiondemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.UUID;

@org.springframework.stereotype.Service
public class Service {

    @Autowired
    Repository repository;

    @Cacheable("cacheable")
    public Entity getEntity(String id) {
        return repository.findById(id).orElse(null);
    }

    @CachePut(value = "cacheable", key = "#entity.id")
    public Entity saveEntity(Entity entity) {
        return repository.save(entity);
    }

    public Entity createNewEntity() {
        return Entity
                .builder()
                .id(UUID.randomUUID().toString())
                .value(UUID.randomUUID().toString())
                .build();
    }

    @CacheEvict(value = "cacheable", key = "#entity.id")
    public boolean updateEntity(Entity entity) {
        repository.save(entity);
        return true;
    }
}
