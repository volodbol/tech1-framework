package io.tech1.framework.b2b.base.security.jwt.tests.contexts;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.tech1.framework.b2b.base.security.jwt.utils.HttpRequestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan({
        "io.tech1.framework.b2b.base.security.jwt.handlers"
})
public class TestsApplicationHandlersContext {

    @Bean
    public HttpRequestUtils httpRequestUtility() {
        return mock(HttpRequestUtils.class);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
