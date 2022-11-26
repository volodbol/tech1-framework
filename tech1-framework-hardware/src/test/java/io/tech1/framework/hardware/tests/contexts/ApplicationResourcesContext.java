package io.tech1.framework.hardware.tests.contexts;

import io.tech1.framework.hardware.monitoring.publishers.HardwareMonitoringPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan({
        // -------------------------------------------------------------------------------------------------------------
        "io.tech1.framework.hardware.monitoring.resources"
        // -------------------------------------------------------------------------------------------------------------
})
@EnableWebMvc
public class ApplicationResourcesContext {
    @Bean
    public HardwareMonitoringPublisher hardwareMonitoringPublisher() {
        return mock(HardwareMonitoringPublisher.class);
    }
}
