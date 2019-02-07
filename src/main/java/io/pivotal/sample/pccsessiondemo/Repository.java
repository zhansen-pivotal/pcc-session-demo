package io.pivotal.sample.pccsessiondemo;

import org.springframework.data.gemfire.repository.GemfireRepository;

public interface Repository extends GemfireRepository<Entity, String> {
}
