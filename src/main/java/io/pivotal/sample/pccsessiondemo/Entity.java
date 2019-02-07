package io.pivotal.sample.pccsessiondemo;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;

@Data
@Builder
@Region("entity")
public class Entity {
    @Id
    String id;
    String value;
}
