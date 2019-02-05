package io.pivotal.sample.pccsessiondemo;

import org.springframework.session.data.gemfire.config.annotation.web.http.EnableGemFireHttpSession;
import org.springframework.stereotype.Component;

@EnableGemFireHttpSession(regionName = "Session")
@Component
public class GeodeConfig {
}
