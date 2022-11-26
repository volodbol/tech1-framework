package io.tech1.framework.b2b.mongodb.server.tests.contexts;

import io.tech1.framework.b2b.mongodb.server.services.UserService;
import io.tech1.framework.utilities.environment.EnvironmentUtility;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan({
        "io.tech1.framework.b2b.mongodb.server.resources"
})
@EnableWebMvc
public class ApplicationResourcesContext {

    // Services
    @Bean
    public UserService userService() {
        return mock(UserService.class);
    }

    // Utilities
    @Bean
    public EnvironmentUtility environmentUtils() {
        return mock(EnvironmentUtility.class);
    }
}
